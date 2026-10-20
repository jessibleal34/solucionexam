package com.medisalud.repository;

import com.medisalud.entity.Medico;
import java.util.List;

public interface RepositorioMedicos {

    void guardar(Medico medico);

    Medico buscarPorCodigo(String codigo);

    List<Medico> listarTodos();
}
