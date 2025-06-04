package me.lucky.roleManager.events.discord.BanFromWhitelist;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import an.awesome.pipelinr.Voidy;
import com.google.inject.Inject;
import me.lucky.roleManager.RoleManager;
import me.lucky.roleManager.data.dao.BanDAO;
import me.lucky.roleManager.data.dao.WhitelistDAO;
import me.lucky.roleManager.data.entities.Ban;
import me.lucky.roleManager.events.discord.PlayerUnwhitelisted.PlayerUnwhitelistedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BanFromWhitelistCommandHandler implements Command.Handler<BanFromWhitelistCommand, Voidy> {

    @Inject
    private WhitelistDAO whitelistDAO;

    @Inject
    private BanDAO banDAO;

    @Inject
    private Server server;

    @Inject
    private Pipeline pipeline;

    @Override
    public Voidy handle(BanFromWhitelistCommand command) {
        var whitelist = whitelistDAO.findByDiscordId(command.discordId());

        if (whitelist == null) {
            command.hook().sendMessage("Der Spieler ist nicht gewhitelistet und kann deswegen nicht gebannt werden!").queue();
            return null;
        }

        Ban ban = new Ban();
        ban.setDiscordId(command.discordId());
        ban.setReason(command.reason());
        ban.setMinecraftUUID(whitelist.getPlayer().getMinecraftId());
        banDAO.create(ban);
        whitelistDAO.delete(whitelist);
        command.hook().sendMessage("Der Spieler wurde erfolgreich gebannt!").queue();

        pipeline.send(new PlayerUnwhitelistedEvent(command.discordId()));

        Player minecraftPlayer = server.getPlayer(UUID.fromString(whitelist.getPlayer().getMinecraftId()));
        if (minecraftPlayer != null) {
            var message = Component.text("Du wurdest vom Minecraft-Server verbannt!", NamedTextColor.RED)
                    .append(Component.newline())
                    .append(Component.newline())
                    .append(Component.text("Grund dafür ist: "))
                    .append(Component.text(command.reason(), NamedTextColor.GOLD));

            Bukkit.getScheduler().runTask(RoleManager.getInstance(), () -> minecraftPlayer.kick(message));
        }
        return null;
    }
}
