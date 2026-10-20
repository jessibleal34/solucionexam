package com.medisalud.entity;

import com.medisalud.notificacion.ObservadorCita;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String codigo;
    private final Paciente paciente;
    private final Medico medico;
    private final LocalDate fecha;
    private EstadoCita estado;
    private String motivoCancelacion;
    private transient List<ObservadorCita> observadores = new ArrayList<>();

    public Cita(String codigo, Paciente paciente, Medico medico, LocalDate fecha) {
        this.codigo = codigo;
        this.paciente = paciente;
        this.medico = medico;
        this.fecha = fecha;
        this.estado = EstadoCita.PENDIENTE;

    }

    public void agregarObservador(ObservadorCita observador) {
        observadores.add(observador);
    }

    public void cambiarEstado(EstadoCita nuevoEstado) {
        this.estado = nuevoEstado;
        for (ObservadorCita observador : observadores) {
            observador.notificarCambioEstado(this);
        }
    }

    public void cancelarConMotivo(String motivo) {
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El motivo de cancelación es obligatorio."
            );
        }

        if (motivo.length() > 200) {
            throw new IllegalArgumentException(
                    "El motivo de cancelación no puede superar los 200 caracteres."
            );
        }

        this.motivoCancelacion = motivo.trim();
        cambiarEstado(EstadoCita.CANCELADA);
    }

    public String getCodigo() {
        return codigo;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    @Override
    public String toString() {
        String motivo = motivoCancelacion == null
                ? "Sin motivo registrado"
                : motivoCancelacion;

        return "Cita " + codigo + " [" + estado + "] "
                + paciente.getNombreCompleto()
                + " con " + medico.getNombreCompleto()
                + " el " + fecha
                + " | Motivo: "
                + (estado == EstadoCita.CANCELADA ? motivo : "");
    }

    private void readObject(ObjectInputStream entrada) throws IOException, ClassNotFoundException {
        entrada.defaultReadObject();
        observadores = new ArrayList<>();
    }
}
