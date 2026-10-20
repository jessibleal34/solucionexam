package com.medisalud.exception;

public class CitaNoEncontradaException extends Exception {

    public CitaNoEncontradaException(String codigo) {
        super("No existe una cita con el codigo " + codigo);
    }
}
