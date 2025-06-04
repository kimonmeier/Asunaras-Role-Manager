package me.lucky.roleManager.events.discord.VerifyUser;

import an.awesome.pipelinr.Command;


public record VerifyUserCommand(String minecraftName, long discordId) implements Command<String> {
}
