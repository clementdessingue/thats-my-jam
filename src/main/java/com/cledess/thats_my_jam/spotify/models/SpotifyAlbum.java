package com.cledess.thats_my_jam.spotify.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SpotifyAlbum(
    @JsonProperty("album_type") String albumType,
    List<SpotifyArtist> artists,
    @JsonProperty("external_urls") ExternalUrls externalUrls,
    String href,
    @JsonProperty("id") String albumId,
    List<Image> images,
    String name,
    @JsonProperty("release_date") String releaseDate,
    @JsonProperty("total_tracks") Integer totalTracks
) {}

@JsonIgnoreProperties(ignoreUnknown = true)
record Image(
    Integer height,
    String url,
    Integer width
) {}