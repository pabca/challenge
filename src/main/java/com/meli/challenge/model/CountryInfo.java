package com.meli.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryInfo {

    private String countryCode;
    private String countryCode3;
    private String countryName;
    private String countryEmoji;
}
