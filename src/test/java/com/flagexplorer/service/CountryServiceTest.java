package com.flagexplorer.service;

import com.flagexplorer.api.CountryApiClient;
import com.flagexplorer.exception.CountryNotFoundException;
import com.flagexplorer.model.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTest {

    @Mock
    private CountryApiClient countryApiClient;


    @InjectMocks
    private CountryService countryService;

    @Test
    void shouldReturnAllCountriesFromApiClient() {
        List<Country> expected = List.of(
                new Country("Kenya", "KE", "Nairobi", 53000000, "https://flagcdn.com/ke.svg"),
                new Country("Japan", "JP", "Tokyo", 126000000, "https://flagcdn.com/jp.svg")
        );

        when(countryApiClient.fetchAllCountries()).thenReturn(expected);

        List<Country> result = countryService.getAllCountries();

        assertEquals(2, result.size());
        assertEquals("KE", result.get(0).code());
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
