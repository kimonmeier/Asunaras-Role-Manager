package me.lucky.roleManager.modules;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import de.exlll.configlib.YamlConfigurations;
import me.lucky.roleManager.DiscordBot;
import me.lucky.roleManager.config.PluginConfiguration;
import me.lucky.roleManager.events.discord.AddWhitelist.AddWhitelistCommandHandler;
import me.lucky.roleManager.events.discord.RemoveWhitelistDiscord.RemoveWhitelistDiscordCommandHandler;
import me.lucky.roleManager.events.discord.RemoveWhitelistMinecraft.RemoveWhitelistMinecraftCommandHandler;
import me.lucky.roleManager.events.discord.ShowDiscord.ShowDiscordCommandHandler;
import me.lucky.roleManager.events.discord.ShowMinecraft.ShowMinecraftCommandHandler;
import me.lucky.roleManager.events.discord.VerifyUser.VerifyUserCommandHandler;
import me.lucky.roleManager.events.minecraft.JoinListener;
import me.lucky.roleManager.interactions.VerifyCommandInteraction;
import me.lucky.roleManager.interactions.WhitelistCommandInteraction;
import net.dv8tion.jda.api.JDA;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class BasicModule extends AbstractModule {
    private final JavaPlugin plugin;

    public BasicModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    protected void configure() {
        super.configure();

        // Minecraft
        bind(JoinListener.class);

        // Discord
        bind(VerifyCommandInteraction.class);
        bind(WhitelistCommandInteraction.class);


        bind(Server.class).toInstance(this.plugin.getServer());
        bind(PluginConfiguration.class).toInstance(this.createYamlConfigurations());
    }

    @Provides
    private Pipeline createPipeline(Injector injector) {
        return new Pipelinr()
                .with(() -> Stream.of(
                        injector.getInstance(VerifyUserCommandHandler.class),
                        injector.getInstance(AddWhitelistCommandHandler.class),
                        injector.getInstance(RemoveWhitelistDiscordCommandHandler.class),
                        injector.getInstance(RemoveWhitelistMinecraftCommandHandler.class),
                        injector.getInstance(ShowDiscordCommandHandler.class),
                        injector.getInstance(ShowMinecraftCommandHandler.class)
                ));
    }

    private PluginConfiguration createYamlConfigurations() {
        var configFile = Paths.get(plugin.getDataFolder().getAbsolutePath(), "config.yml");

        if (!Files.exists(configFile)) {
            YamlConfigurations.save(configFile, PluginConfiguration.class, new PluginConfiguration());
        }

        return YamlConfigurations.load(configFile, PluginConfiguration.class);
    }
}
