package org.example.input;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exceptions.PermissionException;
import org.example.music.Playlist;
import org.example.music.Song;
import org.example.music.SongsManager;
import org.example.repository.PlaylistRepository;
import org.example.user.User;
import org.example.user.UserTypeEnum;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class PlaylistImport {
    private final PlaylistRepository playlistRepository;

    private final SongsManager songsManager;

    public PlaylistImport() {
        this.playlistRepository = PlaylistRepository.getInstance();

        this.songsManager = SongsManager.getInstance();
    }

    /**
     * Imports a playlist from a file.
     * @param playlistUuid The UUID of the playlist to import to.
     * @param format The format of the file (csv, json, txt).
     * @param pathToFile The path to the file.
     * @return True if the import was successful, false otherwise.
     */
    public boolean importPlaylistById(String playlistUuid, String format, String pathToFile) {
        if (User.currentUser.getUserTypeEnum() == UserTypeEnum.ANONYMOUS) {
            throw new PermissionException("You do not have the permission to use this!");
        }

        Playlist currentPlaylist = playlistRepository.getPlaylistById(playlistUuid);
        if (currentPlaylist == null) {
            System.out.println("Playlist " + playlistUuid + " does not exist.");
            return false;
        } else if (currentPlaylist.getUserId() != User.currentUser.getId()) {
            throw new PermissionException("You do not have the permission to use this!");
        }

        List<String> songUuidList = new LinkedList<>();

        if (format.equalsIgnoreCase("csv")) {
            try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    songUuidList.add(values[0]);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return false;
            }
        } else if (format.equalsIgnoreCase("json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<Song> songs = objectMapper.readValue(new File(pathToFile), new TypeReference<List<Song>>(){});
                System.out.println(songs);

                for (Song song : songs) {
                    songUuidList.add(song.getUuid().toString());
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
                return false;
            }
        } else if (format.equalsIgnoreCase("txt")) {
            try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split("####");
                    songUuidList.add(values[0]);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        if (songUuidList.isEmpty()) {
            System.out.println("No songs to import.");
            return false;
        }

        return songsManager.addSongToPlaylistById(playlistUuid, songUuidList);
    }


    /**
     * Imports a playlist from a file.
     * @param playlistName The name of the playlist to import to.
     * @param format The format of the file (csv, json, txt).
     * @param pathToFile The path to the file.
     * @return True if the import was successful, false otherwise.
     */
    public boolean importPlaylistByName(String playlistName, String format, String pathToFile) {
        Playlist playlistCurrent = playlistRepository.getPlaylistByName(playlistName, User.currentUser);
        if (playlistCurrent == null) {
            System.out.println("The desired playlist does not exist.");
            return false;
        }
        String playlistUuid = playlistCurrent.getUuid().toString();
        return this.importPlaylistById(playlistUuid, format, pathToFile);
    }
}
