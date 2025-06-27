package model;

import java.sql.*;
import java.util.*;
import model.DettaglioOrdine;

public class DettaglioOrdineDAO {
    public void inserisciDettaglio(DettaglioOrdine d) {
        String sql = "INSERT INTO dettagli_ordine (ordine_id, prodotto_id, quantita, prezzo_unitario) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, d.getOrdineId());
            stmt.setInt(2, d.getProdottoId());
            stmt.setInt(3, d.getQuantita());
            stmt.setDouble(4, d.getPrezzoUnitario());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public DettaglioOrdine cercaDettaglioById(int id) {
        String sql = "SELECT * FROM dettagli_ordine WHERE id = ?";
        DettaglioOrdine d = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                d = new DettaglioOrdine();
                d.setId(rs.getInt("id"));
                d.setOrdineId(rs.getInt("ordine_id"));
                d.setProdottoId(rs.getInt("prodotto_id"));
                d.setQuantita(rs.getInt("quantita"));
                d.setPrezzoUnitario(rs.getDouble("prezzo_unitario"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return d;
    }

    public List<DettaglioOrdine> listaDettagli() {
        List<DettaglioOrdine> lista = new ArrayList<>();
        String sql = "SELECT * FROM dettagli_ordine";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DettaglioOrdine d = new DettaglioOrdine();
                d.setId(rs.getInt("id"));
                d.setOrdineId(rs.getInt("ordine_id"));
                d.setProdottoId(rs.getInt("prodotto_id"));
                d.setQuantita(rs.getInt("quantita"));
                d.setPrezzoUnitario(rs.getDouble("prezzo_unitario"));
                lista.add(d);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public void aggiornaDettaglio(DettaglioOrdine d) {
        String sql = "UPDATE dettagli_ordine SET ordine_id = ?, prodotto_id = ?, quantita = ?, prezzo_unitario = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, d.getOrdineId());
            stmt.setInt(2, d.getProdottoId());
            stmt.setInt(3, d.getQuantita());
            stmt.setDouble(4, d.getPrezzoUnitario());
            stmt.setInt(5, d.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminaDettaglio(int id) {
        String sql = "DELETE FROM dettagli_ordine WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
