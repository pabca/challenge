package com.meli.challenge.service;

import com.meli.challenge.repository.CountryRepository;
import com.meli.challenge.model.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Slf4j
@Service
public class CountryService {

    private static final String COUNTRY_SERVICE_URL = "https://restcountries.eu/";
    private static final String fields = "currencies;languages;timezones;latlng;name;alpha2Code";

    private CountryRepository countryRepository;

    public CountryService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(COUNTRY_SERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.countryRepository = retrofit.create(CountryRepository.class);
    }

    /**
     * Returns information about a country based on the country ISO code.
     *
     * @param countryCode country ISO code (e.g. "gb").
     * @return information about the country.
     * @throws IOException if there is an error connecting to the service.
     */
    public Country getCountryByCountryCode(String countryCode) throws IOException {

        Call<Country> retrofitCall = countryRepository.getCountryByCountryCode(countryCode, fields);

        log.info("Making call to: " + retrofitCall.request().url());
        Response<Country> response = retrofitCall.execute();

        if (!response.isSuccessful()) {
            throw new IOException("Error getting country information for: " + countryCode);
        }
        return response.body();
    }
}
