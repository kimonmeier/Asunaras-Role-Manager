package me.lucky.roleManager.events.discord.ShowMinecraft;

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

public class ShowMinecraftCommandHandler implements Command.Handler<ShowMinecraftCommand, Voidy> {
    @Inject
    private WhitelistDAO whitelistDAO;

    @Inject
    private DiscordBot discordBot;

    @Override
    public Voidy handle(ShowMinecraftCommand command) {
        var whitelist = whitelistDAO.findByDiscordId(command.discordUserId());

        if (whitelist == null) {
            command.hook().sendMessage("Der Benutzer ist nicht gewhitelistet").queue();
            return null;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Spieler Information");
        embedBuilder.setColor(Color.PINK);
        embedBuilder.addField("Discord-Name", discordBot.getJda().getUserById(whitelist.getDiscordId()).getName(), false);
        embedBuilder.addField("Minecraft-Name", whitelist.getPlayer().getName(), false);

        command.hook().sendMessageEmbeds(embedBuilder.build()).queue();
        return null;
    }
}
