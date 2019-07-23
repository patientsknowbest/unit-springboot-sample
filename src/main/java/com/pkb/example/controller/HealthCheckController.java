package com.pkb.example.controller;

import static com.pkb.unit.dot.DOT.toDOTFormat;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pkb.unit.tracker.SystemState;

import io.reactivex.Observable;

/**
 * Demonstrates use of the Tracker to display the current system state.
 * This data could be scraped by a system dashboard to provide the current
 * state of the system for diagnostic purposes.
 */
@RestController
public class HealthCheckController {
    private SystemState latest;

    public HealthCheckController(Observable<SystemState> tracker) {
        tracker.subscribe(state -> latest = state);
    }

    @RequestMapping("/healthcheck")
    public String index() {
        return toDOTFormat(latest);
    }
}