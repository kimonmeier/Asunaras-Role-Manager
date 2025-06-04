package me.lucky.roleManager.interactions;

public final class CommandsConst {

    public static final String PARAMETER_DISCORD_USER = "discorduser";

    public static final String PARAMETER_MINECRAFT_NAME = "minecraftname";

    public static class Whitelist {

        public static final String DISCORD_USER_SUBCOMMAND = "discord";

        public static final String MINECRAFT_USER_SUBCOMMAND = "minecraft";

        public static class Remove {
            public static final String REMOVE_GROUP_NAME = "remove";
        }

        public static class Add {
            public static final String ADD_SUBCOMMAND = "add";
        }

        public static class Show {
            public static final String SHOW_GROUP_NAME = "show";
        }

    }

}
