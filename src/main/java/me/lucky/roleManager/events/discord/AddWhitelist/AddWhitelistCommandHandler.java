package me.lucky.roleManager.events.discord.AddWhitelist;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.google.inject.Inject;
import me.lucky.roleManager.data.dao.PlayerDAO;
import me.lucky.roleManager.data.dao.WhitelistDAO;
import me.lucky.roleManager.data.entities.Whitelist;

public class AddWhitelistCommandHandler implements Command.Handler<AddWhitelistCommand, Voidy>{
    @Inject
    private PlayerDAO playerDAO;

    @Inject
    private WhitelistDAO whitelistDAO;


    @Override
    public Voidy handle(AddWhitelistCommand command) {
        var whitelist = whitelistDAO.findByDiscordId(command.discordId());

        if (whitelist != null) {
            command.hook().sendMessage("Der User ist bereits gewhitelistet!").queue();
            return null;
        }

        var player = playerDAO.findByMinecraftName(command.minecraftName());

        if (player == null) {
            command.hook().sendMessage("Der Spieler mit den Name " + command.minecraftName() + " wurde nicht gefunden!").queue();
            return null;
        }

        var whitelistToCreate = new Whitelist();
        whitelistToCreate.setDiscordId(command.discordId());
        whitelistToCreate.setPlayer(player);
        whitelistDAO.create(whitelistToCreate);

        command.hook().sendMessage("Der Spieler wurde erfolgreich gewhitelistet!").queue();
        return null;
    }
}
