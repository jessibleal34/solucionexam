package com.medisalud.service;

import com.medisalud.entity.Cita;

/** Patron Strategy: variantes intercambiables para calcular el costo de una cita. */
public interface EstrategiaCosto {

    double calcularCosto(Cita cita);
}
