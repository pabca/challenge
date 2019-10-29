package com.meli.challenge.repository;

import com.meli.challenge.model.Country;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CountryRepository {

    @GET("rest/v2/alpha/{countryCode}")
    Call<Country> getCountryByCountryCode(@Path("countryCode") String countryCode, @Query("fields") String fields);
}
