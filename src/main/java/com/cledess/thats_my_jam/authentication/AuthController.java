package com.cledess.thats_my_jam.authentication;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/token")
    public String getToken(OAuth2AuthenticationToken authentication) {
        return this.authService.getAccessAndRefreshToken(authentication).toString();
    }
    
}
