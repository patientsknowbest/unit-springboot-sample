package com.pkb.example.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pkb.example.MutableConfig;
import com.pkb.unit.tracker.SystemState;

/**
 * ConfigController
 * Demonstrates providing an endpoint for mutating the mutable config.
 */
@RestController
public class ConfigController {
    private SystemState latest;
    private MutableConfig mutableConfig;

    public ConfigController(MutableConfig mutableConfig) {
        this.mutableConfig = mutableConfig;
    }

    @RequestMapping(path = "/setconfig", method = {RequestMethod.POST})
    public String setConfig(@RequestBody String value) {
        mutableConfig.setHttpClientUrl(value);
        return "OK";
    }
}