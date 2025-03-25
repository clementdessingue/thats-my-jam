package com.cledess.thats_my_jam.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.cledess.thats_my_jam.authentication.models.AccessTokenResponse;
import com.cledess.thats_my_jam.authentication.models.RefreshTokenResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.spotify.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.oauth2.access_token}")
    private String accessToken;

    @Value("${spotify.oauth2.refresh_token}")
    private String refreshToken;

    @Value("${spring.security.oauth2.client.provider.spotify.token-uri}")
    private String spotifyTokenUri;

    public AuthService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
        this.webClient = WebClient.builder().build();
    }

    public String refreshAccessToken() {
        MultiValueMap<String, String> bodyForm = new LinkedMultiValueMap<>();
        bodyForm.add("grant_type", "refresh_token");
        bodyForm.add("refresh_token", refreshToken);

        try {
            RefreshTokenResponse tokenResponse = webClient.post()
                    .uri(spotifyTokenUri)
                .headers(headers -> {
                    headers.setBasicAuth(clientId, clientSecret);
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                    .bodyValue(bodyForm)
                .retrieve()
                .bodyToMono(RefreshTokenResponse.class)
                .block();

            if (tokenResponse == null) {
                throw new IllegalStateException("[AuthService][refreshAccessToken] Access Token is null.");
            }

            return tokenResponse.accessToken();
        } catch (Exception e) {
            log.error("[AuthService][refreshAccessToken] Failed to refresh access token with error : {}", e.getMessage());
            throw e;
        }
    }

    public AccessTokenResponse getAccessAndRefreshToken(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient authorizedClient = 
            this.authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        if (authorizedClient == null) {
            throw new IllegalStateException("[AuthService][getAccessAndRefreshToken] No authorized client found for principal : " + authentication.getName());
        }

        OAuth2AccessToken accessTokenFromAuthentication = authorizedClient.getAccessToken();
        OAuth2RefreshToken refreshTokenFromAuthentication = authorizedClient.getRefreshToken();

        if (accessTokenFromAuthentication == null || refreshTokenFromAuthentication == null) {
            throw new IllegalStateException("[AuthService][getAccessAndRefreshToken] No access token and/or refresh token found for principal : " + authentication.getName());
        }

        this.accessToken = accessTokenFromAuthentication.getTokenValue();
        this.refreshToken = refreshTokenFromAuthentication.getTokenValue();

        return new AccessTokenResponse(this.accessToken, this.refreshToken);
    }

   
}
