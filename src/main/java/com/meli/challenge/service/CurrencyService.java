package com.meli.challenge.service;

import com.meli.challenge.repository.CurrencyRepository;
import com.meli.challenge.model.CurrencyDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class CurrencyService {

    private static final String USD_ISO_CODE = "USD";
    private static final String CURRENCY_SERVICE_URL = "https://api.exchangeratesapi.io/";

    private CurrencyRepository currencyRepository;

    public CurrencyService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CURRENCY_SERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.currencyRepository = retrofit.create(CurrencyRepository.class);
    }

    /**
     * Returns the value (in US dollars) of one unit of the provided currency.
     *
     * @param currencyCode ISO code of the currency (e.g. "EUR").
     * @return Optional with the US dollars rate, or empty Optional if there is no available information of the currency.
     * @throws IOException if there is an error connecting to the service.
     */
    public Optional<Double> getUsdRate(String currencyCode) throws IOException {
        Call<CurrencyDetail> retrofitCall = currencyRepository.getCurrencyDetail(currencyCode);

        log.info("Making call to: " + retrofitCall.request().url());
        Response<CurrencyDetail> response = retrofitCall.execute();

        if (!response.isSuccessful()) {
            if (response.code() == HttpStatus.BAD_REQUEST.value()) {
                return Optional.empty();
            }
            throw new IOException("Error getting currency: " + currencyCode);
        }
        return response.body() != null ? Optional.of(response.body().getRates().get(USD_ISO_CODE)) : Optional.empty();
    }
}
