package com.meli.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class StatsCache implements Serializable, RedisData {

    private String country;
    private Integer distance;
    private Integer numberOfInvocations;
}
