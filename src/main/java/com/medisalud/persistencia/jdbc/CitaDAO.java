package com.medisalud.persistencia.jdbc;

import com.medisalud.entity.Cita;
import com.medisalud.entity.EstadoCita;
import com.medisalud.entity.Medico;
import com.medisalud.entity.Paciente;
import com.medisalud.repository.RepositorioCitas;
import com.medisalud.repository.RepositorioMedicos;
import com.medisalud.repository.RepositorioPacientes;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO implements RepositorioCitas {

    private final RepositorioPacientes repositorioPacientes;
    private final RepositorioMedicos repositorioMedicos;

    public CitaDAO(RepositorioPacientes repositorioPacientes, RepositorioMedicos repositorioMedicos) {
        this.repositorioPacientes = repositorioPacientes;
        this.repositorioMedicos = repositorioMedicos;
    }

    @Override
    public void guardar(Cita cita) {
        String sql = "INSERT INTO citas (codigo, paciente_codigo, medico_codigo, fecha, estado) "
                + "VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE estado = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, cita.getCodigo());
            sentencia.setString(2, cita.getPaciente().getCodigo());
            sentencia.setString(3, cita.getMedico().getCodigo());
            sentencia.setDate(4, Date.valueOf(cita.getFecha()));
            sentencia.setString(5, cita.getEstado().name());
            sentencia.setString(6, cita.getEstado().name());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw new RuntimeException("No se pudo guardar la cita " + cita.getCodigo(), excepcion);
        }
    }

    @Override
    public Cita buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, paciente_codigo, medico_codigo, fecha, estado FROM citas WHERE codigo = ?";
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, codigo);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return mapearCita(resultado);
                }
                return null;
            }
        } catch (SQLException excepcion) {
            throw new RuntimeException("No se pudo buscar la cita " + codigo, excepcion);
        }
    }

    @Override
    public List<Cita> listarTodas() {
        String sql = "SELECT codigo, paciente_codigo, medico_codigo, fecha, estado FROM citas";
        return listarConFiltro(sql, null);
    }

    @Override
    public List<Cita> listarPorMedico(String codigoMedico) {
        String sql = "SELECT codigo, paciente_codigo, medico_codigo, fecha, estado FROM citas "
                + "WHERE medico_codigo = ?";
        return listarConFiltro(sql, codigoMedico);
    }

    private List<Cita> listarConFiltro(String sql, String codigoMedico) {
        List<Cita> citas = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            if (codigoMedico != null) {
                sentencia.setString(1, codigoMedico);
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    citas.add(mapearCita(resultado));
                }
            }
        } catch (SQLException excepcion) {
            throw new RuntimeException("No se pudo listar las citas", excepcion);
        }
        return citas;
    }

    private Cita mapearCita(ResultSet resultado) throws SQLException {
        String codigo = resultado.getString("codigo");
        Paciente paciente = repositorioPacientes.buscarPorCodigo(resultado.getString("paciente_codigo"));
        Medico medico = repositorioMedicos.buscarPorCodigo(resultado.getString("medico_codigo"));
        LocalDate fecha = resultado.getDate("fecha").toLocalDate();
        Cita cita = new Cita(codigo, paciente, medico, fecha);
        cita.cambiarEstado(EstadoCita.valueOf(resultado.getString("estado")));
        return cita;
    }
}
