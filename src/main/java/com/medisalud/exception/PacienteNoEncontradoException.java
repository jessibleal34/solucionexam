package com.medisalud.exception;

public class PacienteNoEncontradoException extends Exception {

    public PacienteNoEncontradoException(String codigo) {
        super("No existe un paciente con el codigo " + codigo);
    }
}
