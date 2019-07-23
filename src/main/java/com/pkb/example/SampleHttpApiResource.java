package com.pkb.example;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.pkb.unit.Bus;
import com.pkb.unit.Unit;

/**
 * SampleHttpApiResource
 * A sample resource, showing how something like an HTTP client
 * with some authentication token can be wrapped up in a Unit.
 */
public class SampleHttpApiResource extends Unit {
    public static final String UNIT_NAME = "some-resource";
    private static final long RETRY_PERIOD = 1;
    private static final TimeUnit RETRY_TIME_UNIT = TimeUnit.SECONDS;

    private MutableConfig mutableConfig;
    private CloseableHttpClient client;
    private String someAuthToken;

    public SampleHttpApiResource(Bus bus, MutableConfig mutableConfig) {
        super(UNIT_NAME, bus, RETRY_PERIOD, RETRY_TIME_UNIT);
        this.mutableConfig = mutableConfig;
        addDependency(MutableConfig.UNIT_NAME);
    }

    @Override
    public HandleOutcome handleStart() {
        try {
            // Obtain some authentication token
            client = HttpClients.createDefault();
            HttpRequest httpRequest = DefaultHttpRequestFactory.INSTANCE.newHttpRequest("POST", mutableConfig.getHttpClientUrl());
            CloseableHttpResponse httpResponse = client.execute(HttpHost.create(mutableConfig.getHttpClientUrl()), httpRequest);
            someAuthToken = EntityUtils.toString(httpResponse.getEntity());

            return HandleOutcome.SUCCESS;
        } catch (IOException | MethodNotSupportedException e) {
            return HandleOutcome.FAILURE;
        }
    }

    @Override
    public HandleOutcome handleStop() {
        try {
            client.close();
            return HandleOutcome.SUCCESS;
        } catch (IOException e) {
            return HandleOutcome.FAILURE;
        }
    }

    public String someApiCall() {
        HttpGet httpGet = new HttpGet();
        // Use the authentication token
        httpGet.addHeader("some-auth", someAuthToken);
        httpGet.setURI(URI.create(mutableConfig.getHttpClientUrl()));
        try (CloseableHttpResponse closeableHttpResponse = client.execute(httpGet)) {
            return EntityUtils.toString(closeableHttpResponse.getEntity());
        } catch (IOException e) {
            // Oops, better restart
            failed();
            throw new RuntimeException(e);
        }
    }
}
