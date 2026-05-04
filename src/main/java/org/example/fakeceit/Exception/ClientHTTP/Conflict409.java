package org.example.fakeceit.Errors.ClientHTTP;

public class Conflict409 extends RuntimeException {
    public Conflict409(String message) {
        super(message);
    }
}
