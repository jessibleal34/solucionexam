
package com.medisalud.exception;

import java.time.LocalDate;

public class CancelacionTardiaException extends Exception {

    public CancelacionTardiaException(String codigoCita, LocalDate fechaCita) {
        super("No se puede cancelar la cita " + codigoCita
                + " porque su fecha ya pasó: " + fechaCita);
    }
}


