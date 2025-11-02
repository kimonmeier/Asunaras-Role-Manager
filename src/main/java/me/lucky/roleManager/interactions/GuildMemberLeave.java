package me.lucky.roleManager.interactions;

import com.google.inject.Inject;
import me.lucky.roleManager.RoleManager;
import me.lucky.roleManager.config.PluginConfiguration;
import me.lucky.roleManager.data.dao.WhitelistDAO;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.UUID;

public class GuildMemberLeave extends ListenerAdapter {

    @Inject
    private WhitelistDAO whitelistDAO;

    @Inject
    private Server server;

    @Inject
    private PluginConfiguration pluginConfiguration;

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        if (!BigInteger.valueOf(event.getGuild().getIdLong()).equals(pluginConfiguration.Discord.GuildId)) {
            return;
        }

        var whitelist = whitelistDAO.findByDiscordId(event.getMember().getUser().getIdLong());
        if (whitelist == null) {
            return;
        }

        Player minecraftPlayer = server.getPlayer(UUID.fromString(whitelist.getPlayer().getMinecraftId()));
        if (minecraftPlayer != null) {
            Bukkit.getScheduler().runTask(RoleManager.getInstance(), () -> minecraftPlayer.kick(Component.text("Du bist nicht mehr auf dem Asunara Discord und damit nicht mehr gewhitelistet", NamedTextColor.RED)));
        }

        whitelistDAO.delete(whitelist);
    }
}
