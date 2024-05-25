package org.example.exceptions;

public class InvalidArguments extends RuntimeException {
    public InvalidArguments(String message) {
        super(message);
    }
}
