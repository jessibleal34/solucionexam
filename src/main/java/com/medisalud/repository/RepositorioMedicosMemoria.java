package com.medisalud.repository;

import com.medisalud.entity.Medico;
import java.util.ArrayList;
import java.util.List;

public class RepositorioMedicosMemoria implements RepositorioMedicos {

    private final List<Medico> medicos = new ArrayList<>();

    @Override
    public synchronized void guardar(Medico medico) {
        medicos.add(medico);
    }

    @Override
    public synchronized Medico buscarPorCodigo(String codigo) {
        for (Medico medico : medicos) {
            if (medico.getCodigo().equals(codigo)) {
                return medico;
            }
        }
        return null;
    }

    @Override
    public synchronized List<Medico> listarTodos() {
        return new ArrayList<>(medicos);
    }
}
