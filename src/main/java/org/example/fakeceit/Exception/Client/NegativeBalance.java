package org.example.fakeceit.Exception.Client;

public class NegativeBalance extends RuntimeException {
    public NegativeBalance(String message) {
        super(message);
    }
}
