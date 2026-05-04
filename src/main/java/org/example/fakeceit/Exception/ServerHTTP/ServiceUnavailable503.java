package org.example.fakeceit.Errors.ServerHTTP;

public class ServiceUnavailable503 extends RuntimeException {
    public ServiceUnavailable503(String message) {
        super(message);
    }
}
