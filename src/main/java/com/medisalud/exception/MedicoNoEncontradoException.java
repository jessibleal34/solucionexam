package com.medisalud.exception;

public class MedicoNoEncontradoException extends Exception {

    public MedicoNoEncontradoException(String codigo) {
        super("No existe un medico con el codigo " + codigo);
    }
}
