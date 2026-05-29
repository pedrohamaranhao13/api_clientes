package br.com.phamtecnologia.api_clientes.factories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection getConnection() throws SQLException {

        var host = "jdbc:postgresql://localhost:5432/bd_api_clientes";
        var user = "postgres";
        var pass = "root";

        return DriverManager.getConnection(host, user, pass);
    }
}
