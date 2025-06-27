package model;

import java.sql.*;
import java.util.*;
import model.Pagamento;

public class PagamentoDAO {
    public void inserisciPagamento(Pagamento p) {
        String sql = "INSERT INTO pagamenti (utente_id, importo, metodo, data) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getUtenteId());
            stmt.setDouble(2, p.getImporto());
            stmt.setString(3, p.getMetodo());
            stmt.setDate(4, new java.sql.Date(p.getData().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Pagamento cercaPagamentoById(int id) {
        String sql = "SELECT * FROM pagamenti WHERE id = ?";
        Pagamento p = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                p = new Pagamento();
                p.setId(rs.getInt("id"));
                p.setUtenteId(rs.getInt("utente_id"));
                p.setImporto(rs.getDouble("importo"));
                p.setMetodo(rs.getString("metodo"));
                p.setData(rs.getDate("data"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return p;
    }

    public List<Pagamento> listaPagamenti() {
        List<Pagamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagamenti";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pagamento p = new Pagamento();
                p.setId(rs.getInt("id"));
                p.setUtenteId(rs.getInt("utente_id"));
                p.setImporto(rs.getDouble("importo"));
                p.setMetodo(rs.getString("metodo"));
                p.setData(rs.getDate("data"));
                lista.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public void aggiornaPagamento(Pagamento p) {
        String sql = "UPDATE pagamenti SET utente_id = ?, importo = ?, metodo = ?, data = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getUtenteId());
            stmt.setDouble(2, p.getImporto());
            stmt.setString(3, p.getMetodo());
            stmt.setDate(4, new java.sql.Date(p.getData().getTime()));
            stmt.setInt(5, p.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminaPagamento(int id) {
        String sql = "DELETE FROM pagamenti WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}