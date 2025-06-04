package me.lucky.roleManager.events.discord.AddWhitelist;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import net.dv8tion.jda.api.interactions.InteractionHook;

public record AddWhitelistCommand(InteractionHook hook, long discordId,
                                  String minecraftName) implements Command<Voidy> {

}
