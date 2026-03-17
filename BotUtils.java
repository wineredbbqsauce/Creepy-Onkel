import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import java.awt.Color;
import java.time.Instant;

// Hjelpeklasse med avanserte features for Discord botten
public class BotUtils {
    // Lag en pen embed melding
    public static MessageEmbed createEmbedMessage(String title, String description, String userName) {
        return new EmbedBuilder()
            .setTitle(title)
            .setDescription(description + "\n\nHalla Schnuppa..." + userName)
            .setColor(Color.BLUE)
            .setFooter("Onkel er fortsatt under arbeid v1.0")
            .setTimestamp(Instant.now())
            .build();
    }

    // Eksempel på en mer aanser melding med flere felt
    public static MessageEmbed createDetailedMessage(String userName) {
        return new EmbedBuilder()
                .setTitle("🎉 Hei du søta!")
                .setColor(Color.GREEN)
                .addField("Medlemsnavn", userName, false)
                .addField("Valgt av", "Random Bot v1.0", true)
                .addField("Tid", java.time.LocalDateTime.now().toString(), true)
                .setThumbnail("https://discord.com/assets/6debd47e55ce478674ad4409d942ba4b.png")
                .setFooter("Lykke til! :)")
                .setTimestamp(Instant.now())
                .build();
    }

    
    /**
     * Kategori for meldinger - kan bruke hvis du ønsker ulik melding type
     */
    public static class MessageCategories {
        public static final String[] GREETING = {
            "Hei {user}! Du er utvalgt! 👋",
            "Hallo {user}, overraskelse! 🎊",
            "Velkommen, {user}! 🌟"
        };
 
        public static final String[] FUNNY = {
            "{user}, se det... du var den veldigste! 😄",
            "{user}, plot twist: DU var valgt! 🎭",
            "Surprise! {user}, det var deg! 🎈"
        };
 
        public static final String[] MOTIVATIONAL = {
            "{user}, du er litt spesiell! ⭐",
            "Du har blitt valgt, {user}! Bruk kraften din klokt! 💪",
            "{user}, dagen din begynner nå! 🚀"
        };
    }
 
    /**
     * Velg en random melding fra en kategori
     */
    public static String getRandomMessage(String[] messages) {
        return messages[new java.util.Random().nextInt(messages.length)];
    }
 
    /**
     * Valider at en member er valid (ikke bot, ikke offline osv)
     */
    public static boolean isValidMember(net.dv8tion.jda.api.entities.Member member) {
        return member != null 
                && !member.getUser().isBot() 
                && !member.getUser().isSystem()
                && !member.isPending();
    }
 
    /**
     * Eksempel på logging
     */
    public static void logAction(String action, String details) {
        String timestamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
        System.out.println("[" + timestamp + "] " + action + " - " + details);
    }
}

/**
 * Eksempel på en enkel konfigurasjonsklasse
 */
class BotConfig {
    // Meldinger som skal sendes
    public static final String[] MESSAGES = {
        "Hei {user}! Du er utvalgt til å motta en tilfeldig melding! 🎉",
        "{user}, her kommer en overraskelse for deg! 👋",
        "Psst, {user}! Jeg valgte deg tilfeldig! 😄",
        "{user}, du er heldig i dag! 🍀",
        "Hallo {user}! Du er den utvalgte personen! ⭐",
        "Overraskelse, {user}! 🎊",
        "{user}, du fikk jackpoten! 🎰"
    };
 
    // Konfigurering
    public static final int INITIAL_DELAY_MINUTES = 1;
    public static final int INTERVAL_MINUTES = 15;
    public static final boolean USE_EMBEDS = false; // Sett til true for å bruke embed meldinger
    public static final boolean LOG_ACTIONS = true;
}