package com.meli.challenge.resource;

import com.meli.challenge.model.Country;
import com.meli.challenge.utils.City;
import com.meli.challenge.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryResource extends BaseResource {

    private static final String TIME_FORMAT = "HH:mm:ss";

    private String name;
    private String isoCode;
    private Integer distanceToBue;
    private Double usdRate;
    private CurrencyResource currency;
    private List<String> timezones;
    private List<String> times;
    private List<LanguageResource> languages;

    public CountryResource(Country country) {
        this(country, null);
    }

    public CountryResource(Country country, Double usdRate) {
        this.name = country.getName();
        this.isoCode = country.getAlpha2Code();
        this.distanceToBue = Utils.calculateDistanceToCity(country.getLatlng().get(0), country.getLatlng().get(1), City.BUE);
        this.usdRate = usdRate;
        this.currency = new CurrencyResource(country.getCurrencies().get(0));
        this.timezones = country.getTimezones();
        this.times = Utils.calculateTimesForTimezones(country.getTimezones(), TIME_FORMAT, Instant.now());
        this.languages = country.getLanguages().stream().map(LanguageResource::new).collect(Collectors.toList());
    }
}
