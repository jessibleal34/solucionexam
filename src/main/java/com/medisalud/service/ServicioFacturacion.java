package com.medisalud.service;

import com.medisalud.entity.Cita;
import com.medisalud.entity.Factura;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServicioFacturacion {

    private final List<Factura> facturasEmitidas = new ArrayList<>();

    public Factura emitirFactura(Cita cita, EstrategiaCosto estrategiaCosto) {
        double montoBase = estrategiaCosto.calcularCosto(cita);
        Factura factura = new Factura(cita, montoBase);
        facturasEmitidas.add(factura);
        return factura;
    }

    public List<Factura> listarFacturas() {
        return new ArrayList<>(facturasEmitidas);
    }

    public Map<String, Double> totalFacturadoPorMedico() {
        return facturasEmitidas.stream()
                .collect(Collectors.groupingBy(
                        factura -> factura.getCita().getMedico().getCodigo(),
                        Collectors.summingDouble(Factura::calcularMonto)));
    }
}
