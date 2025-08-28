package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Ordine;
import model.Utente;

public class OrdineDao {
    private Connection conn;

    public OrdineDao(Connection conn) {
        this.conn = conn;
    }

    // Inserisce un ordine nel database e recupera l'ID generato automaticamente
    public void inserisciOrdine(Ordine ordine) throws SQLException {
        String sql = "INSERT INTO orders(user_id, totale, stato, data) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, ordine.getUser_Id().getId());
            stmt.setDouble(2, ordine.getTotale());
            stmt.setString(3, ordine.getStato());
            stmt.setDate(4, new java.sql.Date(ordine.getData().getTime()));
            stmt.executeUpdate();
          
            // Recupera l'ID generato automaticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ordine.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
    }

    // Recupera un ordine tramite ID
    public Ordine getOrdineById(int id) throws SQLException {
        String sql = "SELECT * FROM ordine WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UtenteDao utenteDAO = new UtenteDao();
                    Utente utente = utenteDAO.cercaUtenteById(rs.getInt("user_id"));

                    double totale = rs.getDouble("totale");
                    String stato = rs.getString("stato");
                    Date data = rs.getDate("data");

                    return new Ordine(id, utente, totale, stato, data);
                }
            }
        }
        return null;
    }

    // Recupera tutti gli ordini di un utente
    public List<Ordine> getOrdiniByUtente(int Id) throws SQLException {
        List<Ordine> lista = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Id);
            try (ResultSet rs = stmt.executeQuery()) {
                UtenteDao utentedao = new UtenteDao();
                Utente utente = utentedao.cercaUtenteById(Id);

                while (rs.next()) {
                    int id = rs.getInt("id");
                    double totale = rs.getDouble("totale");
                    String stato = rs.getString("stato");
                    Date data = rs.getDate("data");

                    Ordine ordine = new Ordine(id, utente, totale, stato, data);
                    lista.add(ordine);
                }
            }
        }
        return lista;
    }

    // Aggiorna lo stato di un ordine
    public void aggiornaStatoOrdine(int idOrdine, String nuovoStato) throws SQLException {
        String sql = "UPDATE orders SET stato = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuovoStato);
            stmt.setInt(2, idOrdine);
            stmt.executeUpdate();
        }
    }

 // Elimina un ordine
    public void eliminaOrdine(int id) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}