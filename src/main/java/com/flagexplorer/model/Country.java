package com.flagexplorer.model;

public record Country(String name,
                      String code,
                      String capital,
                      long population,
                      String flagUrl) {
}
