package me.lucky.roleManager.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@NamedQueries({
        @NamedQuery(name = Whitelist.FIND_BY_PLAYER, query = "SELECT w FROM Whitelist w WHERE player.id = :playerId"),
        @NamedQuery(name = Whitelist.FIND_BY_DISCORD, query = "SELECT w FROM Whitelist w WHERE discordId = :discordId")
})

@Entity
@Table(name="Whitelist")
public class Whitelist implements Serializable {

    public static final String FIND_BY_PLAYER = "Whitelist.FindByPlayer";
    public static final String FIND_BY_DISCORD= "Whitelist.FindByDiscord";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long id;

    @Getter
    @Setter
    private long discordId;

    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private Player player;
}
