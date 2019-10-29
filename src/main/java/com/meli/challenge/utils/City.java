package com.meli.challenge.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum that represents cities.
 */
@AllArgsConstructor
@Getter
public enum City {

    BUE(-34.0, -64.0);

    private Double lat;
    private Double lon;
}
