package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Recensione;
import model.Utente;

public class RecensioneDao {
    private Connection conn;

    public RecensioneDao(Connection conn) {
        this.conn = conn;
    }

    // Inserimento di una recensione
    public void inserisciRecensione(Recensione r) throws SQLException {
        String sql = "INSERT INTO recensioni (id_recensione, commento, valutazione, data_recensione, id_utente) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, r.getId_recensione());
            stmt.setString(2, r.getCommento());
            stmt.setString(3, r.getValutazione());
            stmt.setDate(4, new java.sql.Date(r.data_recensione().getTime()));
            stmt.setInt(5, r.getId_utente().getId()); // Presume che Utente abbia un metodo getId()
            stmt.executeUpdate();
        }
    }

    // Recupera una recensione per ID
    public Recensione getRecensioneById(int id) throws SQLException {
        String sql = "SELECT * FROM recensioni WHERE id_recensione = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UtenteDao utentedao = new UtenteDao();
                    Utente utente = utentedao.cercaUtenteById(rs.getInt("id_utente"));

                    return new Recensione(
                        rs.getInt("id_recensione"),
                        rs.getString("commento"),
                        rs.getString("valutazione"),
                        rs.getDate("data_recensione"),
                        utente
                    );
                }
            }
        }
        return null;
    }

    // Recupera tutte le recensioni
    public List<Recensione> getAllRecensioni() throws SQLException {
        List<Recensione> lista = new ArrayList<>();
        String sql = "SELECT * FROM recensioni";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            UtenteDao utenteDao = new UtenteDao();
            while (rs.next()) {
            	Utente utente = utenteDao.cercaUtenteById(rs.getInt("id_utente"));
                Recensione r = new Recensione(
                    rs.getInt("id_recensione"),
                    rs.getString("commento"),
                    rs.getString("valutazione"),
                    rs.getDate("data_recensione"),
                    utente
                );
                lista.add(r);
            }
        }
        return lista;
    }

    // Elimina recensione per ID
    public void eliminaRecensione(int id_recensione) throws SQLException {
        String sql = "DELETE FROM recensioni WHERE id_recensione = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_recensione);
            stmt.executeUpdate();
        }
    }

 // Aggiorna recensione
    public void aggiornaRecensione(Recensione r) throws SQLException {
        String sql = "UPDATE recensioni SET commento = ?, valutazione = ?, data_recensione = ?, id_utente = ? WHERE id_recensione = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, r.getCommento());
            stmt.setString(2, r.getValutazione());
            stmt.setDate(3, new java.sql.Date(r.data_recensione().getTime()));  // CORRETTO: getData_recensione()
            stmt.setInt(4, r.getId_utente().getId());
            stmt.setInt(5, r.getId_recensione());
            stmt.executeUpdate();
        }
    }
}