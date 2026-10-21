package com.medisalud.service;

import com.medisalud.entity.Cita;
import com.medisalud.entity.EstadoCita;
import com.medisalud.entity.Medico;
import com.medisalud.entity.Paciente;
import com.medisalud.exception.CitaNoEncontradaException;
import com.medisalud.exception.TransicionInvalidaException;
import com.medisalud.repository.RepositorioCitas;
import java.time.LocalDate;
import java.util.List;
import com.medisalud.exception.CancelacionTardiaException;

public class ServicioCitas {

    private final RepositorioCitas repositorioCitas;

    public ServicioCitas(RepositorioCitas repositorioCitas) {
        this.repositorioCitas = repositorioCitas;
    }

    public Cita agendarCita(String codigo, Paciente paciente, Medico medico, LocalDate fecha) {
        Cita cita = new Cita(codigo, paciente, medico, fecha);
        repositorioCitas.guardar(cita);
        return cita;
    }

    public Cita buscarPorCodigo(String codigo) throws CitaNoEncontradaException {
        Cita cita = repositorioCitas.buscarPorCodigo(codigo);
        if (cita == null) {
            throw new CitaNoEncontradaException(codigo);
        }
        return cita;
    }

    public void confirmarCita(String codigo) throws CitaNoEncontradaException, TransicionInvalidaException {
        cambiarEstado(codigo, EstadoCita.CONFIRMADA);
    }

    public void atenderCita(String codigo, String diagnostico)
            throws CitaNoEncontradaException, TransicionInvalidaException {
        Cita cita = cambiarEstado(codigo, EstadoCita.ATENDIDA);
        cita.getPaciente().getHistoriaClinica().agregarConsulta(diagnostico);
    }




    public Cita cancelarCita(String codigo, String motivo)
            throws CitaNoEncontradaException,
            TransicionInvalidaException,
            CancelacionTardiaException {

        Cita cita = buscarPorCodigo(codigo);

        // Validar el motivo antes de modificar la cita.
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El motivo de cancelación es obligatorio."
            );
        }

        if (motivo.trim().length() > 200) {
            throw new IllegalArgumentException(
                    "El motivo no puede superar los 200 caracteres."
            );
        }

        // No permitir cancelar citas de fechas anteriores a hoy.
        if (cita.getFecha().isBefore(LocalDate.now())) {
            throw new CancelacionTardiaException(
                    cita.getCodigo(), cita.getFecha()
            );
        }

        // Reutilizar las reglas del patrón State.
        if (!cita.getEstado().puedeTransicionarA(EstadoCita.CANCELADA)) {
            throw new TransicionInvalidaException(
                    codigo, cita.getEstado(), EstadoCita.CANCELADA
            );
        }

        // El método de dominio guarda el motivo y notifica el cambio.
        cita.cancelarConMotivo(motivo);

        repositorioCitas.guardar(cita);

        return cita;
    }


    public List<Cita> listarPorMedico(String codigoMedico) {
        return repositorioCitas.listarPorMedico(codigoMedico);
    }

    private Cita cambiarEstado(String codigo, EstadoCita nuevoEstado)
            throws CitaNoEncontradaException, TransicionInvalidaException {
        Cita cita = buscarPorCodigo(codigo);
        if (!cita.getEstado().puedeTransicionarA(nuevoEstado)) {
            throw new TransicionInvalidaException(codigo, cita.getEstado(), nuevoEstado);
        }
        cita.cambiarEstado(nuevoEstado);
        repositorioCitas.guardar(cita);
        return cita;
    }
}
