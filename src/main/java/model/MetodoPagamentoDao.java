package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagamentoDao {

    // Inserisci un nuovo metodo di pagamento
    public void insert(MetodoPagamento metodo) throws SQLException {
        // Prima rimuovi predefinito da tutti gli altri metodi se questo è predefinito
        if (metodo.isPredefinito()) {
            rimuoviPredefinito(metodo.getIdUtente());
        }
        
        String sql = "INSERT INTO metodi_pagamento (id_utente, tipo, ultime_quattro_cifre, intestatario, scadenza, predefinito) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, metodo.getIdUtente());
            stmt.setString(2, metodo.getTipo());
            stmt.setString(3, metodo.getUltimeQuattroCifre());
            stmt.setString(4, metodo.getIntestatario());
            stmt.setString(5, metodo.getScadenza());
            stmt.setBoolean(6, metodo.isPredefinito());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    metodo.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Aggiorna un metodo di pagamento esistente
    public void update(MetodoPagamento metodo) throws SQLException {
        // Se questo diventa predefinito, rimuovi predefinito da tutti gli altri
        if (metodo.isPredefinito()) {
            rimuoviPredefinito(metodo.getIdUtente());
        }
        
        String sql = "UPDATE metodi_pagamento SET tipo = ?, ultime_quattro_cifre = ?, intestatario = ?, scadenza = ?, predefinito = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, metodo.getTipo());
            stmt.setString(2, metodo.getUltimeQuattroCifre());
            stmt.setString(3, metodo.getIntestatario());
            stmt.setString(4, metodo.getScadenza());
            stmt.setBoolean(5, metodo.isPredefinito());
            stmt.setInt(6, metodo.getId());
            stmt.executeUpdate();
        }
    }

    // Cancella un metodo di pagamento per id
    public void delete(int metodoId) throws SQLException {
        String sql = "DELETE FROM metodi_pagamento WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, metodoId);
            stmt.executeUpdate();
        }
    }

    // Trova un metodo di pagamento per id
    public MetodoPagamento findById(int metodoId) throws SQLException {
        String sql = "SELECT id, id_utente, tipo, ultime_quattro_cifre, intestatario, scadenza, predefinito FROM metodi_pagamento WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, metodoId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MetodoPagamento metodo = new MetodoPagamento();
                    metodo.setId(rs.getInt("id"));
                    metodo.setIdUtente(rs.getInt("id_utente"));
                    metodo.setTipo(rs.getString("tipo"));
                    metodo.setUltimeQuattroCifre(rs.getString("ultime_quattro_cifre"));
                    metodo.setIntestatario(rs.getString("intestatario"));
                    metodo.setScadenza(rs.getString("scadenza"));
                    metodo.setPredefinito(rs.getBoolean("predefinito"));
                    return metodo;
                }
            }
        }
        return null;
    }

    // Trova tutti i metodi di pagamento di un utente
    public List<MetodoPagamento> findByUserId(int userId) throws SQLException {
        List<MetodoPagamento> metodi = new ArrayList<>();
        String sql = "SELECT id, id_utente, tipo, ultime_quattro_cifre, intestatario, scadenza, predefinito FROM metodi_pagamento WHERE id_utente = ? ORDER BY predefinito DESC, id DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MetodoPagamento metodo = new MetodoPagamento();
                    metodo.setId(rs.getInt("id"));
                    metodo.setIdUtente(rs.getInt("id_utente"));
                    metodo.setTipo(rs.getString("tipo"));
                    metodo.setUltimeQuattroCifre(rs.getString("ultime_quattro_cifre"));
                    metodo.setIntestatario(rs.getString("intestatario"));
                    metodo.setScadenza(rs.getString("scadenza"));
                    metodo.setPredefinito(rs.getBoolean("predefinito"));
                    metodi.add(metodo);
                }
            }
        }
        return metodi;
    }

    // Trova tutti i metodi di pagamento con i dettagli utente (con JOIN)
    public List<MetodoPagamento> findAllWithUserDetails() throws SQLException {
        List<MetodoPagamento> metodi = new ArrayList<>();
        String sql = "SELECT m.id, m.id_utente, m.tipo, m.ultime_quattro_cifre, m.intestatario, m.scadenza, m.predefinito, " +
                     "u.name, u.email " +
                     "FROM metodi_pagamento m " +
                     "JOIN users u ON m.id_utente = u.id";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MetodoPagamento metodo = new MetodoPagamento();
                    metodo.setId(rs.getInt("id"));
                    metodo.setIdUtente(rs.getInt("id_utente"));
                    metodo.setTipo(rs.getString("tipo"));
                    metodo.setUltimeQuattroCifre(rs.getString("ultime_quattro_cifre"));
                    metodo.setIntestatario(rs.getString("intestatario"));
                    metodo.setScadenza(rs.getString("scadenza"));
                    metodo.setPredefinito(rs.getBoolean("predefinito"));

                    // Crea oggetto utente collegato
                    Utente utente = new Utente();
                    utente.setId(rs.getInt("id_utente"));
                    utente.setNome(rs.getString("name"));
                    utente.setEmail(rs.getString("email"));
                    metodo.setUtente(utente);

                    metodi.add(metodo);
                }
            }
        }
        return metodi;
    }

    // Conta quanti metodi di pagamento ha un utente
    public int countByUserId(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM metodi_pagamento WHERE id_utente = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }

    // Trova metodi di pagamento per tipo (es. "Visa", "MasterCard", "PayPal")
    public List<MetodoPagamento> findByTipo(String tipo) throws SQLException {
        List<MetodoPagamento> metodi = new ArrayList<>();
        String sql = "SELECT id, id_utente, tipo, ultime_quattro_cifre, intestatario, scadenza, predefinito FROM metodi_pagamento WHERE tipo = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MetodoPagamento metodo = new MetodoPagamento();
                    metodo.setId(rs.getInt("id"));
                    metodo.setIdUtente(rs.getInt("id_utente"));
                    metodo.setTipo(rs.getString("tipo"));
                    metodo.setUltimeQuattroCifre(rs.getString("ultime_quattro_cifre"));
                    metodo.setIntestatario(rs.getString("intestatario"));
                    metodo.setScadenza(rs.getString("scadenza"));
                    metodo.setPredefinito(rs.getBoolean("predefinito"));
                    metodi.add(metodo);
                }
            }
        }
        return metodi;
    }

    // Verifica se un utente ha già un metodo di pagamento con le stesse ultime quattro cifre
    public boolean existsByUserAndLastFourDigits(int userId, String ultimeQuattroCifre) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM metodi_pagamento WHERE id_utente = ? AND ultime_quattro_cifre = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, ultimeQuattroCifre);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        }
        return false;
    }
    
    // Metodo helper per rimuovere il flag predefinito da tutti i metodi dell'utente
    private void rimuoviPredefinito(int userId) throws SQLException {
        String sql = "UPDATE metodi_pagamento SET predefinito = false WHERE id_utente = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
    
    // Trova il metodo di pagamento predefinito dell'utente
    public MetodoPagamento findPredefinito(int userId) throws SQLException {
        String sql = "SELECT id, id_utente, tipo, ultime_quattro_cifre, intestatario, scadenza, predefinito FROM metodi_pagamento WHERE id_utente = ? AND predefinito = true";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MetodoPagamento metodo = new MetodoPagamento();
                    metodo.setId(rs.getInt("id"));
                    metodo.setIdUtente(rs.getInt("id_utente"));
                    metodo.setTipo(rs.getString("tipo"));
                    metodo.setUltimeQuattroCifre(rs.getString("ultime_quattro_cifre"));
                    metodo.setIntestatario(rs.getString("intestatario"));
                    metodo.setScadenza(rs.getString("scadenza"));
                    metodo.setPredefinito(rs.getBoolean("predefinito"));
                    return metodo;
                }
            }
        }
        return null;
    }
    
    // Imposta un metodo di pagamento come predefinito
    public void setPredefinito(int metodoId, int userId) throws SQLException {
        // Prima rimuovi predefinito da tutti gli altri metodi dell'utente
        rimuoviPredefinito(userId);
        
        // Poi imposta questo metodo come predefinito
        String sql = "UPDATE metodi_pagamento SET predefinito = true WHERE id = ? AND id_utente = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, metodoId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }
}
