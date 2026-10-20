package com.medisalud.repository;

import com.medisalud.entity.Paciente;
import java.util.List;

public interface RepositorioPacientes {

    void guardar(Paciente paciente);

    Paciente buscarPorCodigo(String codigo);

    List<Paciente> listarTodos();
}
