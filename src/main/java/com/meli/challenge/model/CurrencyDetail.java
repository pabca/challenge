package com.meli.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDetail {

    private Map<String, Double> rates;
    private String base;
    private String date;
}
