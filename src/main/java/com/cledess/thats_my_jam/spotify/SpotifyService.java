package com.cledess.thats_my_jam.spotify;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.cledess.thats_my_jam.spotify.models.SpotifyRecentlyPlayedTrack;
import com.cledess.thats_my_jam.spotify.models.SpotifyRecentlyPlayedTracksResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SpotifyService {

    private final SpotifyClient spotifyClient;
    private final MongoOperations mongoTemplate;
    

    public SpotifyService(SpotifyClient spotifyClient, MongoOperations mongoTemplate) {
        this.spotifyClient = spotifyClient;
        this.mongoTemplate = mongoTemplate;
    }

    public void retrieveRecentlyPlayedTracksAndInsertIntoDB() {
        SpotifyRecentlyPlayedTracksResponse recentlyPlayedTracks = this.spotifyClient.getRecentlyPlayedTracks();

        int successfullyInsertedTracks = 0;
        List<String> unsuccessfullyInsertedTracksIds = new ArrayList<>();

        for (SpotifyRecentlyPlayedTrack track: recentlyPlayedTracks.tracks()) {
            SpotifyRecentlyPlayedTrack trackDB = this.mongoTemplate.findOne(new Query(Criteria.where("track.trackId").is(track.track().trackId()).and("playedAt").is(track.playedAt())), SpotifyRecentlyPlayedTrack.class);

            if (trackDB == null) {
                try {
                    this.mongoTemplate.save(track);
                    successfullyInsertedTracks++;
                } catch (Exception e) {
                    log.error("[SpotifyService][retrieveRecentlyPlayedTracksAndInsertIntoDB] Error inserting track into DB: {}", e.getMessage());
                    unsuccessfullyInsertedTracksIds.add(track.id());
                }
            }
        }

        if (!unsuccessfullyInsertedTracksIds.isEmpty()) {
            log.error("[SpotifyService][retrieveRecentlyPlayedTracksAndInsertIntoDB] Failed to insert tracks ids into recently-played", unsuccessfullyInsertedTracksIds.toString());
        } else if (successfullyInsertedTracks == 0) {
            log.info("[SpotifyService][retrieveRecentlyPlayedTracksAndInsertIntoDB] No new tracks to insert into recently-played");
        } else {
            log.info("[SpotifyService][retrieveRecentlyPlayedTracksAndInsertIntoDB] Successfully inserted {} tracks into recently-played", successfullyInsertedTracks);
        }
    }
}
