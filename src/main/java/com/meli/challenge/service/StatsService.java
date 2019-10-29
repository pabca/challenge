package com.meli.challenge.service;

import com.meli.challenge.exception.model.DatabaseException;
import com.meli.challenge.model.StatsCache;
import com.meli.challenge.repository.StatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StatsService {

    private StatsRepository statsRepository;

    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    /**
     * Returns all invocations stats available.
     *
     * @return list of invocations stats.
     * @throws DatabaseException if there is an error connecting to the database.
     */
    public List<StatsCache> findAll() throws DatabaseException {
        log.info("Retrieving all invocations stats");
        return new ArrayList<>(statsRepository.findAll().values());
    }

    /**
     * Updates invocation stats for the given country. Creates the stats if those didn't exist.
     *
     * @param countryCode ISO code for the country to be updated.
     * @param statsCache invocation stats.
     * @throws DatabaseException if there is an error connecting to the database.
     */
    public void update(String countryCode, StatsCache statsCache) throws DatabaseException {
        log.info("Updating invocation stats for country: " + countryCode);
        StatsCache stats = statsRepository.findByCountryCode(countryCode);
        if(stats != null) {
            stats.setNumberOfInvocations(stats.getNumberOfInvocations() + 1);
        } else {
            log.info("(Creating stats for country: " + countryCode + ")");
            stats = statsCache;
        }
        save(countryCode, stats);
    }

    /**
     * Calculates the average distance of all the invocations.
     *
     * @return Optional with the value of the average distance, or an empty Optional if stats are not available.
     * @throws DatabaseException if there is an error connecting to the database.
     */
    public Optional<Integer> calculateAverageDistance() throws DatabaseException {
        log.info("Calculating average distance");
        List<StatsCache> stats = findAll();
        if(CollectionUtils.isNotEmpty(stats)) {
            int acumDist = 0;
            int acumInv = 0;
            for(StatsCache stat : stats) {
                acumInv = acumInv + stat.getNumberOfInvocations();
                acumDist = acumDist + (stat.getDistance() * stat.getNumberOfInvocations());
            }
            return Optional.of(acumDist / acumInv);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Returns invocation stats of the nearest country.
     *
     * @return Optional with the invocation stats of the nearest country, or an empty Optional if the stats are not available.
     * @throws DatabaseException if there is an error connecting to the database.
     */
    public Optional<StatsCache> getMinDistance() throws DatabaseException {
        log.info("Retrieving stats of nearest country");
        return findAll().stream().min(Comparator.comparingInt(StatsCache::getDistance));
    }

    /**
     * Returns invocation stats of the furthest country.
     *
     * @return Optional with the invocation stats of the furthest country, or an empty Optional if the stats are not available.
     * @throws DatabaseException if there is an error connecting to the database.
     */
    public Optional<StatsCache> getMaxDistance() throws DatabaseException {
        log.info("Retrieving stats of furthest country");
        return findAll().stream().max(Comparator.comparingInt(StatsCache::getDistance));
    }

    /**
     * Deletes all invocation stats.
     *
     * @throws DatabaseException if there is an error connecting to the database.
     */
    public void deleteIndex() throws DatabaseException {
        log.warn("Deleting stats of all invocations");
        statsRepository.deleteIndex();
    }

    private void save(String countryCode, StatsCache statsCache) throws DatabaseException {
        statsRepository.save(countryCode, statsCache);
    }
}
