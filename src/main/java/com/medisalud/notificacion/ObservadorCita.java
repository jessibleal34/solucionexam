package com.medisalud.notificacion;

import com.medisalud.entity.Cita;

/** Patron Observer: Cita avisa a sus observadores cuando cambia de estado, sin conocer como lo usan. */
public interface ObservadorCita {

    void notificarCambioEstado(Cita cita);
}
