package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import dao.*;
import model.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {
        AlbumDAO albumDAO = new AlbumDAO(jdbcTemplate);
        TrackDAO trackDAO = new TrackDAO(jdbcTemplate);

        log.info("Creating tables");


        jdbcTemplate.execute("DROP TABLE IF EXISTS tracks");
        jdbcTemplate.execute("CREATE TABLE tracks(" +
                "id SERIAL, title VARCHAR(255), album INT)");
        trackDAO.createTrack(new Track("Track 1", 42));
        trackDAO.createTrack(new Track("Track 2", 42));
        trackDAO.createTrack(new Track ("Track 3", 32));

        log.info("*** TEST: trackDAO.getTrack(1) ***");
        log.info(trackDAO.getTrack(1).toString());

        log.info("*** TEST: trackDAO.getAllTracks() ***");
        trackDAO.getAllTracks().forEach(track -> log.info(track.toString()));

        log.info("*** TEST: trackDAO.updateTrack(3) ***");
        Track trackToUpdate = trackDAO.getTrack(3);
        trackToUpdate.setTitle("Track 3 New");
        trackDAO.updateTrack(trackToUpdate);
        log.info(trackDAO.getTrack(3).toString());

        log.info("*** TEST: trackDAO.deleteTrack(2) ***");
        Track tracktoDelete = trackDAO.getTrack(2);
        trackDAO.deleteTrack(tracktoDelete);
        trackDAO.getAllTracks().forEach(track -> log.info(track.toString()));


        jdbcTemplate.execute("DROP TABLE IF EXISTS albums");
        jdbcTemplate.execute("CREATE TABLE albums(" +
                "id INT, title VARCHAR(255))");

        albumDAO.createAlbum(new Album (42, "Album 1"));
        albumDAO.createAlbum(new Album (32, "Album 2"));

        log.info("*** TEST: albumDAO.getAllAlbums() ***");
        albumDAO.getAllAlbums().forEach(album -> log.info(album.toString()));

        log.info("*** TEST: albumDAO.updateAlbum(32) ***");
        Album albumToUpdate = albumDAO.getAlbum(32);
        log.info(albumToUpdate.toString());
        albumToUpdate.setTitle("Album 2 New");
        albumDAO.updateAlbum(albumToUpdate);
        log.info(albumDAO.getAlbum(32).toString());

        log.info("*** TEST: albumDAO.deleteAlbum(42) ***");
        albumDAO.deleteAlbum(albumToUpdate);
        albumDAO.getAllAlbums().forEach(album -> log.info(album.toString()));

    }
}
