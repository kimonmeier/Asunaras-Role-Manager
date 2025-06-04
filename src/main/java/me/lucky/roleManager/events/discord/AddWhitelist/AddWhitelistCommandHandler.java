package me.lucky.roleManager.events.discord.AddWhitelist;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Voidy;
import com.google.inject.Inject;
import me.lucky.roleManager.data.dao.PlayerDAO;
import me.lucky.roleManager.data.dao.WhitelistDAO;
import me.lucky.roleManager.data.entities.Whitelist;
import me.lucky.roleManager.events.discord.PlayerWhitelisted.PlayerWhitelistedEvent;

public class AddWhitelistCommandHandler implements Command.Handler<AddWhitelistCommand, Voidy>{
    @Inject
    private PlayerDAO playerDAO;

    @Inject
    private WhitelistDAO whitelistDAO;

    @Inject
    private Pipeline pipeline;


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

        pipeline.send(new PlayerWhitelistedEvent(command.discordId(), player.getMinecraftId()));

        var whitelistToCreate = new Whitelist();
        whitelistToCreate.setDiscordId(command.discordId());
        whitelistToCreate.setPlayer(player);
        whitelistDAO.create(whitelistToCreate);

        command.hook().sendMessage("Der Spieler wurde erfolgreich gewhitelistet!").queue();
        return null;
    }
}
