package com.pkb.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pkb.unit.Bus;
import com.pkb.unit.LocalBus;
import com.pkb.unit.tracker.SystemState;
import com.pkb.unit.tracker.Tracker;

import io.reactivex.Observable;

@Configuration
public class Config {
    @Bean
    public Bus bus() {
        return new LocalBus();
    }

    @Bean
    public Observable<SystemState> tracker(Bus bus) {
        return Tracker.track(bus);
    }

    @Bean
    public MutableConfig mutableConfig(Bus bus) {
        return new MutableConfig(bus);
    }

    @Bean
    public SampleHttpApiResource someResource(Bus bus, MutableConfig config) {
        return new SampleHttpApiResource(bus, config);
    }
}
