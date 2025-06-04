package me.lucky.roleManager.events.discord.RemoveWhitelistDiscord;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import net.dv8tion.jda.api.interactions.InteractionHook;

public record RemoveWhitelistDiscordCommand(InteractionHook hook, long discordUserId) implements Command<Voidy> {

}
