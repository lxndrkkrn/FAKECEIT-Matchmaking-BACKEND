package org.example.fakeceit.Errors.Server;

public class ImpossibleContract extends RuntimeException {
    public ImpossibleContract(String message) {
        super(message);
    }
}
