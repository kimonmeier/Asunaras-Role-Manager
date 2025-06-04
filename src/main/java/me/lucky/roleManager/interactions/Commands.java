package me.lucky.roleManager.interactions;

public enum Commands {
    VERIFY("verify"),
    WHITELIST("whitelist");

    private final String commandName;

    Commands(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

}
