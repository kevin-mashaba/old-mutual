package com.flagexplorer.api;

import com.flagexplorer.exception.CountryNotFoundException;
import com.flagexplorer.model.Country;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CountryApiClientTest {

    private static WireMockServer wireMockServer;
    private WebClient webClient;

    @InjectMocks
    private CountryApiClient countryApiClient;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @BeforeEach
    void setup() {
        webClient = WebClient.builder().baseUrl("http://localhost:8089").build();

        countryApiClient = new CountryApiClient(webClient);
    }

    @Test
    void shouldFetchAllCountries() {
        stubFor(get(urlEqualTo("/all"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                        [
                          {
                            "name": { "common": "Kenya" },
                            "cca2": "KE",
                            "capital": ["Nairobi"],
                            "population": 53771300
                          },
                          {
                            "name": { "common": "Japan" },
                            "cca2": "JP",
                            "capital": ["Tokyo"],
                            "population": 126000000
                          }
                        ]
                        """)));

        List<Country> countries = countryApiClient.fetchAllCountries();

        assertNotNull(countries);
        assertEquals(2, countries.size());
        assertEquals("Kenya", countries.get(0).name());
        assertEquals("JP", countries.get(1).code());

        verify(getRequestedFor(urlEqualTo("/all")));
    }

    @Test
    void shouldFetchCountryByNameWhenFound() {
        stubFor(get(urlEqualTo("/name/Kenya"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                                "name": {
                                    "common": "Kenya"
                                },
                                "cca2": "KE",
                                "capital": ["Nairobi"],
                                "population": 53771300,
                                "flag": "https://flagcdn.com/ke.svg"
                            }
                        """)));

        Optional<Country> country = countryApiClient.fetchCountryByName("Kenya");

        assertTrue(country.isPresent());
        assertEquals("Kenya", country.get().name());

        verify(getRequestedFor(urlEqualTo("/name/Kenya")));
    }

    @Test
    void shouldThrowExceptionWhenCountryNotFound() {
        stubFor(get(urlEqualTo("/name/UnknownLand"))
                .willReturn(aResponse()
                        .withStatus(404)));

        assertThrows(CountryNotFoundException.class, () -> {
            countryApiClient.fetchCountryByName("UnknownLand");
        });

        verify(getRequestedFor(urlEqualTo("/name/UnknownLand")));
    }
}
