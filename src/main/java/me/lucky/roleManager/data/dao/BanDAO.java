package me.lucky.roleManager.data.dao;

import me.lucky.roleManager.data.entities.Ban;
import org.hibernate.SessionFactory;

import javax.annotation.Nullable;

public class BanDAO extends AbstractDAO<Ban> {
    public BanDAO(SessionFactory sessionFactory) {
        super(sessionFactory, Ban.class);
    }

    public @Nullable Ban findByDiscordId(long discordId) {
        var session = sessionFactory.openSession();

        try {
            var query = session.createNamedQuery(Ban.FIND_BY_DISCORD_ID);
            query.setParameter("discordId", discordId);

            return (Ban) query.getSingleResultOrNull();
        } finally {
            session.close();
        }
    }

    public @Nullable Ban findByMinecraftId(String minecraftId) {
        var session = sessionFactory.openSession();

        try {
            var query = session.createNamedQuery(Ban.FIND_BY_MINECRAFT_ID);
            query.setParameter("minecraftId", minecraftId);

            return (Ban) query.getSingleResultOrNull();
        } finally {
            session.close();
        }
    }
}
