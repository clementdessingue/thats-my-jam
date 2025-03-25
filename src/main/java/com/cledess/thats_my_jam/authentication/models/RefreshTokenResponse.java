package com.cledess.thats_my_jam.authentication.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RefreshTokenResponse(@JsonProperty("access_token") String accessToken) {
}