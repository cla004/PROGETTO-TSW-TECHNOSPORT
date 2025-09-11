package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per la gestione della tabella payments
 */
public class PaymentDao {

    // Inserisce un nuovo pagamento
    public int inserisciPagamento(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (order_id, metodo_id, paid_at) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, payment.getOrderId());
            stmt.setInt(2, payment.getMetodoId());
            stmt.setTimestamp(3, payment.getPaidAt());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        payment.setId(id);
                        return id;
                    }
                }
            }
        }
        return -1;
    }

    // Ottiene un pagamento per ID
    public Payment getPaymentById(int id) throws SQLException {
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Payment(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getInt("metodo_id"),
                        rs.getTimestamp("paid_at")
                    );
                }
            }
        }
        return null;
    }

    // Ottiene un pagamento per order_id
    public Payment getPaymentByOrderId(int orderId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Payment(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getInt("metodo_id"),
                        rs.getTimestamp("paid_at")
                    );
                }
            }
        }
        return null;
    }

    // Aggiorna un pagamento
    public boolean aggiornaPagamento(Payment payment) throws SQLException {
        String sql = "UPDATE payments SET order_id = ?, metodo_id = ?, paid_at = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payment.getOrderId());
            stmt.setInt(2, payment.getMetodoId());
            stmt.setTimestamp(3, payment.getPaidAt());
            stmt.setInt(4, payment.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }

    // Elimina un pagamento
    public boolean eliminaPagamento(int id) throws SQLException {
        String sql = "DELETE FROM payments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Recupera tutti i pagamenti
    public List<Payment> getTuttiIPagamenti() throws SQLException {
        List<Payment> lista = new ArrayList<>();
        String sql = "SELECT * FROM payments ORDER BY paid_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Payment payment = new Payment(
                    rs.getInt("id"),
                    rs.getInt("order_id"),
                    rs.getInt("metodo_id"),
                    rs.getTimestamp("paid_at")
                );
                lista.add(payment);
            }
        }
        return lista;
    }

    // Recupera tutti i pagamenti per un utente specifico (tramite ordini)
    public List<Payment> getPagamentiByUserId(int userId) throws SQLException {
        List<Payment> lista = new ArrayList<>();
        String sql = "SELECT p.* FROM payments p " +
                    "JOIN orders o ON p.order_id = o.id " +
                    "WHERE o.id_utente = ? " +
                    "ORDER BY p.paid_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Payment payment = new Payment(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getInt("metodo_id"),
                        rs.getTimestamp("paid_at")
                    );
                    lista.add(payment);
                }
            }
        }
        return lista;
    }

    // Verifica se un ordine è già stato pagato
    public boolean isOrdinePagato(int orderId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM payments WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}

