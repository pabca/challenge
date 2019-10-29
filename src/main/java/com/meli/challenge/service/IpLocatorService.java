package com.meli.challenge.service;

import com.meli.challenge.repository.IpLocatorRepository;
import com.meli.challenge.model.CountryInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Slf4j
@Service
public class IpLocatorService {

    private static final String IP_LOCATOR_SERVICE_URL = "https://api.ip2country.info/";
    private static final String IP_PARAMETER = "ip?";

    private IpLocatorRepository ipLocatorRepository;

    public IpLocatorService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IP_LOCATOR_SERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.ipLocatorRepository = retrofit.create(IpLocatorRepository.class);
    }

    /**
     * Returns the ISO code of the country from which the provided IP address comes from.
     *
     * @param ipAddress valid IP address to track.
     * @return the ISO code of the country (e.g. "US"), or an empty String if there is no information available for the IP address.
     * @throws IOException if there is an error connecting to the service.
     */
    public String getCountryCodeForIp(String ipAddress) throws IOException {

        Call<CountryInfo> retrofitCall = ipLocatorRepository.getCountryForIp(IP_PARAMETER + ipAddress);

        log.info("Making call to: " + retrofitCall.request().url());
        Response<CountryInfo> response = retrofitCall.execute();

        if (!response.isSuccessful()) {
            throw new IOException("Error getting country for IP: " + ipAddress);
        }
        return response.body() != null ? response.body().getCountryCode() : "";
    }
}
