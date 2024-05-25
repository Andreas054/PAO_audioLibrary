package org.example.music;

import org.example.exceptions.PermissionException;
import org.example.repository.PlaylistRepository;
import org.example.repository.PlaylistsSongsRepository;
import org.example.repository.SongRepository;
import org.example.user.User;
import org.example.user.UserTypeEnum;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SongsManager {
    private static SongsManager instance = null;

    private SongRepository songRepository;
    private PlaylistRepository playlistRepository;
    private PlaylistsSongsRepository playlistsSongsRepository;

    private SongsManager() {
        this.songRepository = SongRepository.getInstance();
        this.playlistRepository = PlaylistRepository.getInstance();
        this.playlistsSongsRepository = PlaylistsSongsRepository.getInstance();
    }

    public static SongsManager getInstance() {
        if (instance == null) {
            instance = new SongsManager();
        }
        return instance;
    }

    public boolean createSong(String title, String artist, String year) {
        if (User.currentUser.getUserTypeEnum() != UserTypeEnum.ADMIN) {
            throw new PermissionException("You do not have the permission to use this!");
        }

        for (Song song : songRepository.getAllSongs()) {
            if (song.getTitle().equals(title) && song.getArtist().equals(artist)) {
                System.out.println("This song is already part of the library!");
                return false;
            }
        }

        Song songToAdd = new Song(title, artist, year);
        songRepository.createSong(songToAdd);
        System.out.printf("Added %s by %s to the library.%n%n", title, artist);
        return true;
    }

    public boolean addSongToPlaylistById(String playlistUuid, List<String> songUuidListToAdd) {
        if (User.currentUser.getUserTypeEnum() == UserTypeEnum.ANONYMOUS) {
            throw new PermissionException("You do not have the permission to use this!");
        }

        Playlist currentPlaylist = playlistRepository.getPlaylistById(playlistUuid);
        if (currentPlaylist == null) {
            System.out.println("The desired playlist does not exist.");
            return false;
        } else if (currentPlaylist.getUserId() != User.currentUser.getId()) {
            throw new PermissionException("You do not have the permission to use this!");
        }

        Map<Song, String> songsToAdd = new LinkedHashMap<>();
        boolean songsOverlap = false;
        List<String> songsUuidForPlaylist = playlistsSongsRepository.getSongsForPlaylist(playlistUuid);

        for (String songUuidToAdd : songUuidListToAdd) {
            Song songToAdd = songRepository.getSongById(songUuidToAdd);
            if (songToAdd == null) {
                System.out.printf("Song with identifier %s does not exist.%n", songUuidToAdd);
                continue;
            } else if (songsUuidForPlaylist.contains(songUuidToAdd)) {
                System.out.printf("The song %s by %s is already part of %s%n", songToAdd.getTitle(), songToAdd.getArtist(), currentPlaylist.getName());
                songsOverlap = true;
                continue;
            }

            songsToAdd.put(songToAdd, playlistUuid);
        }

        if (songsOverlap || songsToAdd.isEmpty()) {
            return false;
        } else {
            for (Map.Entry<Song, String> entry : songsToAdd.entrySet()) {
                playlistsSongsRepository.addSongToPlaylist(entry.getKey().getUuid().toString(), entry.getValue());
                System.out.printf("Added %s by %s to %s%n", entry.getKey().getTitle(), entry.getKey().getArtist(), currentPlaylist.getName());
            }
        }
        System.out.println("All songs added successfully.");
        return true;
    }

    public boolean addSongToPlaylistByName(String playlistName, List<String> songUuidList) {
        Playlist playlistCurrent = playlistRepository.getPlaylistByName(playlistName, User.currentUser);
        if (playlistCurrent == null) {
            System.out.println("The desired playlist does not exist.");
            return false;
        }
        return this.addSongToPlaylistById(playlistCurrent.getUuid().toString(), songUuidList);
    }
}
