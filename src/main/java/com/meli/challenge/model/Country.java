package com.meli.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {

    private List<Currency> currencies;
    private List<Language> languages;
    private List<Double> latlng;
    private List<String> timezones;
    private String name;
    private String alpha2Code;
}
