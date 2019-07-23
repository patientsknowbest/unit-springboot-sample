package com.pkb.example.controller;

import static com.pkb.unit.Unchecked.unchecked;
import static com.pkb.unit.message.ImmutableMessage.message;
import static java.lang.String.format;

import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pkb.example.SampleHttpApiResource;
import com.pkb.unit.Bus;
import com.pkb.unit.Command;
import com.pkb.unit.State;
import com.pkb.unit.Unit;

/**
 * HelloController
 * An example top-level unit which depends on some other units
 * Note that because this is a spring RestController, it's still used
 * even if it's in a failed state. In this example, we're just checking
 * the result of state() prior to responding. This could probably be done
 * more generally using an interceptor.
 */
@RestController
public class HelloController extends Unit {
    public static final String UNIT_NAME = "hello-controller";
    private static final long RETRY_PERIOD = 1;
    private static final TimeUnit RETRY_TIME_UNIT = TimeUnit.SECONDS;

    private SampleHttpApiResource sampleHttpApiResource;

    public HelloController(Bus bus, SampleHttpApiResource sampleHttpApiResource) {
        super(UNIT_NAME, bus, RETRY_PERIOD, RETRY_TIME_UNIT);
        this.sampleHttpApiResource = sampleHttpApiResource;

        // We depend on some other resource
        addDependency(SampleHttpApiResource.UNIT_NAME);

        // Enable ourselves, we're a top-level unit
        unchecked(() -> bus.sink().accept(message(Command.class).withTarget(UNIT_NAME).withPayload(Command.ENABLE)));
    }


    @RequestMapping("/")
    public String index() {
        // Because we're a Spring boot RestController, this will get called whether whatever state
        // we're in. Check the state before responding.
        if (state() == State.STARTED) {
            String s = sampleHttpApiResource.someApiCall();
            return format("API call result %s", s);
        } else {
            return "Hey we failed for some reason";
        }
    }

    @Override
    protected Unit.HandleOutcome handleStart() {
        // If we wanted to change allowed routes, or present some kind of status message
        // we could do that here.
        return HandleOutcome.SUCCESS;
    }

    @Override
    protected Unit.HandleOutcome handleStop() {
        // If we wanted to change allowed routes, or present some kind of status message
        // we could do that here.
        return HandleOutcome.SUCCESS;
    }
}