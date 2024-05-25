package org.example.music;

import org.example.database.MySQLDatabase;
import org.example.exceptions.PermissionException;
import org.example.repository.SongRepository;
import org.example.user.User;
import org.example.user.UserTypeEnum;

import java.util.List;

public class SongsSearch {
    private final SongRepository songRepository;

    public SongsSearch() {
        this.songRepository = SongRepository.getInstance();
    }

    /**
     * Get the number of pages for a specific search criteria
     * @param column the column to search by (name, author)
     * @param value  the value to search for
     * @return the number of pages
     */
    public int getNrPagini(String column, String value) {
        value = "'" + value + "'";
        return songRepository.getNrPagini("songs", column, value);
    }

    /**
     * Search for songs based on a criteria
     * @param criteria the criteria to search by (name, author)
     * @param value the value to search for
     * @param paginaCurenta the current page
     * @return true if the search was successful, false otherwise
     */
    public boolean searchSongs(String criteria, String value, int paginaCurenta) {
        if (User.currentUser.getUserTypeEnum() == UserTypeEnum.ANONYMOUS) {
            throw new PermissionException("You do not have the permission to use this!");
        }

        int nrPaginiTotal = getNrPagini(criteria, value);

        if (paginaCurenta < 0 || (paginaCurenta >= nrPaginiTotal && paginaCurenta != 0)) {
            System.out.println("Invalid page number!");
            return false;
        }

        List<Song> songsByCriteria = songRepository.getSongsByCriteria(criteria, value, paginaCurenta);

        if (songsByCriteria.isEmpty()) {
            paginaCurenta = -1;
        }

        System.out.println("Page " + (paginaCurenta + 1) + " of " + nrPaginiTotal + " (" + MySQLDatabase.nrElementePagina + " items per page):");
        int i = 1;
        for (Song song : songsByCriteria) {
            System.out.println(i + ". " + song.getTitle() + " - " + song.getArtist() + " (" + song.getYear() + ") [ID: " + song.getUuid() + "]");
            i++;
        }
        return true;
    }
}
