package com.cesarfrade.ats.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String mensaje) {
        super(mensaje);
    }
}
