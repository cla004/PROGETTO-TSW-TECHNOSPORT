package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IndirizzoDao {

    // Inserisci un nuovo indirizzo
    public void insert(Indirizzo indirizzo) throws SQLException {
        String sql = "INSERT INTO indirizzi (id_utente, via, citta, cap, provincia, paese) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, indirizzo.getIdUtente());
            stmt.setString(2, indirizzo.getVia());
            stmt.setString(3, indirizzo.getCitta());
            stmt.setString(4, indirizzo.getCap());
            stmt.setString(5, indirizzo.getProvincia());
            stmt.setString(6, indirizzo.getPaese());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    indirizzo.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Aggiorna un indirizzo esistente
    public void update(Indirizzo indirizzo) throws SQLException {
        String sql = "UPDATE indirizzi SET via = ?, citta = ?, cap = ?, provincia = ?, paese = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, indirizzo.getVia());
            stmt.setString(2, indirizzo.getCitta());
            stmt.setString(3, indirizzo.getCap());
            stmt.setString(4, indirizzo.getProvincia());
            stmt.setString(5, indirizzo.getPaese());
            stmt.setInt(6, indirizzo.getId());
            stmt.executeUpdate();
        }
    }

    // Cancella un indirizzo per id
    public void delete(int indirizzoId) throws SQLException {
        String sql = "DELETE FROM indirizzi WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, indirizzoId);
            stmt.executeUpdate();
        }
    }

    // Trova un indirizzo per id
    public Indirizzo findById(int indirizzoId) throws SQLException {
        String sql = "SELECT id, id_utente, via, citta, cap, provincia, paese FROM indirizzi WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, indirizzoId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Indirizzo indirizzo = new Indirizzo();
                    indirizzo.setId(rs.getInt("id"));
                    indirizzo.setIdUtente(rs.getInt("id_utente"));
                    indirizzo.setVia(rs.getString("via"));
                    indirizzo.setCitta(rs.getString("citta"));
                    indirizzo.setCap(rs.getString("cap"));
                    indirizzo.setProvincia(rs.getString("provincia"));
                    indirizzo.setPaese(rs.getString("paese"));
                    return indirizzo;
                }
            }
        }
        return null;
    }

    // Trova tutti gli indirizzi di un utente
    public List<Indirizzo> findByUserId(int userId) throws SQLException {
        List<Indirizzo> indirizzi = new ArrayList<>();
        String sql = "SELECT id, id_utente, via, citta, cap, provincia, paese FROM indirizzi WHERE id_utente = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Indirizzo indirizzo = new Indirizzo();
                    indirizzo.setId(rs.getInt("id"));
                    indirizzo.setIdUtente(rs.getInt("id_utente"));
                    indirizzo.setVia(rs.getString("via"));
                    indirizzo.setCitta(rs.getString("citta"));
                    indirizzo.setCap(rs.getString("cap"));
                    indirizzo.setProvincia(rs.getString("provincia"));
                    indirizzo.setPaese(rs.getString("paese"));
                    indirizzi.add(indirizzo);
                }
            }
        }
        return indirizzi;
    }

    // Trova tutti gli indirizzi con i dettagli utente (con JOIN)
    public List<Indirizzo> findAllWithUserDetails() throws SQLException {
        List<Indirizzo> indirizzi = new ArrayList<>();
        String sql = "SELECT i.id, i.id_utente, i.via, i.citta, i.cap, i.provincia, i.paese, " +
                     "u.name, u.email " +
                     "FROM indirizzi i " +
                     "JOIN users u ON i.id_utente = u.id";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Indirizzo indirizzo = new Indirizzo();
                    indirizzo.setId(rs.getInt("id"));
                    indirizzo.setIdUtente(rs.getInt("id_utente"));
                    indirizzo.setVia(rs.getString("via"));
                    indirizzo.setCitta(rs.getString("citta"));
                    indirizzo.setCap(rs.getString("cap"));
                    indirizzo.setProvincia(rs.getString("provincia"));
                    indirizzo.setPaese(rs.getString("paese"));

                    // Crea oggetto utente collegato
                    Utente utente = new Utente();
                    utente.setId(rs.getInt("id_utente"));
                    utente.setNome(rs.getString("name"));
                    utente.setEmail(rs.getString("email"));
                    indirizzo.setUtente(utente);

                    indirizzi.add(indirizzo);
                }
            }
        }
        return indirizzi;
    }

    // Conta quanti indirizzi ha un utente
    public int countByUserId(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM indirizzi WHERE id_utente = ?";
        
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
}
