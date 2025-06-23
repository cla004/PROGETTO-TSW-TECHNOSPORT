package dao;

import java.sql.*;
import java.util.*;
import model.Categoria;

public class CategoriaDAO {
    public void inserisciCategoria(Categoria c) {
        String sql = "INSERT INTO categorie (nome) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getNome());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Categoria cercaCategoriaById(int id) {
        String sql = "SELECT * FROM categorie WHERE id = ?";
        Categoria c = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return c;
    }

    public List<Categoria> listaCategorie() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorie";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                lista.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public void aggiornaCategoria(Categoria c) {
        String sql = "UPDATE categorie SET nome = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getNome());
            stmt.setInt(2, c.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminaCategoria(int id) {
        String sql = "DELETE FROM categorie WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}