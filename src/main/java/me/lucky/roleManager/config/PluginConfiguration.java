package me.lucky.roleManager.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;

@Configuration
public final class PluginConfiguration {

    @Configuration
    public static class DiscordConfiguration {
        @Comment("Der Discord-Bot Token")
        public String Token = "<<REPLACE>>";

        @Comment("Die Aktivität welche auf den Server angezeigt werden soll")
        public String Activity = "Schaut auf Joy";
    }

    public DiscordConfiguration Discord = new DiscordConfiguration();
}
