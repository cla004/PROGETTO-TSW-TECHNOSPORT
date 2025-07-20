package model;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import model.Prodotto_taglia;
public class ProdottoTagliaDao {

	public void inserisci(Prodotto_taglia pt) {
	    String sql = "INSERT INTO prodotto_taglia (id_prodotto, id_taglia, quantita_disponibili) VALUES (?, ?, ?)";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	      
	        stmt.setInt(1, pt.getIdProdotto());        // prende l'id del prodotto da Prodotto_taglia
	        stmt.setInt(2, pt.getid_taglia());         // prende l'id della taglia da Prodotto_taglia
	        stmt.setDouble(3, pt.getQuantita_disponibili());  // prende la quantit� disponibile
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


    public Prodotto_taglia cerca(int id_prodotto, int id_taglia) {
        String sql = "SELECT * FROM prodotto_taglia WHERE id_prodotto = ? AND id_taglia = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_prodotto);
            stmt.setInt(2, id_taglia);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Prodotto_taglia(
                        rs.getInt("id_prodotto"),
                        rs.getInt("id_taglia"),
                        rs.getDouble("quantita_disponibili"));
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
                        rs.getInt("id_prodotto"),
                        rs.getInt("id_taglia"),
                        rs.getDouble("quantita_disponibili"));
                lista.add(pt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void aggiorna(Prodotto_taglia pt) {
        String sql = "UPDATE prodotto_taglia SET quantita_disponibili = ? WHERE id_prodotto = ? AND id_taglia = ?";
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
        String sql = "DELETE FROM prodotto_taglia WHERE id_prodotto = ? AND id_taglia = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_prodotto);
            stmt.setInt(2, id_taglia);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
