package me.lucky.roleManager.events.discord.ShowDiscord;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.google.inject.Inject;
import com.google.inject.Injector;
import me.lucky.roleManager.DiscordBot;
import me.lucky.roleManager.data.dao.PlayerDAO;
import me.lucky.roleManager.data.dao.WhitelistDAO;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

import java.awt.*;

public class ShowDiscordCommandHandler implements Command.Handler<ShowDiscordCommand, Voidy> {
    @Inject
    private WhitelistDAO whitelistDAO;

    @Inject
    private PlayerDAO playerDAO;

    @Inject
    private DiscordBot discordBot;

    @Override
    public Voidy handle(ShowDiscordCommand command) {
        var player = playerDAO.findByMinecraftName(command.minecraftName());

        if (player == null) {
            command.hook().sendMessage("Der Minecraft-Spieler wurde nicht gefunden!").queue();
            return null;
        }

        var whitelist = whitelistDAO.findByPlayer(player.getId());

        if (whitelist == null) {
            command.hook().sendMessage("Der Spieler ist nicht gewhitelistet").queue();
            return null;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Spieler Information");
        embedBuilder.setColor(Color.PINK);
        embedBuilder.addField("Discord-Name", discordBot.getJda().getUserById(whitelist.getDiscordId()).getName(), false);
        embedBuilder.addField("Minecraft-Name", command.minecraftName(), false);

        command.hook().sendMessageEmbeds(embedBuilder.build()).queue();
        return null;
    }
}
