package com.medisalud.repository;

import com.medisalud.entity.Paciente;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPacientesMemoria implements RepositorioPacientes {

    private final List<Paciente> pacientes = new ArrayList<>();

    @Override
    public synchronized void guardar(Paciente paciente) {
        pacientes.add(paciente);
    }

    @Override
    public synchronized Paciente buscarPorCodigo(String codigo) {
        for (Paciente paciente : pacientes) {
            if (paciente.getCodigo().equals(codigo)) {
                return paciente;
            }
        }
        return null;
    }

    @Override
    public synchronized List<Paciente> listarTodos() {
        return new ArrayList<>(pacientes);
    }
}
