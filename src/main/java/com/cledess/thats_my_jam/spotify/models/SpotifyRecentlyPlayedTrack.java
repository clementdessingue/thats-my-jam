package com.cledess.thats_my_jam.spotify.models;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document("recently-played")
@JsonIgnoreProperties(ignoreUnknown = true)
public record SpotifyRecentlyPlayedTrack(
        SpotifyTrack track,
        @JsonProperty("played_at")
        String playedAt,
        @Id
        String id,
        @CreatedDate
        Instant createdAt) {

}
