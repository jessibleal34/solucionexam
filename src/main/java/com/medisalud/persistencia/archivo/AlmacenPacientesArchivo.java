package com.medisalud.persistencia.archivo;

import com.medisalud.entity.Paciente;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlmacenPacientesArchivo {

    private final String rutaArchivo;

    public AlmacenPacientesArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public void guardarTodos(List<Paciente> pacientes) throws IOException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Paciente paciente : pacientes) {
                String linea = paciente.getCodigo() + "|" + paciente.getNombreCompleto() + "|" + paciente.getEdad();
                escritor.write(linea);
                escritor.newLine();
            }
        }
    }

    public List<Paciente> cargarTodos() throws IOException {
        List<Paciente> pacientes = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return pacientes;
        }
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea = lector.readLine();
            while (linea != null) {
                String[] campos = linea.split("\\|");
                String codigo = campos[0];
                String nombreCompleto = campos[1];
                int edad = Integer.parseInt(campos[2]);
                pacientes.add(new Paciente(nombreCompleto, codigo, edad));
                linea = lector.readLine();
            }
        }
        return pacientes;
    }
}
