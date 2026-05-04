package org.example.fakeceit.Errors.ClientHTTP;

public class BadRequest400 extends RuntimeException {
    public BadRequest400(String message) {
        super(message);
    }
}
