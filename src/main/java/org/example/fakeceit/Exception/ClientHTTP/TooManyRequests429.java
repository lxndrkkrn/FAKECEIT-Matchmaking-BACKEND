package org.example.fakeceit.Exception.ClientHTTP;

public class TooManyRequests429 extends RuntimeException {
    public TooManyRequests429(String message) {
        super(message);
    }
}
