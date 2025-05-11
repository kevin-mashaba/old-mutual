package com.flagexplorer.service.impl;

import com.flagexplorer.api.CountryApiClient;
import com.flagexplorer.exception.CountryNotFoundException;
import com.flagexplorer.model.Country;
import com.flagexplorer.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.EnableCaching;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@EnableCaching
@SpringBootTest
public class CountryServiceTest {

    @MockBean
    private CountryApiClient countryApiClient;


    @Autowired
    private CountryService countryService;

    @Test
    void shouldReturnAllCountriesFromApiClient() {
        List<Country> expected = List.of(
                new Country("Kenya", "KE", "Nairobi", 53000000, "https://flagcdn.com/ke.svg"),
                new Country("Japan", "JP", "Tokyo", 126000000, "https://flagcdn.com/jp.svg")
        );

        when(countryApiClient.fetchAllCountries()).thenReturn(expected);

        List<Country> firstCall = countryService.getAllCountries();
        assertEquals(2, firstCall.size());
        assertEquals("KE", firstCall.get(0).code());

        List<Country> secondCall = countryService.getAllCountries();
        assertEquals(2, secondCall.size());

        verify(countryApiClient, times(1)).fetchAllCountries();
    }

    @Test
    void shouldReturnCountry_whenFound() {
        Country country = new Country("Kenya", "KE", "Nairobi", 53771300L, "https://flagcdn.com/ke.svg");
        when(countryApiClient.fetchCountryByName("Kenya")).thenReturn(Optional.of(country));

        Country result = countryService.getCountryByName("Kenya");
        assertEquals("Kenya", result.name());
    }

    @Test
    void shouldThrowException_whenNotFound() {
        when(countryApiClient.fetchCountryByName("Narnia")).thenReturn(Optional.empty());

        assertThrows(CountryNotFoundException.class,
                () -> countryService.getCountryByName("Narnia"));
    }
}
