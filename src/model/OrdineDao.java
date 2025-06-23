package dao;

import model.Ordine;
import java.sql.*;
import java.util.*;

public class OrdineDAO {

    public void inserisciOrdine(Ordine ordine) {
        String sql = "INSERT INTO ordini (user_id, totale, stato) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ordine.getUser_Id());
            stmt.setDouble(2, ordine.getTotale());
            stmt.setString(3, ordine.getStato());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Ordine cercaOrdineById(int id) {
        String sql = "SELECT * FROM ordini WHERE id = ?";
        Ordine ordine = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ordine = new Ordine(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getDouble("totale"),
                    rs.getString("stato")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordine;
    }

    public List<Ordine> listaOrdini() {
        List<Ordine> ordini = new ArrayList<>();
        String sql = "SELECT * FROM ordini";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Ordine ordine = new Ordine(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getDouble("totale"),
                    rs.getString("stato")
                );
                ordini.add(ordine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordini;
    }

    public void aggiornaOrdine(Ordine ordine) {
        String sql = "UPDATE ordini SET user_id = ?, totale = ?, stato = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ordine.getUser_Id());
            stmt.setDouble(2, ordine.getTotale());
            stmt.setString(3, ordine.getStato());
            stmt.setInt(4, ordine.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminaOrdine(int id) {
        String sql = "DELETE FROM ordini WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
