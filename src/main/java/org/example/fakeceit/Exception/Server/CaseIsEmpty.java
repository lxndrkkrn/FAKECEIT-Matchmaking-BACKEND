package org.example.fakeceit.Errors.Server;

public class CaseIsEmpty extends RuntimeException {
    public CaseIsEmpty(String message) {
        super(message);
    }
}
