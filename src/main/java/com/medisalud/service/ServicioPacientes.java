package com.medisalud.service;

import com.medisalud.entity.Paciente;
import com.medisalud.exception.PacienteNoEncontradoException;
import com.medisalud.repository.RepositorioPacientes;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServicioPacientes {

    private final RepositorioPacientes repositorioPacientes;

    public ServicioPacientes(RepositorioPacientes repositorioPacientes) {
        this.repositorioPacientes = repositorioPacientes;
    }

    public void registrarPaciente(Paciente paciente) {
        repositorioPacientes.guardar(paciente);
    }

    public Paciente buscarPorCodigo(String codigo) throws PacienteNoEncontradoException {
        Paciente paciente = repositorioPacientes.buscarPorCodigo(codigo);
        if (paciente == null) {
            throw new PacienteNoEncontradoException(codigo);
        }
        return paciente;
    }

    public List<Paciente> listarTodos() {
        return repositorioPacientes.listarTodos();
    }

    public List<Paciente> listarMayoresDeEdad(int edadMinima) {
        Predicate<Paciente> esMayorDeEdad = paciente -> paciente.getEdad() >= edadMinima;
        return repositorioPacientes.listarTodos().stream()
                .filter(esMayorDeEdad)
                .collect(Collectors.toList());
    }
}
