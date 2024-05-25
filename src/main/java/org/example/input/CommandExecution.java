package org.example.input;

import org.example.audit.AuditManager;
import org.example.authentication.Session;
import org.example.music.PlaylistManager;
import org.example.music.SongsManager;
import org.example.music.SongsSearch;
import org.example.user.User;

import java.util.List;

public class CommandExecution {
    private static CommandExecution instance = null;
    private final Session session;
    private final SongsManager songsManager;
    private final PlaylistManager playlistManager;
    private final SongsSearch songsSearch;
    private final AuditManager auditManager;
    private final PlaylistExport playlistExport;
    private final PlaylistImport playlistImport;

    private CommandExecution() {
        this.session = new Session();
        this.songsManager = SongsManager.getInstance();
        this.playlistManager = new PlaylistManager();
        this.songsSearch = new SongsSearch();
        this.auditManager = new AuditManager();
        this.playlistExport = new PlaylistExport();
        this.playlistImport = new PlaylistImport();
    }

    public static CommandExecution getInstance() {
        if (instance == null) {
            instance = new CommandExecution();
        }
        return instance;
    }

    public boolean insertIntoAudit(String command, boolean rulatOk) {
        return auditManager.insertIntoAudit(command, rulatOk);
    }

    public boolean getAuditForUser(String userName, int paginaCurenta) {
        User user = session.getUserByName(userName);
        return auditManager.getAuditsForUser(user, paginaCurenta);
    }

    public boolean reReRunAudit(int id) {
        return auditManager.reRunAuditCommand(id);
    }

    public boolean userRegister(String username, String password) {
        return session.register(username, password);
    }

    public boolean userLogin(String username, String password) {
        return session.login(username, password);
    }

    public boolean userLogout() {
        return session.logout();
    }

    public boolean userPromote(String username) {
        return session.promote(username);
    }

    public boolean createSong(String title, String artist, String year) {
        return songsManager.createSong(title, artist, year);
    }

    public boolean addSongToPlaylistById(String playlistUuid, List<String> songUuidList) {
        return songsManager.addSongToPlaylistById(playlistUuid, songUuidList);
    }

    public boolean addSongToPlaylistByName(String playlistName, List<String> songUuidList) {
        return songsManager.addSongToPlaylistByName(playlistName, songUuidList);
    }

    public boolean searchSongs(String criteria, String value, int nrPagina) {
        return songsSearch.searchSongs(criteria, value, nrPagina);
    }

    public boolean createPlaylist(String name) {
        return playlistManager.createPlaylist(name);
    }

    public boolean getPlaylists(int nrPagina) {
        return playlistManager.getPlaylistsForCurrentUser(nrPagina);
    }

    public boolean exportPlaylistById(String playlistUuid, String format) {
        return playlistExport.exportPlaylistById(playlistUuid, format);
    }

    public boolean exportPlaylistByName(String playlistName, String format) {
        return playlistExport.exportPlaylistByName(playlistName, format);
    }

    public boolean importPlaylistById(String playlistUuid, String format, String pathToFile) {
        return playlistImport.importPlaylistById(playlistUuid, format, pathToFile);
    }

    public boolean importPlaylistByName(String playlistName, String format, String pathToFile) {
        return playlistImport.importPlaylistByName(playlistName, format, pathToFile);
    }
}
