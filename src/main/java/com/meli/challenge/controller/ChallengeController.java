package com.meli.challenge.controller;

import com.meli.challenge.exception.model.DatabaseException;
import com.meli.challenge.exception.model.FormatException;
import com.meli.challenge.exception.model.NotFoundException;
import com.meli.challenge.model.Country;

import com.meli.challenge.model.StatsCache;
import com.meli.challenge.resource.CountryResource;
import com.meli.challenge.service.CountryService;
import com.meli.challenge.service.CurrencyService;
import com.meli.challenge.service.IpLocatorService;
import com.meli.challenge.service.StatsService;
import com.meli.challenge.utils.Errors;
import com.meli.challenge.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/location")
public class ChallengeController {

    private IpLocatorService ipLocatorService;
    private CountryService countryService;
    private CurrencyService currencyService;
    private StatsService statsService;

    public ChallengeController(IpLocatorService ipLocatorService, CountryService countryService, CurrencyService currencyService, StatsService statsService) {
        this.ipLocatorService = ipLocatorService;
        this.countryService = countryService;
        this.currencyService = currencyService;
        this.statsService = statsService;
    }

    @GetMapping("")
    public ResponseEntity<CountryResource> getLocationInfo(@RequestParam(name = "originIp") String originIpAddress) throws Exception {
        log.info("Call to GET /location for IP address: " + originIpAddress);

        if (!Utils.isValidIpAddress(originIpAddress)) {
            throw new FormatException(Errors.IP_FORMAT_ERROR.getMessage());
        }

        String countryCode = ipLocatorService.getCountryCodeForIp(originIpAddress);
        if (StringUtils.isEmpty(countryCode)) {
            throw new NotFoundException(Errors.IP_NOT_FOUND_ERROR.getMessage());
        }
        Country country = countryService.getCountryByCountryCode(countryCode);
        Optional<Double> usdRate = currencyService.getUsdRate(country.getCurrencies().get(0).getCode());
        CountryResource countryResource = usdRate.map(usd -> new CountryResource(country, usd)).orElseGet(() -> new CountryResource(country));

        try {
            log.info("Updating invocation stats");
            statsService.update(countryResource.getIsoCode(), new StatsCache(countryResource.getName(), countryResource.getDistanceToBue(), 1));
        } catch (DatabaseException e) {
            // If there is an error while saving stats, the API will still provide a response.
            // An error object will be included in the response.
            log.warn("Partial error: " + e.getMessage());
            countryResource.addError(e.getMessage());
        }
        return new ResponseEntity<>(countryResource, HttpStatus.OK);
    }
}
