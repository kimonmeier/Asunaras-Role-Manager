package me.lucky.roleManager.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;

import java.util.Dictionary;
import java.util.HashMap;

@Configuration
public final class PluginConfiguration {

    @Configuration
    public static class DiscordConfiguration {
        @Comment("Der Discord-Bot Token")
        public String Token = "<<REPLACE>>";

        @Comment("Die Aktivität welche auf den Server angezeigt werden soll")
        public String Activity = "Schaut auf Joy";

        @Comment("Die Rollen-ID")
        public long MinecraftRoleId = -1L;
    }

    @Configuration
    public static class MinecraftConfiguration {
        @Comment("Die Rollen für die Minecraft")
        public HashMap<Long, String> Rollen = new HashMap<>();

        public MinecraftConfiguration() {
            Rollen = new HashMap<>();
            Rollen.put(1L, "A");
            Rollen.put(2L, "B");
        }
    }

    public DiscordConfiguration Discord = new DiscordConfiguration();

    public MinecraftConfiguration Minecraft = new MinecraftConfiguration();
}
