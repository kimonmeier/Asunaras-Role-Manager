package me.lucky.roleManager.events.discord.BanFromWhitelist;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import net.dv8tion.jda.api.interactions.InteractionHook;

public record BanFromWhitelistCommand(InteractionHook hook, long discordId, String reason) implements Command<Voidy> {
}
