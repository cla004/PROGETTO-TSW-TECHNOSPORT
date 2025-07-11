package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAO {

    public void inserisciUtente(Utente utente) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utente.getNome());   
            stmt.setString(2, utente.getEmail());
            stmt.setString(3, utente.getPassword());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Utente cercaUtenteById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        Utente utente = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                utente = new Utente();
                utente.setId(rs.getInt("id"));
                utente.setName(rs.getString("name"));
                utente.setEmail(rs.getString("email"));
                utente.setPassword(rs.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return utente;
    }

    public List<Utente> listaUtenti() {
        String sql = "SELECT * FROM users";
        List<Utente> lista = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Utente utente = new Utente();
                utente.setId(rs.getInt("id"));
                utente.setName(rs.getString("name"));
                utente.setEmail(rs.getString("email"));
                utente.setPassword(rs.getString("password"));
                lista.add(utente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void aggiornaUtente(Utente utente) {
        String sql = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utente.getName());
            stmt.setString(2, utente.getEmail());
            stmt.setString(3, utente.getPassword());
            stmt.setInt(4, utente.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminaUtente(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean emailEsiste(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verify(String passwordInput, String storedHash) {
        String inputHash = PasswordHashing.toHash(passwordInput);
        return inputHash.equals(storedHash);
    }
}