package com.meli.challenge;

import com.meli.challenge.utils.City;
import com.meli.challenge.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class UtilsTest {

    @Test
    public void testCalculateDistanceToCity() {
        Assertions.assertEquals(0, Utils.calculateDistanceToCity(-34.0, -64.0, City.BUE));
        Assertions.assertEquals(7504, Utils.calculateDistanceToCity(23.0, -102.0, City.BUE));
        Assertions.assertEquals(10274, Utils.calculateDistanceToCity(40.0, -4.0, City.BUE));
    }

    @Test
    public void testIsValidIpAddress() {
        Assertions.assertFalse(Utils.isValidIpAddress(""));
        Assertions.assertFalse(Utils.isValidIpAddress(" "));
        Assertions.assertFalse(Utils.isValidIpAddress("notIp"));
        Assertions.assertFalse(Utils.isValidIpAddress("256.256.256.256"));
        Assertions.assertTrue(Utils.isValidIpAddress("192.168.10.1"));
    }

    @Test
    public void testCalculateCurrentTimeForTimezone() {

        List<String> timezones = Arrays.asList("UTC", "UTC-08:00","UTC-05:00");
        Instant time = Instant.parse("2019-10-23T10:12:35Z");

        List<String> times = Utils.calculateTimesForTimezones(timezones, "HH:mm:ss", time);

        Assertions.assertEquals("10:12:35", times.get(0));
        Assertions.assertEquals("02:12:35", times.get(1));
        Assertions.assertEquals("05:12:35", times.get(2));

    }

}
