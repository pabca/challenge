package com.meli.challenge.repository;

import com.meli.challenge.exception.model.DatabaseException;
import com.meli.challenge.model.StatsCache;

import java.util.Map;

public interface StatsRepository {

    Map<String, StatsCache> findAll() throws DatabaseException;
    void save(String countryCode, StatsCache stats) throws DatabaseException;
    StatsCache findByCountryCode(String countryCode) throws DatabaseException;
    void deleteIndex() throws DatabaseException;
}
