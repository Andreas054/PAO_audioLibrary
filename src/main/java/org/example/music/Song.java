package org.example.music;

public class Song {
    private String identifier;
    private String title;
    private String artist;
    private String year;

    public Song(String title, String artist, String year) {
        this.title = title;
        this.artist = artist;
        this.year = year;
    }
}
