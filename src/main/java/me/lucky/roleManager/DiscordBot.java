package me.lucky.roleManager;

import an.awesome.pipelinr.Pipeline;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import lombok.Getter;
import me.lucky.roleManager.config.PluginConfiguration;
import me.lucky.roleManager.interactions.CommandsConst;
import me.lucky.roleManager.interactions.VerifyCommandInteraction;
import me.lucky.roleManager.interactions.WhitelistCommandInteraction;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class DiscordBot extends ListenerAdapter {

    private final PluginConfiguration config;
    private final Pipeline pipeline;
    private final Injector injector;
    private final Logger logger;

    @Getter
    private JDA jda;

    @Inject
    public DiscordBot(PluginConfiguration config, Pipeline pipeline, Injector injector, Logger logger) {
        this.config = config;
        this.pipeline = pipeline;
        this.injector = injector;
        this.logger = logger;
    }

    public void Start() throws LoginException {
        logger.log(Level.INFO, "Starting Discord Bot...");

        String token = config.Discord.Token;
        if (token == null) {
            throw new LoginException("Discord token not set");
        }

        this.jda = JDABuilder
                .createLight(token, EnumSet.of(GatewayIntent.GUILD_MEMBERS))
                .setActivity(Activity.watching(config.Discord.Activity))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();

        try {
            this.jda.awaitStatus(JDA.Status.CONNECTED);
        } catch (IllegalStateException | InterruptedException e) {
            throw new LoginException("Discord token is invalid, so the plugin will not work!");
        }

        // Listener
        this.jda.addEventListener(injector.getInstance(VerifyCommandInteraction.class));
        this.jda.addEventListener(injector.getInstance(WhitelistCommandInteraction.class));


        this.jda.updateCommands()
                .addCommands(this.buildVerifyCommand())
                .addCommands(this.buildWhitelistCommand())
                .queue();

        logger.log(Level.INFO, "Discord Bot started!");
    }

    public void Stop() {
        if (this.jda == null) {
            return;
        }

        this.jda.shutdown();
        try {
            this.jda.awaitShutdown(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CommandData buildVerifyCommand() {
        return Commands
                .slash("verify", "Verifziert dich auf dem Minecraft-Server")
                .addOption(OptionType.STRING, CommandsConst.PARAMETER_MINECRAFT_NAME, "Minecraft Spieler-Name", true)
                .setContexts(InteractionContextType.GUILD);
    }

    public CommandData buildWhitelistCommand() {
        var addToWhitelist = new SubcommandData(CommandsConst.Whitelist.Add.ADD_SUBCOMMAND, "Fügt ein Spieler der Whitelist hinzu")
                .addOption(OptionType.USER, CommandsConst.PARAMETER_DISCORD_USER, "Discord-User", true)
                .addOption(OptionType.STRING, CommandsConst.PARAMETER_MINECRAFT_NAME, "Minecraft-Name", true);

        var showWhitelistGroup = this.buildWhitelistShowGroup();
        var removeFromWhitelistGroup = this.buildWhitelistRemoveGroup();

        return Commands
                .slash("whitelist", "Verwaltet die Whitelist vom Minecraft Server")
                .addSubcommandGroups(showWhitelistGroup)
                .addSubcommandGroups(removeFromWhitelistGroup)
                .addSubcommands(addToWhitelist);

    }

    private SubcommandGroupData buildWhitelistRemoveGroup() {
        var removeDiscordFromWhitelist = new SubcommandData(CommandsConst.Whitelist.DISCORD_USER_SUBCOMMAND, "Entfernt ein Spieler von der Whitelist")
                .addOption(OptionType.USER, CommandsConst.PARAMETER_DISCORD_USER, "Discord-User", true);

        var removeMinecraftFromWhitelist = new SubcommandData(CommandsConst.Whitelist.MINECRAFT_USER_SUBCOMMAND, "Entfernt ein Spieler von der Whitelist")
                .addOption(OptionType.STRING, CommandsConst.PARAMETER_MINECRAFT_NAME, "Minecraft Spielername", true);

        return new SubcommandGroupData(CommandsConst.Whitelist.Remove.REMOVE_GROUP_NAME, "Entfernt ein Spieler von der Whitelist")
                .addSubcommands(removeDiscordFromWhitelist)
                .addSubcommands(removeMinecraftFromWhitelist);
    }

    private SubcommandGroupData buildWhitelistShowGroup() {
        var showDiscordWhitelist = new SubcommandData(CommandsConst.Whitelist.DISCORD_USER_SUBCOMMAND, "Zeigt den Minecraft-Namen eines Benuters an")
                .addOption(OptionType.USER, CommandsConst.PARAMETER_DISCORD_USER, "Discord-User", true);
        var showMinecraftWhitelist = new SubcommandData(CommandsConst.Whitelist.MINECRAFT_USER_SUBCOMMAND, "Zeigt den Discord-Namen eines Benuters an")
                .addOption(OptionType.STRING, CommandsConst.PARAMETER_MINECRAFT_NAME, "Minecraft Spielername", true);

        return new SubcommandGroupData(CommandsConst.Whitelist.Show.SHOW_GROUP_NAME, "Zeigt die Information zu einem Benutzer an")
                .addSubcommands(showDiscordWhitelist)
                .addSubcommands(showMinecraftWhitelist);
    }
}
