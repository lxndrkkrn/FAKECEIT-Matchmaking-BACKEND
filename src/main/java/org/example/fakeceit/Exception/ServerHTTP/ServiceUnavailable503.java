package org.example.fakeceit.Exception.ServerHTTP;

public class ServiceUnavailable503 extends RuntimeException {
    public ServiceUnavailable503(String message) {
        super(message);
    }
}
