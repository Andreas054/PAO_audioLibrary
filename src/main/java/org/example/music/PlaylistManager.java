package org.example.music;

import org.example.database.MySQLDatabase;
import org.example.exceptions.PermissionException;
import org.example.repository.PlaylistRepository;
import org.example.user.User;
import org.example.user.UserTypeEnum;

import java.util.List;

public class PlaylistManager {
    private PlaylistRepository playlistRepository;

    public PlaylistManager() {
        this.playlistRepository = PlaylistRepository.getInstance();
    }

    public int getNrPagini() {
        return playlistRepository.getNrPagini("playlists", "user_id", User.currentUser.getId());
    }

    public boolean createPlaylist(String playlistName) {
        if (User.currentUser.getUserTypeEnum() == UserTypeEnum.ANONYMOUS) {
            throw new PermissionException("You do not have the permission to use this!");
        }

        int nrPagini = getNrPagini();
        for (int i = 0; i < nrPagini; i++) {
            List<Playlist> playlists = playlistRepository.getAllPlaylistsForUser(User.currentUser, i);
            for (Playlist playlist : playlists) {
                if (playlist.getName().equals(playlistName)) {
                    System.out.println("You already have a playlist named " + playlistName);
                    return false;
                }
            }
        }

        Playlist playlistToAdd = new Playlist(playlistName);
        playlistRepository.addPlaylist(playlistToAdd);
        System.out.println("Playlist " + playlistName + " was created successfully!");
        return true;
    }

    public boolean getPlaylistsForCurrentUser(int paginaCurenta) {
        if (User.currentUser.getUserTypeEnum() == UserTypeEnum.ANONYMOUS) {
            throw new PermissionException("You do not have the permission to use this!");
        }

        int nrPaginiTotal = getNrPagini();

        if (paginaCurenta < 0 || (paginaCurenta >= nrPaginiTotal && paginaCurenta != 0)) {
            System.out.println("Invalid page number!");
            return false;
        }

        List<Playlist> playlists = playlistRepository.getAllPlaylistsForUser(User.currentUser, paginaCurenta);

        if (playlists.isEmpty()) {
            paginaCurenta = -1;
        }

        System.out.println("Page " + (paginaCurenta + 1) + " of " + nrPaginiTotal + " (" + MySQLDatabase.nrElementePagina + " items per page):");
        int i = 1;
        for (Playlist playlist : playlists) {
            System.out.println(i + ". " + playlist.getName() + " [ID: " + playlist.getUuid() + "]");
            i ++;
        }
        return true;
    }
}
