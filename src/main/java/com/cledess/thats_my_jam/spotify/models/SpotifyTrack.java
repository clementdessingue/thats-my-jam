package com.cledess.thats_my_jam.spotify.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SpotifyTrack(
    SpotifyAlbum album,
    List<SpotifyArtist> artists,
    @JsonProperty("explicit") Boolean isExplicit,
    @JsonProperty("external_urls") ExternalUrls externalUrls,
    String href,
    @JsonProperty("id") String trackId,
    String name,
    Integer popularity,
    @JsonProperty("track_number") Integer trackNumber
) {}

@JsonIgnoreProperties(ignoreUnknown = true)
record ExternalUrls(@JsonProperty("spotify") String spotifyUrl) {}