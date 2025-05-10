package com.flagexplorer.config;

import com.flagexplorer.api.config.RestCountriesConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "restcountries.api.base-url=https://example.com/api")
public class RestCountriesConfigTest {
    @Autowired
    private RestCountriesConfig config;

    @Test
    void shouldLoadBaseUrlFromProperties() {
        assertEquals("https://example.com/api", config.getBaseUrl());
    }
}
