package me.lucky.roleManager.events.discord.RemoveWhitelistMinecraft;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import an.awesome.pipelinr.Voidy;
import com.google.inject.Inject;
import me.lucky.roleManager.data.dao.PlayerDAO;
import me.lucky.roleManager.data.dao.WhitelistDAO;
import me.lucky.roleManager.events.discord.PlayerUnwhitelisted.PlayerUnwhitelistedEvent;

public class RemoveWhitelistMinecraftCommandHandler implements Command.Handler<RemoveWhitelistMinecraftCommand, Voidy> {
    @Inject
    private WhitelistDAO whitelistDAO;

    @Inject
    private PlayerDAO playerDAO;

    @Inject
    private Pipeline pipeline;

    @Override
    public Voidy handle(RemoveWhitelistMinecraftCommand command) {
        var player = playerDAO.findByMinecraftName(command.minecraftName());

        if (player == null) {
            command.hook().sendMessage("Der angegebene Spieler wurde nicht gefunden!").queue();
            return null;
        }

        var whitelist = whitelistDAO.findByPlayer(player.getId());

        if (whitelist == null) {
            command.hook().sendMessage("Der angegeben Spieler wurde nicht auf der Whitelist gefunden!").queue();
            return null;
        }

        pipeline.send(new PlayerUnwhitelistedEvent(whitelist.getDiscordId()));

        whitelistDAO.delete(whitelist);
        command.hook().sendMessage("Der angegebene Spieler wurde erfolgreich von der Whitelist entfernt!").queue();
        return null;
    }
}
