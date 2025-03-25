package com.cledess.thats_my_jam.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.cledess.thats_my_jam.authentication.models.AccessTokenResponse;
import com.cledess.thats_my_jam.authentication.models.RefreshTokenResponse;

@Service
public class AuthService {
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.spotify.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.spotify.client-secret}")
    private String clientSecret;

    @Value("${access_token}")
    private String accessToken;

    @Value("${refresh_token}")
    private String refreshToken;

    public AuthService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
        this.webClient = WebClient.builder().baseUrl("https://accounts.spotify.com/api").build();
    }

    public String refreshAccessToken() {
        RefreshTokenResponse tokenResponse = webClient.post()
                .uri("/token")
                .headers(headers -> {
                    headers.setBasicAuth(clientId, clientSecret);
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .bodyValue("grant_type=refresh_token&refresh_token=" + refreshToken)
                .retrieve()
                .bodyToMono(RefreshTokenResponse.class)
                .block();
        
        if (tokenResponse == null) {
            throw new IllegalStateException("Failed to refresh access token.");
        }

        return tokenResponse.accessToken();
    }

    public AccessTokenResponse getAccessAndRefreshToken(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient authorizedClient = 
            this.authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
            throw new IllegalStateException("No authorized client or access token found for principal : " + authentication.getName());
        }

        this.accessToken = authorizedClient.getAccessToken().getTokenValue();
        this.refreshToken = authorizedClient.getRefreshToken().getTokenValue();

        return new AccessTokenResponse(this.accessToken, this.refreshToken);
    }

   
}
