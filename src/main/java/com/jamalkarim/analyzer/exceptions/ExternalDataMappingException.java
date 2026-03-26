package com.jamalkarim.analyzer.exceptions;

/**
 * Thrown when data from an external source (like the NFL API) cannot be 
 * correctly mapped to the internal domain model.
 */
public class ExternalDataMappingException extends RuntimeException {
    public ExternalDataMappingException(String message) {
        super(message);
    }
}
