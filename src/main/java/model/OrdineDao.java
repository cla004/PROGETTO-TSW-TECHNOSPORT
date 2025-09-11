package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdineDao {

    // Inserisce un ordine nel database e recupera l'ID generato automaticamente
    public void inserisciOrdine(Ordine ordine) throws SQLException {
        String sql = "INSERT INTO orders(id_utente, id_indirizzo, id_metodo, totale, stato) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, ordine.getIdUtente());
            stmt.setInt(2, ordine.getIdIndirizzo());
            stmt.setInt(3, ordine.getIdMetodo());
            stmt.setDouble(4, ordine.getTotale());
            stmt.setString(5, ordine.getStato());
            stmt.executeUpdate();

            // Recupera l'ID generato automaticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ordine.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Recupera un ordine tramite ID
    public Ordine getOrdineById(int id) throws SQLException {
        String sql = "SELECT id, id_utente, id_indirizzo, id_metodo, totale, data_ordine, stato FROM orders WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Ordine ordine = new Ordine();
                    ordine.setId(rs.getInt("id"));
                    ordine.setIdUtente(rs.getInt("id_utente"));
                    ordine.setIdIndirizzo(rs.getInt("id_indirizzo"));
                    ordine.setIdMetodo(rs.getInt("id_metodo"));
                    ordine.setTotale(rs.getDouble("totale"));
                    ordine.setDataOrdine(rs.getTimestamp("data_ordine"));
                    ordine.setStato(rs.getString("stato"));
                    return ordine;
                }
            }
        }
        return null;
    }

    // Recupera un ordine con tutti i dettagli collegati (JOIN)
    public Ordine getOrdineByIdCompleto(int id) throws SQLException {
        String sql = "SELECT o.id, o.id_utente, o.id_indirizzo, o.id_metodo, o.totale, o.data_ordine, o.stato, " +
                     "u.name, u.email, " +
                     "i.via, i.citta, i.cap, i.provincia, i.paese, " +
                     "m.tipo, m.ultime_quattro_cifre, m.intestatario, m.scadenza, m.predefinito " +
                     "FROM orders o " +
                     "JOIN users u ON o.id_utente = u.id " +
                     "JOIN indirizzi i ON o.id_indirizzo = i.id " +
                     "JOIN metodi_pagamento m ON o.id_metodo = m.id " +
                     "WHERE o.id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Crea ordine
                    Ordine ordine = new Ordine();
                    ordine.setId(rs.getInt("id"));
                    ordine.setIdUtente(rs.getInt("id_utente"));
                    ordine.setIdIndirizzo(rs.getInt("id_indirizzo"));
                    ordine.setIdMetodo(rs.getInt("id_metodo"));
                    ordine.setTotale(rs.getDouble("totale"));
                    ordine.setDataOrdine(rs.getTimestamp("data_ordine"));
                    ordine.setStato(rs.getString("stato"));
                    
                    // Crea oggetti collegati
                    Utente utente = new Utente();
                    utente.setId(rs.getInt("id_utente"));
                    utente.setNome(rs.getString("name"));
                    utente.setEmail(rs.getString("email"));
                    ordine.setUtente(utente);
                    
                    Indirizzo indirizzo = new Indirizzo();
                    indirizzo.setId(rs.getInt("id_indirizzo"));
                    indirizzo.setVia(rs.getString("via"));
                    indirizzo.setCitta(rs.getString("citta"));
                    indirizzo.setCap(rs.getString("cap"));
                    indirizzo.setProvincia(rs.getString("provincia"));
                    indirizzo.setPaese(rs.getString("paese"));
                    ordine.setIndirizzo(indirizzo);
                    
                    MetodoPagamento metodo = new MetodoPagamento();
                    metodo.setId(rs.getInt("id_metodo"));
                    metodo.setTipo(rs.getString("tipo"));
                    metodo.setUltimeQuattroCifre(rs.getString("ultime_quattro_cifre"));
                    metodo.setIntestatario(rs.getString("intestatario"));
                    metodo.setScadenza(rs.getString("scadenza"));
                    metodo.setPredefinito(rs.getBoolean("predefinito"));
                    ordine.setMetodoPagamento(metodo);
                    
                    return ordine;
                }
            }
        }
        return null;
    }

    // Recupera tutti gli ordini di un utente
    public List<Ordine> getOrdiniByUtente(int userId) throws SQLException {
        List<Ordine> lista = new ArrayList<>();
        String sql = "SELECT id, id_utente, id_indirizzo, id_metodo, totale, data_ordine, stato FROM orders WHERE id_utente = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Ordine ordine = new Ordine();
                    ordine.setId(rs.getInt("id"));
                    ordine.setIdUtente(rs.getInt("id_utente"));
                    ordine.setIdIndirizzo(rs.getInt("id_indirizzo"));
                    ordine.setIdMetodo(rs.getInt("id_metodo"));
                    ordine.setTotale(rs.getDouble("totale"));
                    ordine.setDataOrdine(rs.getTimestamp("data_ordine"));
                    ordine.setStato(rs.getString("stato"));
                    lista.add(ordine);
                }
            }
        }
        return lista;
    }
    
    // Recupera tutti gli ordini
    public List<Ordine> getAllOrdini() throws SQLException {
        List<Ordine> lista = new ArrayList<>();
        String sql = "SELECT id, id_utente, id_indirizzo, id_metodo, totale, data_ordine, stato FROM orders ORDER BY data_ordine DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Ordine ordine = new Ordine();
                    ordine.setId(rs.getInt("id"));
                    ordine.setIdUtente(rs.getInt("id_utente"));
                    ordine.setIdIndirizzo(rs.getInt("id_indirizzo"));
                    ordine.setIdMetodo(rs.getInt("id_metodo"));
                    ordine.setTotale(rs.getDouble("totale"));
                    ordine.setDataOrdine(rs.getTimestamp("data_ordine"));
                    ordine.setStato(rs.getString("stato"));
                    lista.add(ordine);
                }
            }
        }
        return lista;
    }

    // Aggiorna lo stato di un ordine
    public void aggiornaStatoOrdine(int idOrdine, String nuovoStato) throws SQLException {
        String sql = "UPDATE orders SET stato = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuovoStato);
            stmt.setInt(2, idOrdine);
            stmt.executeUpdate();
        }
    }

    // Elimina un ordine
    public void eliminaOrdine(int id) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    // METODI PER STATISTICHE ADMIN
    
    /**
     * Conta gli ordini ricevuti oggi
     */
    public int contaOrdiniOggi() {
        String sql = "SELECT COUNT(*) as totale FROM orders WHERE DATE(data_ordine) = CURDATE()";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("totale");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Calcola le vendite del mese corrente
     */
    public double calcolaVenditeMese() {
        String sql = "SELECT SUM(totale) as totale FROM orders WHERE MONTH(data_ordine) = MONTH(CURDATE()) AND YEAR(data_ordine) = YEAR(CURDATE()) AND stato != 'ANNULLATO'";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("totale");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0.0;
    }
    
    /**
     * Conta ordini totali
     */
    public int contaTotaleOrdini() {
        String sql = "SELECT COUNT(*) as totale FROM orders";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("totale");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Calcola vendite totali
     */
    public double calcolaVenditeTotali() {
        String sql = "SELECT SUM(totale) as totale FROM orders WHERE stato != 'ANNULLATO'";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("totale");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0.0;
    }
}
