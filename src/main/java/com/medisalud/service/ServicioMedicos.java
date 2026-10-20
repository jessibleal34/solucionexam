package com.medisalud.service;

import com.medisalud.entity.Medico;
import com.medisalud.exception.MedicoNoEncontradoException;
import com.medisalud.repository.RepositorioMedicos;
import java.util.List;

public class ServicioMedicos {

    private final RepositorioMedicos repositorioMedicos;

    public ServicioMedicos(RepositorioMedicos repositorioMedicos) {
        this.repositorioMedicos = repositorioMedicos;
    }

    public void registrarMedico(Medico medico) {
        repositorioMedicos.guardar(medico);
    }

    public Medico buscarPorCodigo(String codigo) throws MedicoNoEncontradoException {
        Medico medico = repositorioMedicos.buscarPorCodigo(codigo);
        if (medico == null) {
            throw new MedicoNoEncontradoException(codigo);
        }
        return medico;
    }

    public List<Medico> listarTodos() {
        return repositorioMedicos.listarTodos();
    }
}
