package me.lucky.roleManager.events.discord.PlayerUnwhitelisted;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.google.inject.Inject;
import me.lucky.roleManager.DiscordBot;
import me.lucky.roleManager.RoleManager;
import me.lucky.roleManager.config.PluginConfiguration;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class PlayerUnwhitelistedEventHandler implements Command.Handler<PlayerUnwhitelistedEvent, Voidy> {
    @Inject
    private DiscordBot discordBot;

    @Inject
    private PluginConfiguration config;

    @Override
    public Voidy handle(PlayerUnwhitelistedEvent event) {
        Guild guild = discordBot.getJda().getGuildById(config.Discord.GuildId);

        assert guild != null;
        Member member = guild.getMemberById(event.discordId());
        Role role = guild.getRoleById(config.Discord.MinecraftRoleId);

        assert member != null;
        assert role != null;

        guild.removeRoleFromMember(member, role).queue();
        return null;

    }
}
