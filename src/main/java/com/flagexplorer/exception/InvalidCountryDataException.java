package com.flagexplorer.exception;

public class InvalidCountryDataException extends RuntimeException{
    public InvalidCountryDataException(String message) {
        super(message);
    }

    public InvalidCountryDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
