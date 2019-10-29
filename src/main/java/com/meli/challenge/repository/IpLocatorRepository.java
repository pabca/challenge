package com.meli.challenge.repository;

import com.meli.challenge.model.CountryInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IpLocatorRepository {

    @GET()
    Call<CountryInfo> getCountryForIp(@Url String url);
}
