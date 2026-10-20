package com.medisalud.entity;

public class Medico extends Persona {

    private String especialidad;

    public Medico(String nombreCompleto, String codigo, String especialidad) {
        super(nombreCompleto, codigo);
        setEspecialidad(especialidad);
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.trim().isEmpty()) {
            throw new IllegalArgumentException("La especialidad no puede estar vacia");
        }
        this.especialidad = especialidad;
    }

    @Override
    public String toString() {
        return super.toString() + " (medico, " + especialidad + ")";
    }
}
