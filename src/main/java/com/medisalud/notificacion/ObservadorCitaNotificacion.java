package com.medisalud.notificacion;

import com.medisalud.entity.Cita;

public class ObservadorCitaNotificacion implements ObservadorCita {

    private final HiloNotificaciones hiloNotificaciones;

    public ObservadorCitaNotificacion(HiloNotificaciones hiloNotificaciones) {
        this.hiloNotificaciones = hiloNotificaciones;
    }

    @Override
    public void notificarCambioEstado(Cita cita) {
        hiloNotificaciones.encolar("La cita " + cita.getCodigo() + " ahora esta " + cita.getEstado());
    }
}
