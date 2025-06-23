package dao

import com.example.model.Carrello;
import com.example.model.Utente;
import com.example.model.Prodotto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarrelloDAO {
    private final Connection connection;

    public CarrelloDAO(Connection connection) {
        this.connection = connection;
    }

    // Inserisci un nuovo elemento nel carrello
    public void insert(CartItem item) throws SQLException {
        String sql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, item.getUser().getId());
            stmt.setInt(2, item.getProduct().getId());
            stmt.setInt(3, item.getQuantity());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Aggiorna quantità di un elemento carrello
    public void update(CartItem item) throws SQLException {
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, item.getQuantity());
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
    public List<CartItem> findByUser(User user) throws SQLException {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT ci.id, ci.quantity, p.id AS pid, p.name, p.price " +
                     "FROM cart_items ci " +
                     "JOIN products p ON ci.product_id = p.id " +
                     "WHERE ci.user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getInt("id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUser(user);

                    Product product = new Product();
                    product.setId(rs.getInt("pid"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getBigDecimal("price"));

                    item.setProduct(product);

                    items.add(item);
                }
            }
        }
        return items;
    }

    // Trova elemento carrello per id
    public CartItem findById(int id) throws SQLException {
        String sql = "SELECT ci.id, ci.quantity, u.id AS uid, u.name AS uname, p.id AS pid, p.name AS pname, p.price " +
                     "FROM cart_items ci " +
                     "JOIN users u ON ci.user_id = u.id " +
                     "JOIN products p ON ci.product_id = p.id " +
                     "WHERE ci.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getInt("id"));
                    item.setQuantity(rs.getInt("quantity"));

                    User user = new User();
                    user.setId(rs.getInt("uid"));
                    user.setName(rs.getString("uname"));
                    item.setUser(user);

                    Product product = new Product();
                    product.setId(rs.getInt("pid"));
                    product.setName(rs.getString("pname"));
                    product.setPrice(rs.getBigDecimal("price"));
                    item.setProduct(product);

                    return item;
                }
            }
        }
        return null; // non trovato
    }
}