package me.lucky.roleManager.events.discord.PlayerUnwhitelisted;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;

public record PlayerUnwhitelistedEvent(long discordId) implements Command<Voidy> {
}
