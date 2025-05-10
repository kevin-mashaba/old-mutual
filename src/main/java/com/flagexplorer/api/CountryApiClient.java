package com.flagexplorer.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.flagexplorer.api.config.RestCountriesConfig;
import com.flagexplorer.exception.CountryNotFoundException;
import com.flagexplorer.model.Country;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
@Component
public class CountryApiClient {

    private final WebClient webClient;
    //private final RestCountriesConfig config;

    public CountryApiClient(WebClient webClient) {
        this.webClient = webClient;
    }
    public List<Country> fetchAllCountries() {
        return webClient.get()
                .uri("/all")
                .retrieve()
                .bodyToFlux(JsonNode.class)
                .map(this::mapToCountry)
                .collectList()
                .block();
    }

    public Optional<Country> fetchCountryByName(String name) {
        return webClient.get()
                .uri("/name/{name}", name)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    if (response.statusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new CountryNotFoundException(name));
                    }
                    return response.createException();
                })
                .bodyToFlux(JsonNode.class)
                .map(this::mapToCountry)
                .next()
                .blockOptional();
    }

    private Country mapToCountry(JsonNode node) {
        String name = node.path("name").path("common").asText();
        String code = node.path("cca2").asText();

        JsonNode capitalNode = node.path("capital");
        String capital = (capitalNode.isArray() && capitalNode.size() > 0)
                ? capitalNode.get(0).asText()
                : "N/A";

        long population = node.path("population").asLong();
        String flag = "https://flagcdn.com/" + code.toLowerCase() + ".svg";

        return new Country(name, code, capital, population, flag);
    }
}
