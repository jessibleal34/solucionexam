package com.medisalud.persistencia.jdbc;

import com.medisalud.entity.Medico;
import com.medisalud.repository.RepositorioMedicos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO implements RepositorioMedicos {

    @Override
    public void guardar(Medico medico) {
        String sql = "INSERT INTO medicos (codigo, nombre_completo, especialidad) VALUES (?, ?, ?)";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, medico.getCodigo());
            sentencia.setString(2, medico.getNombreCompleto());
            sentencia.setString(3, medico.getEspecialidad());
            sentencia.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException excepcion) {
            throw new IllegalArgumentException("Ya existe un medico con el codigo " + medico.getCodigo());
        } catch (SQLException excepcion) {
            throw new RuntimeException("No se pudo guardar el medico " + medico.getCodigo(), excepcion);
        }
    }

    @Override
    public Medico buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, nombre_completo, especialidad FROM medicos WHERE codigo = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, codigo);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return mapearMedico(resultado);
                }
                return null;
            }
        } catch (SQLException excepcion) {
            throw new RuntimeException("No se pudo buscar el medico " + codigo, excepcion);
        }
    }

    @Override
    public List<Medico> listarTodos() {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT codigo, nombre_completo, especialidad FROM medicos";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                medicos.add(mapearMedico(resultado));
            }
        } catch (SQLException excepcion) {
            throw new RuntimeException("No se pudo listar los medicos", excepcion);
        }
        return medicos;
    }

    private Medico mapearMedico(ResultSet resultado) throws SQLException {
        String codigo = resultado.getString("codigo");
        String nombreCompleto = resultado.getString("nombre_completo");
        String especialidad = resultado.getString("especialidad");
        return new Medico(nombreCompleto, codigo, especialidad);
    }
}
