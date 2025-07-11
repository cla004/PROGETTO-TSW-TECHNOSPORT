package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Configura le tue credenziali del database
    // IMPORTANTISSIMO: Cambia "tecnosport", "root", "Milan" con i tuoi valori reali
    private static final String URL = "jdbc:mysql://localhost:3306/eccomerce";
    private static final String USER = "root"; // Il tuo username del database
    private static final String PASSWORD = "Milan"; // La tua password del database

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Errore: Driver JDBC di MySQL non trovato.");
            throw new SQLException("Driver JDBC di MySQL non trovato.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura della connessione al database: " + e.getMessage());
            }
        }
    }
}