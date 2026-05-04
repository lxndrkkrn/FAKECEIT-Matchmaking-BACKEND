package org.example.fakeceit.Errors.ClientHTTP;

public class Forbidden403 extends RuntimeException {
    public Forbidden403(String message) {
        super(message);
    }
}
