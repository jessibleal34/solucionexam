package com.medisalud.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HistoriaClinica implements Serializable {

    private static final long serialVersionUID = 1L;

    private String antecedentes;
    private String alergias;
    private String observaciones;
    private final List<String> consultas = new ArrayList<>();

    public HistoriaClinica() {
    }

    public HistoriaClinica(String antecedentes, String alergias, String observaciones) {
        this.antecedentes = antecedentes;
        this.alergias = alergias;
        this.observaciones = observaciones;
    }

    public void agregarConsulta(String diagnostico) {
        consultas.add(diagnostico);
    }

    public List<String> getConsultas() {
        return consultas;
    }

    public String getAntecedentes() {
        return antecedentes;
    }

    public String getAlergias() {
        return alergias;
    }

    public String getObservaciones() {
        return observaciones;
    }

    @Override
    public String toString() {
        return "Historia clinica: " + consultas.size() + " consulta(s) registrada(s)";
    }
}
