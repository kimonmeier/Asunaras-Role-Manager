package me.lucky.roleManager.events.discord.RemoveWhitelistDiscord;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Voidy;
import com.google.inject.Inject;
import me.lucky.roleManager.data.dao.WhitelistDAO;
import me.lucky.roleManager.events.discord.PlayerUnwhitelisted.PlayerUnwhitelistedEvent;

public class RemoveWhitelistDiscordCommandHandler implements Command.Handler<RemoveWhitelistDiscordCommand, Voidy> {
    @Inject
    private WhitelistDAO whitelistDAO;

    @Inject
    private Pipeline pipeline;

    @Override
    public Voidy handle(RemoveWhitelistDiscordCommand command) {
        var whitelist = whitelistDAO.findByDiscordId(command.discordUserId());

        if (whitelist == null) {
            command.hook().sendMessage("Der angegebene User hat keine Whitelist!").queue();
            return null;
        }

        pipeline.send(new PlayerUnwhitelistedEvent(command.discordUserId()));

        whitelistDAO.delete(whitelist);
        command.hook().sendMessage("Die Whitelist für den User wurde erfolgreich gelöscht!").queue();
        return null;
    }
}
