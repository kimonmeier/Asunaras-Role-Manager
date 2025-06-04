package me.lucky.roleManager.events.minecraft;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.lucky.roleManager.DiscordBot;
import me.lucky.roleManager.RoleManager;
import me.lucky.roleManager.config.PluginConfiguration;
import me.lucky.roleManager.data.dao.BanDAO;
import me.lucky.roleManager.data.dao.PlayerDAO;
import me.lucky.roleManager.data.dao.WhitelistDAO;
import me.lucky.roleManager.data.entities.Player;
import me.lucky.roleManager.data.entities.Whitelist;
import net.dv8tion.jda.api.entities.Member;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import javax.management.relation.Role;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class JoinListener implements Listener {

    @Inject
    private PlayerDAO playerDAO;

    @Inject
    private WhitelistDAO whitelistDAO;

    @Inject
    private DiscordBot discordBot;

    @Inject
    private PluginConfiguration pluginConfiguration;

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

        syncPrimaryGroup(event.getPlayer().getUniqueId(), whitelist);

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

    private void syncPrimaryGroup(UUID playerId, Whitelist whitelist) {
        Member member = discordBot.getJda().getGuildById(RoleManager.GUILD_ID.longValue()).getMemberById(whitelist.getDiscordId());


        String primaryGroup = member.getRoles().stream().map(currentRole -> pluginConfiguration.Minecraft.Rollen.getOrDefault(currentRole.getIdLong(), null)).filter(Objects::nonNull).findFirst().orElse(null);

        User luckPermsUser = LuckPermsProvider.get().getUserManager().getUser(playerId);

        if (luckPermsUser == null) {
            throw new RuntimeException("Could not find LuckPerms user for player " + playerId);
        }

        var nodes = luckPermsUser.getNodes().stream().filter(x -> x.getKey().startsWith("group.")).toList();
        for(var node : nodes) {
            luckPermsUser.data().remove(node);
        }

        luckPermsUser.data().add(Node.builder("group." + primaryGroup).build());

        LuckPermsProvider.get().getUserManager().saveUser(luckPermsUser).join();
    }
}
