package com.pkb.example;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.pkb.unit.Bus;
import com.pkb.unit.Unit;

/**
 * MutableConfig
 * Demonstrates implementing some runtime-mutable configuration values in our application.
 * In this example, we can change some URL that other units can use. When the value is changed,
 * this unit (and therefore all it's dependents) will restart & re-connect to the new URL.
 */
public class MutableConfig extends Unit {
    public static final String UNIT_NAME = "mutable-config-unit";
    private static final long RETRY_PERIOD = 1;
    private static final TimeUnit RETRY_TIME_UNIT = TimeUnit.SECONDS;

    private HashMap<String, String> configs;

    public MutableConfig(Bus bus) {
        super(UNIT_NAME, bus, RETRY_PERIOD, RETRY_TIME_UNIT);
        configs = new HashMap<>();
    }

    @Override
    public HandleOutcome handleStart() {
        return HandleOutcome.SUCCESS;
    }

    @Override
    public HandleOutcome handleStop() {
        return HandleOutcome.SUCCESS;
    }

    public String getHttpClientUrl() {
        return configs.getOrDefault("http-client-url", "http://example.com");
    }

    public void setHttpClientUrl(String value) {
        configs.put("http-client-url", value);
        stop(); // causes deps to restart as well
    }
}
