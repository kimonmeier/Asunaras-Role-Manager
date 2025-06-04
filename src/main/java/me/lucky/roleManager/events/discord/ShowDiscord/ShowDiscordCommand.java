package me.lucky.roleManager.events.discord.ShowDiscord;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import net.dv8tion.jda.api.interactions.InteractionHook;

public record ShowDiscordCommand(InteractionHook hook, String minecraftName) implements Command<Voidy> {
}
