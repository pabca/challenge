package com.meli.challenge.repository.impl;

import com.meli.challenge.exception.model.DatabaseException;
import com.meli.challenge.model.StatsCache;
import com.meli.challenge.repository.StatsRepository;
import com.meli.challenge.utils.Errors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Slf4j
public class DefaultStatsRepository implements StatsRepository {

    private static final String STATS_KEY = "STATS";

    private HashOperations<String, String, StatsCache> hashOperations;

    public DefaultStatsRepository(RedisTemplate<String, StatsCache> redisTemplate) {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Map<String, StatsCache> findAll() throws DatabaseException {
        try {
            return hashOperations.entries(STATS_KEY);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new DatabaseException(Errors.DATABASE_ERROR.getMessage());
        }
    }

    @Override
    public void save(String countryCode, StatsCache stats) throws DatabaseException {
        try {
            hashOperations.put(STATS_KEY, countryCode, stats);
        } catch(Exception e) {
            throw new DatabaseException(Errors.DATABASE_ERROR.getMessage());
        }
    }

    @Override
    public StatsCache findByCountryCode(String countryCode) throws DatabaseException {
        try {
            return hashOperations.get(STATS_KEY, countryCode);
        } catch(Exception e) {
            throw new DatabaseException(Errors.DATABASE_ERROR.getMessage());
        }
    }

    @Override
    public void deleteIndex() throws DatabaseException {
        try {
            hashOperations.keys(STATS_KEY).forEach(key -> hashOperations.delete(STATS_KEY, key));
        } catch(Exception e) {
            throw new DatabaseException(Errors.DATABASE_ERROR.getMessage());
        }

    }
}
