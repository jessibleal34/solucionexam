package com.medisalud.entity;

/** Patron Builder: arma una HistoriaClinica con varios campos opcionales paso a paso. */
public class ConstructorHistoriaClinica {

    private String antecedentes;
    private String alergias;
    private String observaciones;

    public ConstructorHistoriaClinica conAntecedentes(String antecedentes) {
        this.antecedentes = antecedentes;
        return this;
    }

    public ConstructorHistoriaClinica conAlergias(String alergias) {
        this.alergias = alergias;
        return this;
    }

    public ConstructorHistoriaClinica conObservaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    public HistoriaClinica construir() {
        return new HistoriaClinica(antecedentes, alergias, observaciones);
    }
}
