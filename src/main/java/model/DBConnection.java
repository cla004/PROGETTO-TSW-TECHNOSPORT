package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    
    private static final String URL = "jdbc:mysql://localhost:3306/eccomerce";
    private static final String USER = "root"; // Username del database
    private static final String PASSWORD = "Cavani2007!@"; // La  password del database

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