package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DettaglioOrdineDao {

    // Inserisce un nuovo dettaglio ordine
    public void inserisciDettaglio(DettaglioOrdine dettaglio) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price, taglia_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, dettaglio.getOrderId());
            stmt.setInt(2, dettaglio.getProductId());
            stmt.setInt(3, dettaglio.getQuantity());
            stmt.setDouble(4, dettaglio.getPrice());
            stmt.setInt(5, dettaglio.getTagliaId());
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dettaglio.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Trova un dettaglio ordine per id
    public DettaglioOrdine findById(int id) throws SQLException {
        String sql = "SELECT id, order_id, product_id, quantity, price, taglia_id FROM order_items WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    DettaglioOrdine dettaglio = new DettaglioOrdine();
                    dettaglio.setId(rs.getInt("id"));
                    dettaglio.setOrderId(rs.getInt("order_id"));
                    dettaglio.setProductId(rs.getInt("product_id"));
                    dettaglio.setQuantity(rs.getInt("quantity"));
                    dettaglio.setPrice(rs.getDouble("price"));
                    dettaglio.setTagliaId(rs.getInt("taglia_id"));
                    return dettaglio;
                }
            }
        }
        return null;
    }

    // Trova tutti gli order_items di un ordine specifico
    public List<DettaglioOrdine> findByOrderId(int orderId) throws SQLException {
        List<DettaglioOrdine> dettagli = new ArrayList<>();
        String sql = "SELECT id, order_id, product_id, quantity, price, taglia_id FROM order_items WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DettaglioOrdine dettaglio = new DettaglioOrdine();
                    dettaglio.setId(rs.getInt("id"));
                    dettaglio.setOrderId(rs.getInt("order_id"));
                    dettaglio.setProductId(rs.getInt("product_id"));
                    dettaglio.setQuantity(rs.getInt("quantity"));
                    dettaglio.setPrice(rs.getDouble("price"));
                    dettaglio.setTagliaId(rs.getInt("taglia_id"));
                    dettagli.add(dettaglio);
                }
            }
        }
        return dettagli;
    }

    // Trova order_items con tutti i dettagli collegati (JOIN completo)
    public List<DettaglioOrdine> findByOrderIdCompleto(int orderId) throws SQLException {
        List<DettaglioOrdine> dettagli = new ArrayList<>();
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.price, oi.taglia_id, " +
                     "p.name, p.immagine, p.description, " +
                     "t.nome as taglia_nome " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "LEFT JOIN taglie t ON oi.taglia_id = t.id " +
                     "WHERE oi.order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DettaglioOrdine dettaglio = new DettaglioOrdine();
                    dettaglio.setId(rs.getInt("id"));
                    dettaglio.setOrderId(rs.getInt("order_id"));
                    dettaglio.setProductId(rs.getInt("product_id"));
                    dettaglio.setQuantity(rs.getInt("quantity"));
                    dettaglio.setPrice(rs.getDouble("price"));
                    dettaglio.setTagliaId(rs.getInt("taglia_id"));
                    
                    // Crea oggetto prodotto collegato
                    Prodotti prodotto = new Prodotti();
                    prodotto.setId_prodotto(rs.getInt("product_id"));
                    prodotto.setNome(rs.getString("name"));
                    prodotto.setImmagine(rs.getString("immagine"));
                    prodotto.setDescrizione(rs.getString("description"));
                    dettaglio.setProdotto(prodotto);
                    
                    // Crea oggetto taglia collegato (se presente)
                    if (rs.getInt("taglia_id") != 0) {
                        Taglia taglia = new Taglia();
                        taglia.setid_taglia(rs.getInt("taglia_id"));
                        taglia.setEtichetta(rs.getString("taglia_nome"));
                        dettaglio.setTaglia(taglia);
                    }
                    
                    dettagli.add(dettaglio);
                }
            }
        }
        return dettagli;
    }

    // Aggiorna un dettaglio ordine
    public void update(DettaglioOrdine dettaglio) throws SQLException {
        String sql = "UPDATE order_items SET quantity = ?, price = ?, taglia_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, dettaglio.getQuantity());
            stmt.setDouble(2, dettaglio.getPrice());
            stmt.setInt(3, dettaglio.getTagliaId());
            stmt.setInt(4, dettaglio.getId());
            stmt.executeUpdate();
        }
    }

    // Elimina un dettaglio ordine
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM order_items WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    // Elimina tutti i dettagli di un ordine
    public void deleteByOrderId(int orderId) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }
}
