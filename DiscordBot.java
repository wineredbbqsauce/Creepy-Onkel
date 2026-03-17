import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import javax.security.auth.login.LoginException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiscordBot extends ListenerAdapter {

    // Grupper av meldinger som skal sendes tilfeldig
    private static final String[] MESSAGES = {
        "Hei {user}! Du er utvalgt til å motta en tilfeldig melding! 🎉",
        "{user}, her kommer en overraskelse for deg! 👋",
        "Psst, {user}! Jeg valgte deg tilfeldig! 😄",
        "{user}, du er heldig i dag! 🍀",
        "Hallo {user}! Du er den utvalgte personen! ⭐"
    };

    private static final Random RANDOM = new Random();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws LoginException {
        // Hent token fra environment variabel eller hardkod den (IKKE anbefalt!)
        String token = System.getenv("DISCORD_TOKEN");
        if (token == null) {
            System.err.println("Error: DISCORD_TOKEN environment variable ikke satt!");
            System.exit(1);
        }

        // Opprett JDA instans
        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(new DiscordBot())
                .build();

        try {
            jda.awaitReady();
            System.out.println("Bot er klar!");

            // Start scheduler for å sende tilfeldige meldinger
            startRandomMessageScheduler(jda);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starter en scheduler som sender tilfeldige meldinger til tilfeldige medlemmer
     * hver N minutt
     */
    private static void startRandomMessageScheduler(JDA jda) {
        // Sender melding hver 15. minutt
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // Velg en tilfeldig server (guild) som botten er medlem av
                List<Guild> guilds = jda.getGuilds();
                if (guilds.isEmpty()) {
                    System.out.println("Bot er ikke medlem av noen servere enda.");
                    return;
                }

                Guild guild = guilds.get(RANDOM.nextInt(guilds.size()));
                
                // Hent alle medlemmer som ikke er bot
                List<Member> members = new ArrayList<>(guild.getMembers());
                members.removeIf(Member::getUser).removeIf(member -> member.getUser().isBot());

                if (members.isEmpty()) {
                    System.out.println("Ingen medlemmer å velge fra i " + guild.getName());
                    return;
                }

                // Velg en tilfeldig medlem
                Member randomMember = members.get(RANDOM.nextInt(members.size()));
                
                // Velg en tilfeldig melding
                String message = MESSAGES[RANDOM.nextInt(MESSAGES.length)];
                
                // Erstatt {user} med medlemmets navn
                message = message.replace("{user}", randomMember.getAsMention());

                // Send direktemelding til medlemmet
                randomMember.getUser().openPrivateChannel()
                        .flatMap(channel -> channel.sendMessage(message))
                        .queue(
                            success -> System.out.println("Sendt melding til " + randomMember.getEffectiveName()),
                            error -> System.err.println("Kunne ikke sende melding til " + randomMember.getEffectiveName() + ": " + error.getMessage())
                        );

            } catch (Exception e) {
                System.err.println("Feil ved sending av tilfeldig melding: " + e.getMessage());
                e.printStackTrace();
            }
        }, 1, 15, TimeUnit.MINUTES); // Start etter 1 minutt, deretter hver 15. minutt
    }

    /**
     * Håndter meldinger hvis du ønsker kommandoer
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Ignorer meldinger fra bots
        if (event.getAuthor().isBot()) {
            return;
        }

        String message = event.getMessage().getContentRaw();

        // Eksempel kommando: !ping
        if (message.equalsIgnoreCase("!ping")) {
            event.getChannel().sendMessage("Pong! 🏓").queue();
        }

        // Eksempel kommando: !sendnow (sender en melding nå)
        if (message.equalsIgnoreCase("!sendnow")) {
            sendRandomMessageNow(event.getGuild());
        }
    }

    /**
     * Hjelpefunksjon for å sende en melding nå (brukes av !sendnow kommando)
     */
    private void sendRandomMessageNow(Guild guild) {
        if (guild == null) {
            return;
        }

        List<Member> members = new ArrayList<>(guild.getMembers());
        members.removeIf(Member::getUser).removeIf(member -> member.getUser().isBot());

        if (members.isEmpty()) {
            return;
        }

        Member randomMember = members.get(RANDOM.nextInt(members.size()));
        String message = MESSAGES[RANDOM.nextInt(MESSAGES.length)];
        message = message.replace("{user}", randomMember.getAsMention());

        randomMember.getUser().openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(message))
                .queue();
    }
}