package me.lucky.roleManager.events.discord.PlayerWhitelisted;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;

public record PlayerWhitelistedEvent(long discordId) implements Command<Voidy> {
}
