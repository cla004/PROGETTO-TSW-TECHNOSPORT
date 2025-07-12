package model;

import java.sql.*;
import java.util.*;
import model.Taglia;

public class TagliaDAO {
    public void inserisciTaglia(Taglia t) {
        String sql = "INSERT INTO taglie (nome) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getEtichetta());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Taglia cercaTagliaById(int id) {
        String sql = "SELECT * FROM taglie WHERE id = ?";
        Taglia t = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                t = new Taglia();
                t.setid_taglia(rs.getInt("id"));
                t.setEtichetta(rs.getString("nome"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return t;
    }

    public List<Taglia> listaTaglie() {
        List<Taglia> lista = new ArrayList<>();
        String sql = "SELECT * FROM taglie";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Taglia t = new Taglia();
                t.setid_taglia(rs.getInt("id"));
                t.setEtichetta(rs.getString("nome"));
                lista.add(t);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public void aggiornaTaglia(Taglia t) {
        String sql = "UPDATE taglie SET nome = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getEtichetta());
            stmt.setInt(2, t.getid_taglia());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminaTaglia(int id) {
        String sql = "DELETE FROM taglie WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}