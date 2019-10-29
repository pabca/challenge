package com.meli.challenge.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsResource {

    private Integer averageDistance;
    private CountryStatsResource countryMinDistance;
    private CountryStatsResource countryMaxDistance;
}
