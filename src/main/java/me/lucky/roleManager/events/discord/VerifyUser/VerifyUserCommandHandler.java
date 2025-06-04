package me.lucky.roleManager.events.discord.VerifyUser;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.google.inject.Inject;
import me.lucky.roleManager.data.dao.BanDAO;
import me.lucky.roleManager.data.dao.PlayerDAO;
import me.lucky.roleManager.data.dao.WhitelistDAO;
import me.lucky.roleManager.data.entities.Whitelist;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class VerifyUserCommandHandler implements Command.Handler<VerifyUserCommand, Voidy> {

    @Inject
    private PlayerDAO playerDAO;

    @Inject
    private WhitelistDAO whitelistDAO;

    @Inject
    private BanDAO banDAO;

    @Override
    public Voidy handle(VerifyUserCommand command) {
        var whitelist = whitelistDAO.findByDiscordId(command.discordId());

        if (whitelist != null) {
            command.hook().sendMessage("Du bist bereits mit einem Benutzer verifiziert. Bitte wende dich an den Support!").queue();
            return null;
        }

        var player = playerDAO.findByMinecraftName(command.minecraftName());

        if (player == null) {
            command.hook().sendMessage("Es wurde kein Spieler mit diesem Namen gefunden! Stell sicher, dass du mindestens einmal auf den Server gejoint bist!").queue();
            return null;
        }

        var ban = banDAO.findByDiscordId(command.discordId());

        if (ban == null) {
            ban = banDAO.findByMinecraftId(player.getMinecraftId());
        }

        if (ban != null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Verbannt");
            embedBuilder.setColor(Color.RED);
            embedBuilder.setDescription("Du kannst dich nicht mehr verifizieren, da du vom Minecraft-Server verbannt wurdest!");
            embedBuilder.addField("Grund", ban.getReason(), false);
            command.hook().sendMessageEmbeds(embedBuilder.build()).queue();
            return null;
        }

        Whitelist whitelistToCreate = new Whitelist();
        whitelistToCreate.setDiscordId(command.discordId());
        whitelistToCreate.setPlayer(player);

        whitelistDAO.create(whitelistToCreate);
        command.hook().sendMessage("Du wurdest erfolgreich verifiziert!").queue();
        return null;
    }
}
