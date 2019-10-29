package com.meli.challenge.repository;

import com.meli.challenge.model.CurrencyDetail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyRepository {

    @GET("latest")
    Call<CurrencyDetail> getCurrencyDetail(@Query("base") String base);
}
