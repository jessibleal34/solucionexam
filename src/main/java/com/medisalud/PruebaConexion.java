
package com.medisalud;

import com.medisalud.persistencia.jdbc.ConexionBD;
import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            System.out.println("¡Conexión exitosa con MySQL!");
            System.out.println("Base de datos: " + conexion.getCatalog());
            System.out.println("Conexión válida: " + conexion.isValid(3));
        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
