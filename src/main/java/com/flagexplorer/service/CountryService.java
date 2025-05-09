package com.flagexplorer.service;

import com.flagexplorer.model.Country;

import java.util.List;

public interface CountryService {
    List<Country> getAllCountries();
    Country getCountryByName(String name);
}
