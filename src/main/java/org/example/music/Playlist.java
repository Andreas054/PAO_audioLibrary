package org.example.music;

import java.util.UUID;

public class Playlist {
    private UUID uuid;
    private String name;
    private int user_id;

    public Playlist(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
    }

    public Playlist(UUID uuid, String name, int user_id) {
        this.uuid = uuid;
        this.name = name;
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public int getUserId() {
        return user_id;
    }

    public UUID getUuid() {
        return uuid;
    }
}
