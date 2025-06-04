package me.lucky.roleManager;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.SneakyThrows;
import me.lucky.roleManager.events.minecraft.JoinListener;
import me.lucky.roleManager.modules.BasicModule;
import me.lucky.roleManager.modules.DbModule;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.math.BigInteger;
import java.util.logging.Level;

public final class RoleManager extends JavaPlugin {
    public static final BigInteger GUILD_ID = new BigInteger("679367558809255938");

    private DiscordBot bot;

    @SneakyThrows
    @Override
    public void onEnable() {
        Injector injector = Guice.createInjector(new BasicModule(this), new DbModule(this));

        bot = injector.getInstance(DiscordBot.class);
        try {
            bot.Start();
        } catch (LoginException e) {
            this.getLogger().log(Level.SEVERE, "While starting the discord bot an exception occured", e);
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(injector.getInstance(JoinListener.class), this);

        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        bot.Stop();
    }
}
