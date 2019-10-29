package com.meli.challenge.controller;

import com.meli.challenge.model.StatsCache;
import com.meli.challenge.resource.CountryStatsResource;
import com.meli.challenge.resource.StatsResource;
import com.meli.challenge.service.StatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/stats")
public class StatsController {

    private StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<StatsCache>> getAllStats() throws Exception {
        log.info("Call to GET /stats/all");
        return new ResponseEntity<>(statsService.findAll(), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<StatsResource> getStats() throws Exception {
        log.info("Call to GET /stats");
        StatsResource statsResource = new StatsResource();
        statsService.calculateAverageDistance().ifPresent(statsResource::setAverageDistance);
        statsService.getMinDistance().ifPresent(statsCache -> statsResource.setCountryMinDistance(new CountryStatsResource(statsCache)));
        statsService.getMaxDistance().ifPresent(statsCache -> statsResource.setCountryMaxDistance(new CountryStatsResource(statsCache)));
        return new ResponseEntity<>(statsResource, HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteStats() throws Exception {
        log.info("Call to DELETE /stats");
        statsService.deleteIndex();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
