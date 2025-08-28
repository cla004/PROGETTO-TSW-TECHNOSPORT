package model;

import model.Prodotti;

import java.sql.*;
import java.util.*;

public class ProdottiDao {

    public void inserisciProdotto(Prodotti p) {
        String sql = "INSERT INTO products (name, description, price, category_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getDescrizione());
            stmt.setDouble(3, p.getPrezzo());
            stmt.setInt(4, p.getId_categoria());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Prodotti cercaProdottoById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        Prodotti p = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                p = new Prodotti();
                p.setId_prodotto(rs.getInt("id"));
                p.setNome(rs.getString("name"));
                p.setDescrizione(rs.getString("description"));
                p.setPrezzo(rs.getDouble("price"));
                p.setId_categoria(rs.getInt("category_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

    public List<Prodotti> listaProdotti() {
        List<Prodotti> lista = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Prodotti p = new Prodotti();
                p.setId_prodotto(rs.getInt("id"));
                p.setNome(rs.getString("name"));
                p.setDescrizione(rs.getString("description"));
                p.setPrezzo(rs.getDouble("price"));
                p.setId_categoria(rs.getInt("category_id"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void aggiornaProdotto(Prodotti p) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, category_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getDescrizione());
            stmt.setDouble(3, p.getPrezzo());
            stmt.setInt(4, p.getId_categoria());
            stmt.setInt(5, p.getId_prodotto());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminaProdotto(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String getPhotoById(int id) throws SQLException {
        String sql = "SELECT immagine FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("immagine"); // restituisce il path dell'immagine
                }
            }
        }
        return null;
    }

    public String getNomeCategoriaById(int idCategoria) throws SQLException {
        String sql = "SELECT name FROM categories WHERE id = ?";
        try (Connection conn =DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        }
        return "null";
    
}
    
    public List<Prodotti> findByCategoriaId(int idCategoria) {
        List<Prodotti> lista = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCategoria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Prodotti p = new Prodotti();
                    p.setId_prodotto(rs.getInt("id"));
                    p.setNome(rs.getString("name"));
                    p.setDescrizione(rs.getString("description"));
                    p.setPrezzo(rs.getDouble("price"));
                    p.setId_categoria(rs.getInt("category_id"));
                    p.setImmagine(rs.getString("image")); // se hai il campo immagine nel DB
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}

