package org.example.fakeceit.Errors.ClientHTTP;

public class NotFound404 extends RuntimeException {
    public NotFound404(String message) {
        super(message);
    }
}
