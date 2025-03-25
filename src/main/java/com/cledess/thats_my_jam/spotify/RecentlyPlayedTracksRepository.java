package com.cledess.thats_my_jam.spotify;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cledess.thats_my_jam.spotify.models.SpotifyRecentlyPlayedTrack;

public interface RecentlyPlayedTracksRepository extends MongoRepository<SpotifyRecentlyPlayedTrack, String> {
    
}
