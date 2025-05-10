package com.flagexplorer.controller;

import com.flagexplorer.exception.CountryNotFoundException;
import com.flagexplorer.model.Country;
import com.flagexplorer.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(CountryController.class)
public class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Test
    void shouldReturnAllCountries() throws Exception {
        List<Country> mockCountries = List.of(
                new Country("Kenya", "KE", "Nairobi", 53000000, "https://flagcdn.com/ke.svg"),
                new Country("Germany", "DE", "Berlin", 83000000, "https://flagcdn.com/de.svg")
        );

        when(countryService.getAllCountries()).thenReturn(mockCountries);

        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Kenya"))
                .andExpect(jsonPath("$[0].code").value("KE"))
                .andExpect(jsonPath("$[1].capital").value("Berlin"))
                .andExpect(jsonPath("$[1].flagUrl").value("https://flagcdn.com/de.svg"));
    }


    @Test
    void shouldReturnCountryByName_whenCountryExists() throws Exception {
        Country country = new Country("Kenya", "KE", "Nairobi", 53771300L, "https://flagcdn.com/ke.svg");
        when(countryService.getCountryByName("Kenya")).thenReturn(country);

        mockMvc.perform(get("/countries/Kenya"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Kenya"))
                .andExpect(jsonPath("$.code").value("KE"));
    }

    @Test
    void shouldReturn404_whenCountryNotFound() throws Exception {
        when(countryService.getCountryByName("Narnia"))
                .thenThrow(new CountryNotFoundException("Narnia"));

        mockMvc.perform(get("/countries/Narnia"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Country not found: Narnia"));
    }
}
