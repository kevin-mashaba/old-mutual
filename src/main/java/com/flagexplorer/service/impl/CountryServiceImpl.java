package com.flagexplorer.service.impl;

import com.flagexplorer.api.CountryApiClient;
import com.flagexplorer.exception.CountryNotFoundException;
import com.flagexplorer.model.Country;
import com.flagexplorer.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryApiClient countryApiClient;

    @Override
    public List<Country> getAllCountries() {
        return countryApiClient.fetchAllCountries();
    }

    @Override
    public Country getCountryByName(String name) {
        return countryApiClient.fetchCountryByName(name)
                .orElseThrow(() -> new CountryNotFoundException(name));
    }
}
