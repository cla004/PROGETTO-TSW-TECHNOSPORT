package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Pagamento;
import model.Recensione;

public class PagamentoDao {
    private Connection conn;

    public PagamentoDao(Connection conn) {
        this.conn = conn;
    }

    // Inserisce un nuovo pagamento
    public void inserisciPagamento(Pagamento pagamento) throws SQLException {
        String sql = "INSERT INTO pagamento (id_recensione, metodo, stato_pagamento) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pagamento.getId_recensione().getId());
            stmt.setString(2, pagamento.getMetodo());
            stmt.setString(3, pagamento.getStato_pagamento());
            stmt.executeUpdate();
        }
    }

    // Ottiene un pagamento per ID
    public Pagamento getPagamentoById(int idPagamento) throws SQLException {
        String sql = "SELECT * FROM pagamento WHERE id_pagamento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPagamento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    RecensioneDao recensioneDAO = new RecensioneDao(conn);
                    Recensione recensione = recensioneDAO.getRecensioneById(rs.getInt("id_recensione"));
                    String metodo = rs.getString("metodo");
                    String stato = rs.getString("stato_pagamento");

                    return new Pagamento(recensione, metodo, stato, idPagamento);
                }
            }
        }
        return null;
    }

    // Aggiorna un pagamento
    public void aggiornaPagamento(Pagamento pagamento) throws SQLException {
        String sql = "UPDATE pagamento SET id_recensione = ?, metodo = ?, stato_pagamento = ? WHERE id_pagamento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pagamento.getId_recensione().getId());
            stmt.setString(2, pagamento.getMetodo());
            stmt.setString(3, pagamento.getStato_pagamento());
            stmt.setInt(4, pagamento.id_pagamento());
            stmt.executeUpdate();
        }
    }

    // Elimina un pagamento
    public void eliminaPagamento(int idPagamento) throws SQLException {
        String sql = "DELETE FROM pagamento WHERE id_pagamento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPagamento);
            stmt.executeUpdate();
        }
    }


    // Recupera tutti i pagamenti
    public List<Pagamento> getTuttiIPagamenti() throws SQLException {
        List<Pagamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagamento";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            RecensioneDao recensioneDAO = new RecensioneDao(conn);

            while (rs.next()) {
                int id = rs.getInt("id_pagamento");
                Recensione recensione = recensioneDAO.getRecensioneById(rs.getInt("id_recensione"));
                String metodo = rs.getString("metodo");
                String stato = rs.getString("stato_pagamento");

                Pagamento pagamento = new Pagamento(recensione, metodo, stato, id);
                lista.add(pagamento);
            }
        }
        return lista;
    }
}

