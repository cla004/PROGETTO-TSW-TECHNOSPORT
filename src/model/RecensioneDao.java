package dao;

import model.Recensione;
import java.sql.*;
import java.util.*;

public class RecensioneDAO {

    public void inserisciRecensione(Recensione r) {
        String sql = "INSERT INTO recensioni (utente_id, prodotto_id, testo, voto, data) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, r.getUtenteId());
            stmt.setInt(2, r.getProdottoId());
            stmt.setString(3, r.getTesto());
            stmt.setInt(4, r.getVoto());
            stmt.setDate(5, new java.sql.Date(r.getData().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Recensione cercaRecensioneById(int id) {
        String sql = "SELECT * FROM recensioni WHERE id = ?";
        Recensione r = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                r = new Recensione();
                r.setId(rs.getInt("id"));
                r.setUtenteId(rs.getInt("utente_id"));
                r.setProdottoId(rs.getInt("prodotto_id"));
                r.setTesto(rs.getString("testo"));
                r.setVoto(rs.getInt("voto"));
                r.setData(rs.getDate("data"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    public List<Recensione> listaRecensioni() {
        List<Recensione> lista = new ArrayList<>();
        String sql = "SELECT * FROM recensioni";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Recensione r = new Recensione();
                r.setId(rs.getInt("id"));
                r.setUtenteId(rs.getInt("utente_id"));
                r.setProdottoId(rs.getInt("prodotto_id"));
                r.setTesto(rs.getString("testo"));
                r.setVoto(rs.getInt("voto"));
                r.setData(rs.getDate("data"));
                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void aggiornaRecensione(Recensione r) {
        String sql = "UPDATE recensioni SET utente_id = ?, prodotto_id = ?, testo = ?, voto = ?, data = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, r.getUtenteId());
            stmt.setInt(2, r.getProdottoId());
            stmt.setString(3, r.getTesto());
            stmt.setInt(4, r.getVoto());
            stmt.setDate(5, new java.sql.Date(r.getData().getTime()));
            stmt.setInt(6, r.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminaRecensione(int id) {
        String sql = "DELETE FROM recensioni WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
