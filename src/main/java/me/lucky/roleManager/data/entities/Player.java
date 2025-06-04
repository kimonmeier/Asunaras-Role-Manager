package me.lucky.roleManager.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@NamedQueries({
        @NamedQuery(name = Player.FIND_BY_MINECRAFT_ID, query = "SELECT p FROM Player p WHERE minecraftId = :minecraftId"),
        @NamedQuery(name = Player.FIND_BY_MINECRAFT_NAME, query = "SELECT p FROM Player p WHERE LOWER(name) = LOWER(:name)")
})

@Entity
@Table(name = "Player")
public class Player implements Serializable {

    public static final String FIND_BY_MINECRAFT_ID = "PlayerDAO.FindByMinecraftId";
    public static final String FIND_BY_MINECRAFT_NAME = "PlayerDAO.FindByMinecraftName";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String minecraftId;
}
