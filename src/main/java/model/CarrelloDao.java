package model;

import model.Carrello;
import model.Utente;
import model.Prodotti;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarrelloDao {
    private final Connection connection;
	private Carrello item;

    public CarrelloDao(Connection connection) {
        this.connection = connection;
    }

    // Inserisci un nuovo elemento nel carrello
    public void insert(Carrello item) throws SQLException {
        String sql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, item.getUtente().getId());
            stmt.setInt(2, item.getProdotto().getId_prodotto());
            stmt.setInt(3, item.getQuantità());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Aggiorna quantità di un elemento carrello
    public void update(Carrello item) throws SQLException {
 
		String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, item.getQuantità());
            stmt.setInt(2, item.getId());
            stmt.executeUpdate();
        }
    }

    // Cancella elemento dal carrello per id
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Trova tutti gli elementi del carrello di un utente
    public List<Carrello> findByUser(Utente user) throws SQLException {
        List<Carrello> items = new ArrayList<>();
        String sql = "SELECT ci.id, ci.quantity, p.id AS pid, p.name, p.price " +
                     "FROM cart_items ci " +
                     "JOIN products p ON ci.product_id = p.id " +
                     "WHERE ci.user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Carrello item = new Carrello();
                    item.setId(rs.getInt("id"));
                    item.setQuantità(rs.getInt("quantity"));
                    item.setUtente(user);

                    Prodotti product = new Prodotti();
                    product.setId(rs.getInt("pid"));
                    product.setNome(rs.getString("name"));
                    product.setPrezzo(rs.getDouble("price"));

                    item.setProdotto(product);

                    items.add(item);
                }
            }
        }
        return items;
    }

    // Trova elemento carrello per id
    public Carrello findById(int id) throws SQLException {
        String sql = "SELECT ci.id, ci.quantity, u.id AS uid, u.name AS uname, p.id AS pid, p.name AS pname, p.price " +
                     "FROM cart_items ci " +
                     "JOIN users u ON ci.user_id = u.id " +
                     "JOIN products p ON ci.product_id = p.id " +
                     "WHERE ci.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Carrello item = new Carrello();
                    item.setId(rs.getInt("id"));
                    item.setQuantità(rs.getInt("quantity"));

                    Utente user = new Utente();
                    user.setId(rs.getInt("uid"));
                    user.setNome(rs.getString("uname"));
                    item.setUtente(user);

                    Prodotti product = new Prodotti();
                    product.setId(rs.getInt("pid"));
                    product.setNome(rs.getString("pname"));
                    product.setPrezzo(rs.getDouble("price"));
                    item.setProdotto(product);

                    return item;
                }
            }
        }
    return null; // non trovato
    }
}
