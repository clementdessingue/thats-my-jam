package com.cledess.thats_my_jam.spotify.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SpotifyRecentlyPlayedTracksResponse(
    @JsonProperty("items") List<SpotifyRecentlyPlayedTrack> tracks
) {}