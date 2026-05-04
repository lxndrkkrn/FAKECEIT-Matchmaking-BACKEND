package org.example.fakeceit.Errors.Client;

public class IncorrectName extends RuntimeException {
    public IncorrectName(String message) {
        super(message);
    }
}
