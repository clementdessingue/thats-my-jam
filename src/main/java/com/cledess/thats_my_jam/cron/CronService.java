package com.cledess.thats_my_jam.cron;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cledess.thats_my_jam.spotify.SpotifyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CronService {

    private final SpotifyService spotifyService;

    public CronService(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }
    
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void startGettingRecentlyPlayedTracks() {
        log.info("[CronService][startGettingRecentlyPlayedTracks] Start of scheduled task");
        this.spotifyService.retrieveRecentlyPlayedTracksAndInsertIntoDB();
        log.info("[CronService][startGettingRecentlyPlayedTracks] End of scheduled task");
    }
}
