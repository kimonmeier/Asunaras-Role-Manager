package me.lucky.roleManager.events.minecraft;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.lucky.roleManager.data.dao.BanDAO;
import me.lucky.roleManager.data.dao.PlayerDAO;
import me.lucky.roleManager.data.dao.WhitelistDAO;
import me.lucky.roleManager.data.entities.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class JoinListener implements Listener {

    @Inject
    private PlayerDAO playerDAO;

    @Inject
    private WhitelistDAO whitelistDAO;

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        var playerId = event.getPlayer().getUniqueId().toString();
        Player player = playerDAO.findByMinecraftId(playerId);
        if (player == null) {
            var playerToCreate = new Player();
            playerToCreate.setMinecraftId(playerId);
            playerToCreate.setName(event.getPlayer().getName());

            playerDAO.create(playerToCreate);

            event.kickMessage(getWhitelistMessage(event.getPlayer()));
            event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
            return;
        }

        player.setName(event.getPlayer().getName());

        playerDAO.update(player);

        var whitelist = whitelistDAO.findByPlayer(player.getId());

        if (whitelist == null) {
            event.kickMessage(getWhitelistMessage(event.getPlayer()));
            event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
            return;
        }

        event.setResult(PlayerLoginEvent.Result.ALLOWED);
    }

    private Component getWhitelistMessage(org.bukkit.entity.Player player) {
        return Component.text("Leider ist dein Minecraft-Account")
                .append(Component.text(" noch nicht ", NamedTextColor.RED))
                .append(Component.text("mit"))
                .append(Component.text(" Asunaras Cafe ", NamedTextColor.LIGHT_PURPLE))
                .append(Component.text("verbunden!"))
                .append(Component.newline())
                .append(Component.newline())
                .append(Component.text("Du kannst dich verifizieren in dem du auf dem Server "))
                .append(Component.newline())
                .append(Component.text("/verify " + player.getName(), NamedTextColor.AQUA))
                .append(Component.newline())
                .append(Component.text("eingibst"));

    }
}
