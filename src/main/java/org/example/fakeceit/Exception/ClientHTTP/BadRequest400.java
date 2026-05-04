package org.example.fakeceit.Exception.ClientHTTP;

public class BadRequest400 extends RuntimeException {
    public BadRequest400(String message) {
        super(message);
    }
}
