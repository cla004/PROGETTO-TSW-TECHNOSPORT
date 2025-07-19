package model;

import model.Prodotti;

import java.sql.*;
import java.util.*;

public class ProdottiDao {

    public void inserisciProdotto(Prodotti p) {
        String sql = "INSERT INTO prodotti (nome, descrizione, prezzo, categoria_id) VALUES (?, ?, ?, ?)";
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
        String sql = "SELECT * FROM prodotti WHERE id = ?";
        Prodotti p = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                p = new Prodotti();
                p.setId_prodotto(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setDescrizione(rs.getString("descrizione"));
                p.setPrezzo(rs.getDouble("prezzo"));
                p.setId_categoria(rs.getInt("categoria_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

    public List<Prodotti> listaProdotti() {
        List<Prodotti> lista = new ArrayList<>();
        String sql = "SELECT * FROM prodotti";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Prodotti p = new Prodotti();
                p.setId_prodotto(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setDescrizione(rs.getString("descrizione"));
                p.setPrezzo(rs.getDouble("prezzo"));
                p.setId_categoria(rs.getInt("categoria_id"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void aggiornaProdotto(Prodotti p) {
        String sql = "UPDATE prodotti SET nome = ?, descrizione = ?, prezzo = ?, categoria_id = ? WHERE id = ?";
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
        String sql = "DELETE FROM prodotti WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public byte[] getPhotoById(int id) throws SQLException {
        String sql = "SELECT immagine FROM prodotti WHERE id = ?";
        try (Connection conn =DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("immagine"); // restituisce il BLOB come array di byte
                }
            }
        }
        return null;
    }   
    
    public String getNomeCategoriaById(int idCategoria) throws SQLException {
        String sql = "SELECT nome_categoria FROM categorie WHERE id_categoria = ?";
        try (Connection conn =DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nome_categoria");
                }
            }
        }
        return "null";
    
}
}