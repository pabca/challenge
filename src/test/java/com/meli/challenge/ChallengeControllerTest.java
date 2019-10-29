package com.meli.challenge;

import com.meli.challenge.controller.ChallengeController;
import com.meli.challenge.exception.model.DatabaseException;
import com.meli.challenge.exception.model.FormatException;
import com.meli.challenge.exception.model.NotFoundException;
import com.meli.challenge.model.Country;
import com.meli.challenge.model.Currency;
import com.meli.challenge.model.Language;
import com.meli.challenge.model.StatsCache;
import com.meli.challenge.resource.CountryResource;
import com.meli.challenge.service.CountryService;
import com.meli.challenge.service.CurrencyService;
import com.meli.challenge.service.IpLocatorService;
import com.meli.challenge.service.StatsService;
import com.meli.challenge.utils.City;
import com.meli.challenge.utils.Utils;
import mockit.*;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class ChallengeControllerTest {

    private ChallengeController challengeController;

    @Mocked
    private IpLocatorService ipLocatorService;
    @Mocked
    private CountryService countryService;
    @Mocked
    private CurrencyService currencyService;
    @Mocked
    private StatsService statsService;

    @BeforeEach
    public void prepareTests() {
        challengeController = new ChallengeController(ipLocatorService, countryService, currencyService, statsService);
    }

    @Test
    public void testGetLocationInfo_badIp() throws Exception {

        new MockUp<Utils>() {
            @Mock
            public Boolean isValidIpAddress(String ip) {
                return false;
            }
        };

        Assertions.assertThrows(FormatException.class, () -> {
            challengeController.getLocationInfo("someIp");
        });
    }

    @Test
    public void testGetLocationInfo_notFoundIp() throws Exception {

        new MockUp<Utils>() {
            @Mock
            public Boolean isValidIpAddress(String ip) {
                return true;
            }
        };

        new Expectations() {{
           ipLocatorService.getCountryCodeForIp("someIp"); result = "";
        }};

        Assertions.assertThrows(NotFoundException.class, () -> {
            challengeController.getLocationInfo("someIp");
        });

        new FullVerifications(ipLocatorService){};
    }

    @Test
    public void testGetLocationInfo_exceptionCountryService() throws Exception {

        new MockUp<Utils>() {
            @Mock
            public Boolean isValidIpAddress(String ip) {
                return true;
            }
        };

        new Expectations() {{
            ipLocatorService.getCountryCodeForIp("someIp"); result = "validCountryCode";
            countryService.getCountryByCountryCode("validCountryCode"); result = new IOException();
        }};

        Assertions.assertThrows(Exception.class, () -> {
            challengeController.getLocationInfo("someIp");
        });

        new FullVerifications(ipLocatorService, countryService){};
    }

    @Test
    public void testGetLocationInfo_exceptionCurrencyService() throws Exception {

        new MockUp<Utils>() {
            @Mock
            public Boolean isValidIpAddress(String ip) {
                return true;
            }
        };

        Country country = new Country();
        Currency currency = new Currency();
        currency.setCode("US");
        country.setCurrencies(Collections.singletonList(currency));

        new Expectations() {{
            ipLocatorService.getCountryCodeForIp("someIp"); result = "validCountryCode";
            countryService.getCountryByCountryCode("validCountryCode"); result = country;
            currencyService.getUsdRate("US"); result = new IOException();
        }};

        Assertions.assertThrows(Exception.class, () -> {
            challengeController.getLocationInfo("someIp");
        });

        new FullVerifications(ipLocatorService, countryService, currencyService){};
    }

    @Test
    public void testGetLocationInfo_exceptionStatsService() throws Exception {

        new MockUp<Utils>() {
            @Mock
            public Boolean isValidIpAddress(String ip) {
                return true;
            }
        };

        Country country = new Country();
        Currency currency = new Currency();
        currency.setCode("US");
        country.setCurrencies(Collections.singletonList(currency));
        Optional<Double> usdRate = Optional.of(2.0);
        Double usd = 2.0;
        CountryResource countryResource = new CountryResource();
        countryResource.setIsoCode("someCode");
        countryResource.setName("someName");
        countryResource.setDistanceToBue(1);

        new MockUp<CountryResource>() {
            @Mock
            public void $init(Country country, Double usdRate) {}
        };

        new Expectations() {{
            ipLocatorService.getCountryCodeForIp("someIp"); result = "validCountryCode";
            countryService.getCountryByCountryCode("validCountryCode"); result = country;
            currencyService.getUsdRate("US"); result = usdRate;
            statsService.update(null, (StatsCache)any); result = new DatabaseException("errorMessage");
        }};

        ResponseEntity<CountryResource> response = challengeController.getLocationInfo("someIp");

        new FullVerifications(ipLocatorService, countryService, currencyService, statsService){};
        Assertions.assertEquals("errorMessage", response.getBody().getErrors().get(0).getMessage());
    }

    @Test
    public void testGetLocationInfo_successResponse() throws Exception {

        new MockUp<Utils>() {
            @Mock
            public Boolean isValidIpAddress(String ip) {
                return true;
            }
            @Mock
            public Integer calculateDistanceToCity(Double lat, Double lon, City city) {
                return 1;
            }
            @Mock
            public List<String> calculateTimesForTimezones(List<String> timezones, String format, Instant time) {
                return Collections.singletonList("12:00:05");
            }
        };

        Country country = new Country();
        Language language = new Language("ES", "spa", "spanish", "espanol");
        Currency currency = new Currency();
        currency.setCode("US");
        country.setCurrencies(Collections.singletonList(currency));
        country.setLanguages(Collections.singletonList(language));
        country.setLatlng(Arrays.asList(1.0, 2.0));
        country.setName("espana");
        country.setAlpha2Code("es");
        Optional<Double> usdRate = Optional.of(2.0);
        Double usd = 2.0;
        country.setTimezones(Collections.singletonList("UTC+01:00"));
        CountryResource countryResource = new CountryResource();
        countryResource.setIsoCode("someCode");
        countryResource.setName("someName");
        countryResource.setDistanceToBue(1);

        new Expectations() {{
            ipLocatorService.getCountryCodeForIp("someIp"); result = "validCountryCode";
            countryService.getCountryByCountryCode("validCountryCode"); result = country;
            currencyService.getUsdRate("US"); result = usdRate;
            statsService.update("es", (StatsCache)any);
        }};

        ResponseEntity<CountryResource> response = challengeController.getLocationInfo("someIp");

        new FullVerifications(ipLocatorService, countryService, currencyService, statsService){};
        CountryResource cr = response.getBody();
        Assertions.assertTrue(CollectionUtils.isEmpty(cr.getErrors()));
        Assertions.assertEquals("espana", cr.getName());
        Assertions.assertEquals("es", cr.getIsoCode());
        Assertions.assertEquals(1, cr.getDistanceToBue());
        Assertions.assertEquals(2.0, cr.getUsdRate());
        Assertions.assertEquals("US", cr.getCurrency().getCode());
        Assertions.assertTrue(CollectionUtils.isNotEmpty(cr.getTimezones()));
        Assertions.assertEquals("UTC+01:00", cr.getTimezones().get(0));
        Assertions.assertEquals("12:00:05", cr.getTimes().get(0));
        Assertions.assertTrue(CollectionUtils.isNotEmpty(cr.getLanguages()));
        Assertions.assertEquals("ES", cr.getLanguages().get(0).getIsoCode());
    }
}
