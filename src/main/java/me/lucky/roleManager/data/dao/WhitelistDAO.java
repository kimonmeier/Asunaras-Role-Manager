package me.lucky.roleManager.data.dao;

import jakarta.persistence.EntityManager;
import me.lucky.roleManager.data.entities.Whitelist;
import org.hibernate.SessionFactory;

import javax.annotation.Nullable;

public class WhitelistDAO extends AbstractDAO<Whitelist> {

    public WhitelistDAO(SessionFactory sessionFactory) {
        super(sessionFactory, Whitelist.class);
    }

    public @Nullable Whitelist findByPlayer(long playerId) {
        var session = sessionFactory.openSession();

        try {
            var query = session.createNamedQuery(Whitelist.FIND_BY_PLAYER);
            query.setParameter("playerId", playerId);

            return (Whitelist) query.getSingleResultOrNull();
        } finally {
            session.close();
        }
    }

    public @Nullable Whitelist findByDiscordId(long discordId) {
        var session = sessionFactory.openSession();

        try {
            var query = session.createNamedQuery(Whitelist.FIND_BY_DISCORD);
            query.setParameter("discordId", discordId);

            return (Whitelist) query.getSingleResultOrNull();
        } finally {
            session.close();
        }
    }
}
