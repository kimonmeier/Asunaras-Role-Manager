package me.lucky.roleManager.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@NamedQueries({
        @NamedQuery(name = Ban.FIND_BY_DISCORD_ID, query = "SELECT b FROM Ban b WHERE discordId = :discordId"),
        @NamedQuery(name = Ban.FIND_BY_MINECRAFT_ID, query = "SELECT b FROM Ban b WHERE LOWER(minecraftUUID) = LOWER(:minecraftId)")
})

@Entity
@Table(name = "Ban")
public class Ban implements Serializable {

    public static final String FIND_BY_DISCORD_ID = "Ban.findByDiscordId";
    public static final String FIND_BY_MINECRAFT_ID = "Ban.findByMinecraftId";

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Getter
    @Setter
    private long discordId;

    @Getter
    @Setter
    private String minecraftUUID;

    @Getter
    @Setter
    private String reason;
}
