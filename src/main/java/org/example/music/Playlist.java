package org.example.music;

import java.util.UUID;

public class Playlist {
    private UUID UUID;
    private String name;

    public Playlist(String name) {
        this.UUID = UUID.randomUUID();
        this.name = name;
    }
}
