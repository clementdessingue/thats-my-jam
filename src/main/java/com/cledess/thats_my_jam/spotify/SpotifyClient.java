package com.cledess.thats_my_jam.spotify;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.cledess.thats_my_jam.authentication.AuthService;
import com.cledess.thats_my_jam.spotify.models.SpotifyRecentlyPlayedTracksResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SpotifyClient {

    private final AuthService authService;
    private final WebClient webClient;

    public SpotifyClient(AuthService authService) {
        this.authService = authService;
        this.webClient = WebClient.builder().baseUrl("https://api.spotify.com/v1").build();
    }

    public SpotifyRecentlyPlayedTracksResponse getRecentlyPlayedTracks() {
        log.info("[SpotifyClient][getRecentlyPlayedTracks] Retrieving recently played tracks from Spotify...");
        String accessToken = authService.refreshAccessToken();

        SpotifyRecentlyPlayedTracksResponse tracksResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/me/player/recently-played")
                    .queryParam("limit", 50)
                    .build())
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(SpotifyRecentlyPlayedTracksResponse.class)
                .block();

        log.info("[SpotifyClient][getRecentlyPlayedTracks] Retrieved {} tracks from Spotify", tracksResponse.tracks().size());
        return tracksResponse;
    }
}
