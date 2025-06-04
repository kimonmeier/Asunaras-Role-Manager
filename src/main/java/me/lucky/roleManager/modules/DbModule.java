package me.lucky.roleManager.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import jakarta.persistence.Transient;
import me.lucky.roleManager.data.dao.PlayerDAO;
import me.lucky.roleManager.data.dao.WhitelistDAO;
import me.lucky.roleManager.data.entities.Player;
import me.lucky.roleManager.data.entities.Whitelist;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.nio.file.Paths;

public class DbModule extends AbstractModule {

    private final JavaPlugin plugin;

    public DbModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void configure() {
    }

    @Provides
    @Singleton
    public SessionFactory provideSessionFactory() {
        try {
            // Load the configuration and build the SessionFactory
            Configuration configuration = new Configuration();

            configuration.setProperty("hibernate.connection.url", "jdbc:sqlite:" + Paths.get(plugin.getDataFolder().getAbsolutePath(), "database.db"));
            configuration.setProperty("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
            configuration.setProperty("hibernate.connection.driver_clas", "org.sqlite.JDBC");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");

            configuration.addAnnotatedClass(Player.class);
            configuration.addAnnotatedClass(Whitelist.class);

            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new RuntimeException(ex);
        }
    }

    @Provides
    @Transient
    public PlayerDAO providePlayer(SessionFactory sessionFactory) {
        return new PlayerDAO(sessionFactory);
    }

    @Provides
    @Transient
    public WhitelistDAO provideWhitelistDAO(SessionFactory sessionFactory) {
        return new WhitelistDAO(sessionFactory);
    }
}
