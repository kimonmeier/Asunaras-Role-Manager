package me.lucky.roleManager.events.discord.PlayerWhitelisted;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.google.inject.Inject;
import me.lucky.roleManager.DiscordBot;
import me.lucky.roleManager.RoleManager;
import me.lucky.roleManager.config.PluginConfiguration;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlayerWhitelistedEventHandler implements Command.Handler<PlayerWhitelistedEvent, Voidy> {

    @Inject
    private DiscordBot discordBot;

    @Inject
    private PluginConfiguration configuration;

    @Override
    public Voidy handle(PlayerWhitelistedEvent event) {
        Guild guild = discordBot.getJda().getGuildById(configuration.Discord.GuildId);

        assert guild != null;
        Member member = guild.getMemberById(event.discordId());
        Role role = guild.getRoleById(configuration.Discord.MinecraftRoleId);

        assert member != null;
        assert role != null;

        guild.addRoleToMember(member, role).queue();
        return null;
    }
}
