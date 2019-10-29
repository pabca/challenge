package com.meli.challenge.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static final Integer EARTH_RADIUS = 6371;
    private static final String IP_REGEX = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    private static final String UTC_TIMEZONE = "UTC";

    /**
     * Calculates the approximate distance in kilometers between a point and the provided City.
     *
     * @param lat latitude of the point.
     * @param lon longitude of the point.
     * @param city City to calculate distance.
     * @return the approximate distance in kilometers.
     */
    public static Integer calculateDistanceToCity(Double lat, Double lon, City city) {
        return (int)(Math.acos(Math.sin(toRad(lat)) * Math.sin(toRad(city.getLat())) +
                Math.cos(toRad(lat)) * Math.cos(toRad(city.getLat())) *
                        Math.cos(toRad(city.getLon()) - toRad(lon))) * EARTH_RADIUS);
    }

    /**
     * Validates the format of an IP address and the range (0.0.0.0 to 255.255.255.255).
     *
     * @param ip string that represents the IP address to validate.
     * @return true if the IP has the correct format or false.
     */
    public static Boolean isValidIpAddress(String ip) {
        return StringUtils.isNotBlank(ip) && ip.matches(IP_REGEX);
    }

    /**
     * Returns a list of dates/times, which consist in transforming the provided date/time in the different timezones that are also provided.
     *
     * @param timezones list of timezones that are used for the transformation.
     * @param format output format that is used to populate the returned list.
     * @param time date/time to transform.
     * @return list of dates/times, with the provided format, based on the provided date/time and timezones.
     */
    public static List<String> calculateTimesForTimezones(List<String> timezones, String format, Instant time) {
        return timezones.stream()
                .map(timezone -> timezone.replace(UTC_TIMEZONE, ""))
                .map(timezone -> ZonedDateTime.ofInstant(time, (StringUtils.isEmpty(timezone) ? ZoneOffset.UTC : ZoneOffset.of(timezone)).normalized()))
                .map(timezone -> timezone.format(DateTimeFormatter.ofPattern(format)))
                .collect(Collectors.toList());
    }

    private static Double toRad(Double deg) {
        return deg*Math.PI/180;
    }
}
