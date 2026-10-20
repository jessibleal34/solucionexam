package com.medisalud.service;

import com.medisalud.entity.Cita;

public class EstrategiaCostoConsultaEspecialista implements EstrategiaCosto {

    private static final double COSTO_CONSULTA_ESPECIALISTA = 90.0;

    @Override
    public double calcularCosto(Cita cita) {
        return COSTO_CONSULTA_ESPECIALISTA;
    }
}
