package me.lucky.roleManager.data.dao;

import jakarta.persistence.EntityManager;
import me.lucky.roleManager.data.entities.Player;
import org.hibernate.SessionFactory;

import javax.annotation.Nullable;

public class PlayerDAO extends AbstractDAO<Player> {

    public PlayerDAO(SessionFactory sessionFactory) {
        super(sessionFactory, Player.class);
    }


    public @Nullable Player findByMinecraftId(String minecraftId) {
        var session = sessionFactory.openSession();

        try {
            var query = session.createNamedQuery(Player.FIND_BY_MINECRAFT_ID);
            query.setParameter("minecraftId", minecraftId);

            return (Player) query.getSingleResultOrNull(); } finally {
            session.close();
        }
    }

    public @Nullable Player findByMinecraftName(String minecraftName) {
        var session = sessionFactory.openSession();

        try {
            var query = session.createNamedQuery(Player.FIND_BY_MINECRAFT_NAME);
            query.setParameter("name", minecraftName);

            return (Player) query.getSingleResultOrNull(); } finally {
            session.close();
        }
    }
}
