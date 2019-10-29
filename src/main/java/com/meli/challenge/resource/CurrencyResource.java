package com.meli.challenge.resource;

import com.meli.challenge.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyResource {

    private String code;
    private String name;

    public CurrencyResource(Currency currencyModel) {
        this.code = currencyModel.getCode();
        this.name = currencyModel.getName();
    }
}
