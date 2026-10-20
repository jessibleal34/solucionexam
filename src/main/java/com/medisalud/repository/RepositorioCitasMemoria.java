package com.medisalud.repository;

import com.medisalud.entity.Cita;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositorioCitasMemoria implements RepositorioCitas {

    private final Map<String, Cita> citasPorCodigo = new HashMap<>();

    @Override
    public synchronized void guardar(Cita cita) {
        citasPorCodigo.put(cita.getCodigo(), cita);
    }

    @Override
    public synchronized Cita buscarPorCodigo(String codigo) {
        return citasPorCodigo.get(codigo);
    }

    @Override
    public synchronized List<Cita> listarTodas() {
        return new ArrayList<>(citasPorCodigo.values());
    }

    @Override
    public synchronized List<Cita> listarPorMedico(String codigoMedico) {
        List<Cita> resultado = new ArrayList<>();
        for (Cita cita : citasPorCodigo.values()) {
            if (cita.getMedico().getCodigo().equals(codigoMedico)) {
                resultado.add(cita);
            }
        }
        return resultado;
    }
}
