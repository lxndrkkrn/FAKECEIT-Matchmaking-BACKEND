package org.example.fakeceit.Errors.ClientHTTP;

public class TooManyRequests429 extends RuntimeException {
    public TooManyRequests429(String message) {
        super(message);
    }
}
