package com.medisalud.vista;

import com.medisalud.controlador.ControladorMediSalud;
import com.medisalud.entity.Cita;
import com.medisalud.entity.ConstructorHistoriaClinica;
import com.medisalud.entity.Factura;
import com.medisalud.entity.FacturaConDescuentoAfiliado;
import com.medisalud.entity.FacturaConRecargoNocturno;
import com.medisalud.entity.HistoriaClinica;
import com.medisalud.entity.Medico;
import com.medisalud.entity.Paciente;
import com.medisalud.exception.CitaNoEncontradaException;
import com.medisalud.exception.MedicoNoEncontradoException;
import com.medisalud.exception.PacienteNoEncontradoException;
import com.medisalud.exception.TransicionInvalidaException;
import com.medisalud.service.EstrategiaCosto;
import com.medisalud.service.EstrategiaCostoConsultaEspecialista;
import com.medisalud.service.EstrategiaCostoConsultaGeneral;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VistaConsola {

    private final ControladorMediSalud controlador;
    private final Scanner lector = new Scanner(System.in);

    public VistaConsola(ControladorMediSalud controlador) {
        this.controlador = controlador;
    }

    public void mostrarMenu() {
        boolean continuar = true;
        while (continuar) {
            System.out.println();
            System.out.println("=== Menu principal - MediSalud ===");
            System.out.println("1. Registrar paciente");
            System.out.println("2. Registrar medico");
            System.out.println("3. Agendar cita");
            System.out.println("4. Consultar historia clinica");
            System.out.println("5. Facturar consulta");
            System.out.println("6. Generar reporte");
            System.out.println("7. Salir");
            System.out.print("Elegir una opcion: ");
            String opcion = lector.nextLine();

            switch (opcion) {
                case "1":
                    registrarPaciente();
                    break;
                case "2":
                    registrarMedico();
                    break;
                case "3":
                    agendarCita();
                    break;
                case "4":
                    consultarHistoriaClinica();
                    break;
                case "5":
                    facturarConsulta();
                    break;
                case "6":
                    generarReporte();
                    break;
                case "7":
                    continuar = false;
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        }
        System.out.println("Cerrando el Sistema de Gestion de Citas Medicas MediSalud. Hasta pronto.");
    }

    private void registrarPaciente() {
        try {
            System.out.print("Nombre completo: ");
            String nombreCompleto = lector.nextLine();
            System.out.print("Codigo: ");
            String codigo = lector.nextLine();
            System.out.print("Edad: ");
            int edad = Integer.parseInt(lector.nextLine());

            System.out.print("Desea registrar antecedentes y alergias? (s/n): ");
            Paciente paciente;
            if (lector.nextLine().equalsIgnoreCase("s")) {
                System.out.print("Antecedentes: ");
                String antecedentes = lector.nextLine();
                System.out.print("Alergias: ");
                String alergias = lector.nextLine();
                HistoriaClinica historia = new ConstructorHistoriaClinica()
                        .conAntecedentes(antecedentes)
                        .conAlergias(alergias)
                        .construir();
                paciente = new Paciente(nombreCompleto, codigo, edad, historia);
            } else {
                paciente = new Paciente(nombreCompleto, codigo, edad);
            }

            controlador.registrarPaciente(paciente);
            System.out.println("Paciente registrado: " + paciente);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("No se pudo registrar el paciente: " + excepcion.getMessage());
        }
    }

    private void registrarMedico() {
        try {
            System.out.print("Nombre completo: ");
            String nombreCompleto = lector.nextLine();
            System.out.print("Codigo: ");
            String codigo = lector.nextLine();
            System.out.print("Especialidad: ");
            String especialidad = lector.nextLine();

            Medico medico = new Medico(nombreCompleto, codigo, especialidad);
            controlador.registrarMedico(medico);
            System.out.println("Medico registrado: " + medico);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("No se pudo registrar el medico: " + excepcion.getMessage());
        }
    }

    private void agendarCita() {
        try {
            System.out.print("Codigo de la cita: ");
            String codigoCita = lector.nextLine();
            System.out.print("Codigo del paciente: ");
            String codigoPaciente = lector.nextLine();
            System.out.print("Codigo del medico: ");
            String codigoMedico = lector.nextLine();
            System.out.print("Fecha (aaaa-mm-dd): ");
            LocalDate fecha = LocalDate.parse(lector.nextLine());

            Cita cita = controlador.agendarCita(codigoCita, codigoPaciente, codigoMedico, fecha);
            System.out.println("Cita agendada: " + cita);

            System.out.print("Confirmar la cita ahora? (s/n): ");
            if (lector.nextLine().equalsIgnoreCase("s")) {
                controlador.confirmarCita(codigoCita);
                System.out.print("Registrar atencion con diagnostico? (s/n): ");
                if (lector.nextLine().equalsIgnoreCase("s")) {
                    System.out.print("Diagnostico: ");
                    String diagnostico = lector.nextLine();
                    controlador.atenderCita(codigoCita, diagnostico);
                }
                System.out.println("Estado actualizado: " + controlador.buscarCita(codigoCita).getEstado());
            }
        } catch (PacienteNoEncontradoException | MedicoNoEncontradoException
                | CitaNoEncontradaException | TransicionInvalidaException | DateTimeParseException excepcion) {
            System.out.println("No se pudo agendar la cita: " + excepcion.getMessage());
        }
    }

    private void consultarHistoriaClinica() {
        try {
            System.out.print("Codigo del paciente: ");
            String codigoPaciente = lector.nextLine();
            HistoriaClinica historia = controlador.consultarHistoriaClinica(codigoPaciente);
            System.out.println(historia);
            if (historia.getConsultas().isEmpty()) {
                System.out.println("Sin consultas registradas todavia.");
            }
            for (String consulta : historia.getConsultas()) {
                System.out.println("- " + consulta);
            }
        } catch (PacienteNoEncontradoException excepcion) {
            System.out.println("No se pudo consultar: " + excepcion.getMessage());
        }
    }

    private void facturarConsulta() {
        try {
            System.out.print("Codigo de la cita: ");
            String codigoCita = lector.nextLine();
            System.out.print("Tipo de consulta (G=general, E=especialista): ");
            String tipo = lector.nextLine();
            EstrategiaCosto estrategiaCosto = tipo.equalsIgnoreCase("E")
                    ? new EstrategiaCostoConsultaEspecialista()
                    : new EstrategiaCostoConsultaGeneral();

            Factura factura = controlador.facturarCita(codigoCita, estrategiaCosto);

            System.out.print("Es paciente afiliado? (s/n): ");
            if (lector.nextLine().equalsIgnoreCase("s")) {
                factura = new FacturaConDescuentoAfiliado(factura);
            }
            System.out.print("Es horario nocturno? (s/n): ");
            if (lector.nextLine().equalsIgnoreCase("s")) {
                factura = new FacturaConRecargoNocturno(factura);
            }

            System.out.println(factura);
        } catch (CitaNoEncontradaException excepcion) {
            System.out.println("No se pudo facturar: " + excepcion.getMessage());
        }
    }

    private void generarReporte() {
        try {
            System.out.print("Codigo del medico para ver sus citas: ");
            String codigoMedico = lector.nextLine();
            List<Cita> citas = controlador.generarReportePorMedico(codigoMedico);
            imprimirTitulo("Citas del medico " + codigoMedico + " (" + citas.size() + ")");
            if (citas.isEmpty()) {
                System.out.println("  Sin citas registradas.");
            } else {
                String formato = "  %-6s | %-24s | %-10s | %-10s%n";
                System.out.printf(formato, "Cita", "Paciente", "Fecha", "Estado");
                System.out.println("  " + "-".repeat(7) + "+" + "-".repeat(26) + "+" + "-".repeat(12) + "+" + "-".repeat(11));
                for (Cita cita : citas) {
                    System.out.printf(formato, cita.getCodigo(), cita.getPaciente().getNombreCompleto(),
                            cita.getFecha(), cita.getEstado());
                }
            }

            Map<String, Double> totalPorMedico = controlador.generarReporteFacturacionPorMedico();
            imprimirTitulo("Total facturado por medico");
            if (totalPorMedico.isEmpty()) {
                System.out.println("  Sin facturas emitidas.");
            } else {
                double granTotal = 0;
                for (Map.Entry<String, Double> fila : totalPorMedico.entrySet()) {
                    System.out.printf("  %-10s | $%,12.2f%n", fila.getKey(), fila.getValue());
                    granTotal += fila.getValue();
                }
                System.out.printf("  %-10s | $%,12.2f%n", "TOTAL", granTotal);
            }

            System.out.print("Edad minima para el reporte de pacientes: ");
            int edadMinima = Integer.parseInt(lector.nextLine());
            List<Paciente> mayores = controlador.listarPacientesMayoresDeEdad(edadMinima);
            imprimirTitulo("Pacientes de " + edadMinima + " anios o mas (" + mayores.size() + ")");
            if (mayores.isEmpty()) {
                System.out.println("  Ningun paciente cumple el criterio.");
            } else {
                String formato = "  %-8s | %-26s | %4s%n";
                System.out.printf(formato, "Codigo", "Nombre", "Edad");
                System.out.println("  " + "-".repeat(9) + "+" + "-".repeat(28) + "+" + "-".repeat(6));
                for (Paciente paciente : mayores) {
                    System.out.printf(formato, paciente.getCodigo(), paciente.getNombreCompleto(), paciente.getEdad());
                }
            }
            System.out.println();
        } catch (NumberFormatException excepcion) {
            System.out.println("Edad invalida: " + excepcion.getMessage());
        }
    }

    private void imprimirTitulo(String titulo) {
        System.out.println();
        System.out.println("=== " + titulo + " ===");
    }
}
