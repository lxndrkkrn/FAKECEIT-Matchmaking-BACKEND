package org.example.fakeceit.Errors.ClientHTTP;

public class Unauthorized401 extends RuntimeException {
    public Unauthorized401(String message) {
        super(message);
    }
}
