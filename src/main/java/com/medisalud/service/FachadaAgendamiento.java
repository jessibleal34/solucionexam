package com.medisalud.service;

import com.medisalud.entity.Cita;
import com.medisalud.entity.Medico;
import com.medisalud.entity.Paciente;
import com.medisalud.exception.MedicoNoEncontradoException;
import com.medisalud.exception.PacienteNoEncontradoException;
import com.medisalud.notificacion.ObservadorCita;
import java.time.LocalDate;

/** Patron Facade: un unico metodo coordina buscar paciente, buscar medico, crear la cita y conectarla con su observador. */
public class FachadaAgendamiento {

    private final ServicioPacientes servicioPacientes;
    private final ServicioMedicos servicioMedicos;
    private final ServicioCitas servicioCitas;
    private final ObservadorCita observadorCita;

    public FachadaAgendamiento(ServicioPacientes servicioPacientes,
                                ServicioMedicos servicioMedicos,
                                ServicioCitas servicioCitas,
                                ObservadorCita observadorCita) {
        this.servicioPacientes = servicioPacientes;
        this.servicioMedicos = servicioMedicos;
        this.servicioCitas = servicioCitas;
        this.observadorCita = observadorCita;
    }

    public Cita agendar(String codigoCita, String codigoPaciente, String codigoMedico, LocalDate fecha)
            throws PacienteNoEncontradoException, MedicoNoEncontradoException {
        Paciente paciente = servicioPacientes.buscarPorCodigo(codigoPaciente);
        Medico medico = servicioMedicos.buscarPorCodigo(codigoMedico);
        Cita cita = servicioCitas.agendarCita(codigoCita, paciente, medico, fecha);
        cita.agregarObservador(observadorCita);
        return cita;
    }
}
