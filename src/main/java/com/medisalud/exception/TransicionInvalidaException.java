package com.medisalud.exception;

import com.medisalud.entity.EstadoCita;

public class TransicionInvalidaException extends Exception {

    public TransicionInvalidaException(String codigoCita, EstadoCita estadoActual, EstadoCita estadoNuevo) {
        super("La cita " + codigoCita + " no puede pasar de " + estadoActual + " a " + estadoNuevo);
    }
}
