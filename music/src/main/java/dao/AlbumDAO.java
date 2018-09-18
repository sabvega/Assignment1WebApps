package dao;

import org.springframework.jdbc.core.JdbcTemplate;

import model.*;
import java.util.Collection;
import java.util.ArrayList;

public class AlbumDAO {
    private JdbcTemplate jdbcTemplate;

    public AlbumDAO(JdbcTemplate jdbcTemp) {
        this.jdbcTemplate = jdbcTemp;
    }

    public Album createAlbum(Album album){
       //when user wants to we add album and update the table

       this.jdbcTemplate.update(
           "INSERT INTO albums (id, title) VALUES (?,?)", album.getId(), album.getTitle()
       );
        return album;
    }

    public Album getAlbum(int id){
        Album album = new Album(id, "");
        //Part of the insert/update function for albums

        //Get album and set tracks using getTracksByAlbumId(id) in TracksDAO
        TrackDAO tracksDAOtemp = new TrackDAO(jdbcTemplate);

        this.jdbcTemplate.query(
                "SELECT * FROM albums", new Object[] { },
                (rs, rowNum) -> new Album(rs.getInt("id"), rs.getString("title"))
        ).forEach(addedAlbum -> {
            album.setTracks(tracksDAOtemp.getTracksByAlbumId(id));
            album.setTitle(addedAlbum.getTitle());
        });
        return album;
    }

    public Collection<Album> getAllAlbums(){
        Collection<Album> albums = new ArrayList<Album>();
        this.jdbcTemplate.query(
                "SELECT * FROM albums", new Object[] { },
                (rs, rowNum) -> new Album(rs.getInt("id"), rs.getString("title"))
        ).forEach(album -> albums.add(album));

        return albums;
    }

    public Album updateAlbum(Album album){
        //changes information in the album depending on what needs to be updated
          this.jdbcTemplate.update("UPDATE albums SET title = ? WHERE id = ? ",
          album.getTitle(), album.getId()
        );
        return album;
    }

    public boolean deleteAlbum(Album album){
        boolean success = false;
        //deletes the album specified by id and notifies us by returning 
        //a boolean

        this.jdbcTemplate.update("DELETE FROM albums WHERE id = ? ",
          album.getId()
        );
        success = true;
        return success;
    }



}
