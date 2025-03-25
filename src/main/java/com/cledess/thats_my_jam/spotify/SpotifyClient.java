package com.cledess.thats_my_jam.spotify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.cledess.thats_my_jam.authentication.AuthService;
import com.cledess.thats_my_jam.spotify.exceptions.EmptyResponseException;
import com.cledess.thats_my_jam.spotify.models.SpotifyRecentlyPlayedTracksResponse;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SpotifyClient {

    @Value("${spotify.api.base-url}")
    private String spotifyApiUrl;

    @Value("${spotify.api.recently-played-path}")
    private String spotifyApiRecentlyPlayedPath;

    private final AuthService authService;
    private WebClient webClient;

    public SpotifyClient(AuthService authService) {
        this.authService = authService;
    }

    @PostConstruct
    private void init() {
        this.webClient = WebClient.builder().baseUrl(this.spotifyApiUrl).build();
    }

    public SpotifyRecentlyPlayedTracksResponse getRecentlyPlayedTracks() {
        log.info("[SpotifyClient][getRecentlyPlayedTracks] Retrieving recently played tracks from Spotify...");
        try {
            String accessToken = authService.refreshAccessToken();

            SpotifyRecentlyPlayedTracksResponse tracksResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path(spotifyApiRecentlyPlayedPath)
                    .queryParam("limit", 50)
                    .build())
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(SpotifyRecentlyPlayedTracksResponse.class)
                .block();

            if (tracksResponse == null) {
                throw new EmptyResponseException("Empty response from api : " + spotifyApiUrl + spotifyApiRecentlyPlayedPath);
            }

            log.info("[SpotifyClient][getRecentlyPlayedTracks] Retrieved {} tracks from Spotify", tracksResponse.tracks().size());
            return tracksResponse;
        } catch (Exception e) {
            log.error("[SpotifyClient][getRecentlyPlayedTracks] Error retrieving recently played tracks from Spotify: {}", e.getMessage());
            return null;
        }
    }
}
