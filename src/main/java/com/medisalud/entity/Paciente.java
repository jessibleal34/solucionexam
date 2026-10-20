package com.medisalud.entity;

public class Paciente extends Persona {

    private int edad;
    private final HistoriaClinica historiaClinica;

    public Paciente(String nombreCompleto, String codigo, int edad) {
        this(nombreCompleto, codigo, edad, new HistoriaClinica());
    }

    public Paciente(String nombreCompleto, String codigo, int edad, HistoriaClinica historiaClinica) {
        super(nombreCompleto, codigo);
        setEdad(edad);
        this.historiaClinica = historiaClinica;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        if (edad < 0) {
            throw new IllegalArgumentException("La edad no puede ser negativa");
        }
        this.edad = edad;
    }

    public HistoriaClinica getHistoriaClinica() {
        return historiaClinica;
    }

    @Override
    public String toString() {
        return super.toString() + " (paciente, " + edad + " anios)";
    }
}
