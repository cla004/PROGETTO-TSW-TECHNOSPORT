package model;



import java.sql.*;
import java.util.*;
import model.Categoria;

public class CategoriaDao {
    public void inserisciCategoria(Categoria c) {
        String sql = "INSERT INTO categories (nome) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getnome_recensione());
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
                c.setId_categoria(rs.getInt("id"));
                c.setnome_recensione(rs.getString("nome"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return c;
    }

    public List<Categoria> listaCategorie() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId_categoria(rs.getInt("id"));
                c.setnome_recensione(rs.getString("name"));
                lista.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public void aggiornaCategoria(Categoria c) {
        String sql = "UPDATE categories SET nome = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getnome_recensione());
            stmt.setInt(2, c.getid_categoria());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminaCategoria(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
