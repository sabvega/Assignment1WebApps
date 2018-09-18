package dao;

import org.springframework.jdbc.core.JdbcTemplate;

import model.*;
import java.util.Collection;
import java.util.ArrayList;


public class TrackDAO {
    private JdbcTemplate jdbcTemplate;

    public TrackDAO(JdbcTemplate jdbcTemp) {
        this.jdbcTemplate = jdbcTemp;
    }


    public Track createTrack(Track track){
        //When user wants to add a new track to the track table
        //updating database

        this.jdbcTemplate.update("INSERT INTO tracks (id, title, album) VALUES (?, ?, ?)",
          track.getId(), track.getTitle(), track.getAlbumId()
        );
        return track;
    }

    public Track getTrack(int id){
        Track track = new Track(id);
        //retreive the track info based off of id

        this.jdbcTemplate.query( "SELECT * FROM tracks WHERE id = ?", new Object[] { id },
        (rs, rowNum) -> new Track(rs.getString("title"), rs.getInt("album"))
      ).forEach(addedTrack -> {
          track.setTitle(addedTrack.getTitle());
          track.setAlbumId(addedTrack.getAlbumId());
        }
      );
        return track;
    }

    public Collection<Track> getAllTracks(){
        Collection<Track> tracks = new ArrayList<Track>();
        //get all tracks in table

        this.jdbcTemplate.query("SELECT * FROM tracks",
        (rs, rowNum) -> new Track(rs.getInt("id"), rs.getString("title"), rs.getInt("album"))
        ).forEach(track -> tracks.add(track) );

        return tracks;
    }

    public Collection<Track> getTracksByAlbumId(int albumId){
        Collection<Track> tracks = new ArrayList<Track>();

        this.jdbcTemplate.query(
                "SELECT id, title FROM tracks WHERE album = ?", new Object[] { albumId },
                (rs, rowNum) -> new Track(rs.getInt("id"), rs.getString("title"),albumId)
        ).forEach(track -> tracks.add(track) );

        return tracks;
    }
    public Track updateTrack(Track track){
        //update information of a track decided by user

        this.jdbcTemplate.update( "UPDATE tracks SET title = ?, album = ? WHERE id = ? ",
          track.getTitle(), track.getAlbumId(), track.getId()
        );

        return track;
    }

    public boolean deleteTrack(Track track){
        boolean success = false;
        //remove this track based off id from the database

        this.jdbcTemplate.update( "DELETE FROM tracks WHERE id = ? ",
          track.getId()
        );
        success = true;
        return success;
    }

}
