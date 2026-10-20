package com.medisalud.repository;

import com.medisalud.entity.Cita;
import java.util.List;

public interface RepositorioCitas {

    void guardar(Cita cita);

    Cita buscarPorCodigo(String codigo);

    List<Cita> listarTodas();

    List<Cita> listarPorMedico(String codigoMedico);
}
