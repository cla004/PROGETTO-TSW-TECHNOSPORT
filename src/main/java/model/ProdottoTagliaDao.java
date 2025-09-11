package model;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import model.Prodotto_taglia;
public class ProdottoTagliaDao {

    public void inserisci(Prodotto_taglia pt) {
        String sql = "INSERT INTO prodotto_taglia (prodotto_id, taglia_id, quantita_disponibile) VALUES (?, ?, ?)";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	      
	        stmt.setInt(1, pt.getIdProdotto());        // prende l'id del prodotto da Prodotto_taglia
	        stmt.setInt(2, pt.getid_taglia());         // prende l'id della taglia da Prodotto_taglia
	        stmt.setDouble(3, pt.getQuantita_disponibili());  // prende la quantità disponibile
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


    public Prodotto_taglia cerca(int id_prodotto, int id_taglia) {
        String sql = "SELECT * FROM prodotto_taglia WHERE prodotto_id = ? AND taglia_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_prodotto);
            stmt.setInt(2, id_taglia);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Prodotto_taglia(
                        rs.getInt("prodotto_id"),
                        rs.getInt("taglia_id"),
                        rs.getDouble("quantita_disponibile"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Prodotto_taglia> lista() {
        List<Prodotto_taglia> lista = new ArrayList<>();
        String sql = "SELECT * FROM prodotto_taglia";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Prodotto_taglia pt = new Prodotto_taglia(
                        rs.getInt("prodotto_id"),
                        rs.getInt("taglia_id"),
                        rs.getDouble("quantita_disponibile"));
                lista.add(pt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void aggiorna(Prodotto_taglia pt) {
        String sql = "UPDATE prodotto_taglia SET quantita_disponibile = ? WHERE prodotto_id = ? AND taglia_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, pt.getQuantita_disponibili());
            stmt.setInt(2, pt.getIdProdotto());
            stmt.setInt(3, pt.getid_taglia());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void elimina(int id_prodotto, int id_taglia) {
        String sql = "DELETE FROM prodotto_taglia WHERE prodotto_id = ? AND taglia_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_prodotto);
            stmt.setInt(2, id_taglia);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Metodo per ottenere le taglie disponibili per un prodotto specifico
    public List<Prodotto_taglia> getTaglieDisponibiliPerProdotto(int id_prodotto) {
        List<Prodotto_taglia> taglie = new ArrayList<>();
        String sql = "SELECT * FROM prodotto_taglia WHERE prodotto_id = ? AND quantita_disponibile > 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_prodotto);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Prodotto_taglia pt = new Prodotto_taglia(
                        rs.getInt("prodotto_id"),
                        rs.getInt("taglia_id"),
                        rs.getDouble("quantita_disponibile"));
                taglie.add(pt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taglie;
    }
    
    // Metodo per decrementare la quantità disponibile
    public boolean decrementaQuantita(int id_prodotto, int id_taglia, int quantitaDaDecrementare) {
        String sql = "UPDATE prodotto_taglia SET quantita_disponibile = quantita_disponibile - ? " +
                    "WHERE prodotto_id = ? AND taglia_id = ? AND quantita_disponibile >= ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantitaDaDecrementare);
            stmt.setInt(2, id_prodotto);
            stmt.setInt(3, id_taglia);
            stmt.setInt(4, quantitaDaDecrementare);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // true se l'aggiornamento è riuscito
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Elimina tutte le associazioni prodotto-taglia per un prodotto specifico
     * Utile quando si elimina completamente un prodotto
     */
    public void eliminaByProdottoId(int id_prodotto) {
        String sql = "DELETE FROM prodotto_taglia WHERE prodotto_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_prodotto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
