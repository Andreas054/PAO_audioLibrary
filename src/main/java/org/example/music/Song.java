package org.example.music;

import java.util.UUID;

public class Song {
    private UUID uuid;
    private String title;
    private String artist;
    private String year;

    public Song() {
    }

    public Song(String title, String artist, String year) {
        this.uuid = UUID.randomUUID();
        this.title = title;
        this.artist = artist;
        this.year = year;
    }

    public Song(UUID uuid, String title, String artist, String year) {
        this.uuid = uuid;
        this.title = title;
        this.artist = artist;
        this.year = year;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getYear() {
        return year;
    }
}
