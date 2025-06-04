package me.lucky.roleManager.events.discord.VerifyUser;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import net.dv8tion.jda.api.interactions.InteractionHook;


public record VerifyUserCommand(InteractionHook hook, String minecraftName, long discordId) implements Command<Voidy> {
}
