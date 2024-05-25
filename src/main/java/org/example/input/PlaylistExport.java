package org.example.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exceptions.PermissionException;
import org.example.music.Playlist;
import org.example.music.Song;
import org.example.repository.PlaylistRepository;
import org.example.repository.PlaylistsSongsRepository;
import org.example.repository.SongRepository;
import org.example.user.User;
import org.example.user.UserTypeEnum;

import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PlaylistExport {
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistsSongsRepository playlistsSongsRepository;

    private Playlist currentPlaylist;

    public PlaylistExport() {
        this.songRepository = SongRepository.getInstance();
        this.playlistRepository = PlaylistRepository.getInstance();
        this.playlistsSongsRepository = PlaylistsSongsRepository.getInstance();
    }

    /**
     * Get all songs inside a playlist
     * @param playlistUuid - the playlist id
     * @return a list of songs
     */
    public List<Song> getSongsForPlaylist(String playlistUuid) {
        if (User.currentUser.getUserTypeEnum() == UserTypeEnum.ANONYMOUS) {
            throw new PermissionException("You do not have the permission to use this!");
        }

        currentPlaylist = playlistRepository.getPlaylistById(playlistUuid);
        if (currentPlaylist == null) {
            System.out.println("Playlist " + playlistUuid + " does not exist.");
            return null;
        } else if (currentPlaylist.getUserId() != User.currentUser.getId()) {
            throw new PermissionException("You do not have the permission to use this!");
        }

        List<Song> songsToExport = new LinkedList<>();
        List<String> songUuids = playlistsSongsRepository.getSongsForPlaylist(playlistUuid);
        for (String songUuid : songUuids) {
            Song song = songRepository.getSongById(songUuid);
            songsToExport.add(song);
        }

        return songsToExport;
    }

    /**
     * Export a playlist by id
     * @param playlistUuid - the playlist id
     * @param format - the format of the export (json, csv, txt)
     * @return true if the export was successful, false otherwise
     */
    public boolean exportPlaylistById(String playlistUuid, String format) {
        List<Song> songsToExport = this.getSongsForPlaylist(playlistUuid);
        if (songsToExport == null) {
            return false;
        } else if (songsToExport.isEmpty()) {
            System.out.println("Playlist is empty.");
            return false;
        }

        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        if (format.equalsIgnoreCase("json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            try (FileWriter fileWriter = new FileWriter(String.format("src/main/java/org/example/ioPlaylists/export_%s_%s_%s.json", User.currentUser.getName(), currentPlaylist.getName(), currentDate))) {
                objectMapper.writeValue(fileWriter, songsToExport);
                System.out.println("Export successful.");
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } else if (format.equalsIgnoreCase("csv")) {
            try (FileWriter fileWriter = new FileWriter(String.format("src/main/java/org/example/ioPlaylists/export_%s_%s_%s.csv", User.currentUser.getName(), currentPlaylist.getName(), currentDate))) {
                for (Song song : songsToExport) {
                    fileWriter.write(song.getUuid().toString() + "," + song.getTitle() + "," + song.getArtist() + "," + song.getYear() + "\n");
                }
                System.out.println("Export successful.");
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else if (format.equalsIgnoreCase("txt")) {
            try (FileWriter fileWriter = new FileWriter(String.format("src/main/java/org/example/ioPlaylists/export_%s_%s_%s.txt", User.currentUser.getName(), currentPlaylist.getName(), currentDate))) {
                for (Song song : songsToExport) {
                    fileWriter.write(song.getUuid().toString() + "####" + song.getTitle() + "####" + song.getArtist() + "####" + song.getYear() + "\n");
                }
                System.out.println("Export successful.");
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Export failed");
        return false;
    }

    /**
     * Export a playlist by name
     * @param playlistName - the playlist name
     * @param format - the format of the export (json, csv, txt)
     * @return true if the export was successful, false otherwise
     */
    public boolean exportPlaylistByName(String playlistName, String format) {
        Playlist playlistCurrent = playlistRepository.getPlaylistByName(playlistName, User.currentUser);
        if (playlistCurrent == null) {
            System.out.println("Playlist " + playlistName + " does not exist.");
            return false;
        }
        String playlistUuid = playlistCurrent.getUuid().toString();
        return this.exportPlaylistById(playlistUuid, format);
    }
}
