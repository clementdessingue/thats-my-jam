package com.cledess.thats_my_jam.authentication.models;

public class AccessTokenResponse {

    private final String accessToken;

    private final String refreshToken;

    public AccessTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    @Override
    public String toString() {
        return String.format("AccessTokenResponse {%n accessToken: %s,%n refreshToken: %s%n }", this.accessToken, this.refreshToken);
    }
}
