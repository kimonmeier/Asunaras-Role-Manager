package me.lucky.roleManager.events.discord.VerifyUser;

import an.awesome.pipelinr.Command;
import com.google.inject.Inject;
import me.lucky.roleManager.data.dao.PlayerDAO;
import me.lucky.roleManager.data.dao.WhitelistDAO;
import me.lucky.roleManager.data.entities.Whitelist;

public class VerifyUserCommandHandler implements Command.Handler<VerifyUserCommand, String> {

    @Inject
    private PlayerDAO playerDAO;

    @Inject
    private WhitelistDAO whitelistDAO;

    @Override
    public String handle(VerifyUserCommand verifyUserCommand) {
        var whitelist = whitelistDAO.findByDiscordId(verifyUserCommand.discordId());

        if (whitelist != null) {
            return "Du bist bereits mit einem Benutzer verifiziert. Bitte wende dich an den Support!";
        }

        var player = playerDAO.findByMinecraftName(verifyUserCommand.minecraftName());

        if (player == null) {
            return "Es wurde kein Spieler mit diesem Namen gefunden! Stell sicher, dass du mindestens einmal auf den Server gejoint bist!";
        }

        Whitelist whitelistToCreate = new Whitelist();
        whitelistToCreate.setDiscordId(verifyUserCommand.discordId());
        whitelistToCreate.setPlayer(player);

        whitelistDAO.create(whitelistToCreate);
        return "Du wurdest erfolgreich verifiziert!";
    }
}
