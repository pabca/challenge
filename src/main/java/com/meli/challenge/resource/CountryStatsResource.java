package com.meli.challenge.resource;

import com.meli.challenge.model.StatsCache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryStatsResource {

    private String country;
    private Integer distance;
    private Integer numberOfInvocations;

    public CountryStatsResource(StatsCache statsCache) {
        this.country = statsCache.getCountry();
        this.distance = statsCache.getDistance();
        this.numberOfInvocations = statsCache.getNumberOfInvocations();
    }
}
