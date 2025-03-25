package com.cledess.thats_my_jam.spotify.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyArtist(
    @JsonProperty("external_urls") ExternalUrls externalUrls,
    String href,
    @JsonProperty("id") String artistId,
    String name
) {}
