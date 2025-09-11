package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarrelloDao {

    // Crea un nuovo carrello per un utente
    public void createCart(Carrello cart) throws SQLException {
        String sql = "INSERT INTO cart (user_id) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, cart.getUserId());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cart.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Trova carrello di un utente
    public Carrello findByUserId(int userId) throws SQLException {
        String sql = "SELECT id, user_id, created_at FROM cart WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Carrello cart = new Carrello();
                    cart.setId(rs.getInt("id"));
                    cart.setUserId(rs.getInt("user_id"));
                    cart.setCreatedAt(rs.getTimestamp("created_at"));
                    return cart;
                }
            }
        }
        return null;
    }

    // Trova carrello per id
    public Carrello findById(int cartId) throws SQLException {
        String sql = "SELECT id, user_id, created_at FROM cart WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Carrello cart = new Carrello();
                    cart.setId(rs.getInt("id"));
                    cart.setUserId(rs.getInt("user_id"));
                    cart.setCreatedAt(rs.getTimestamp("created_at"));
                    return cart;
                }
            }
        }
        return null;
    }

    // Elimina carrello
    public void deleteCart(int cartId) throws SQLException {
        String sql = "DELETE FROM cart WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartId);
            stmt.executeUpdate();
        }
    }
}
