package org.example.fakeceit.Errors.Client;

public class InvalidValue extends RuntimeException {
    public InvalidValue(String message) {
        super(message);
    }
}
