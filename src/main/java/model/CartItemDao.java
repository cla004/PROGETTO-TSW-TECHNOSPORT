package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemDao {

    // Inserisci un nuovo elemento nel carrello
    public void insert(CartItem item) throws SQLException {
        String sql = "INSERT INTO cart_items (cart_id, product_id, quantity, taglia_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, item.getCartId());
            stmt.setInt(2, item.getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.setInt(4, item.getTagliaId());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Aggiorna quantità di un elemento carrello
    public void updateQuantity(int itemId, int newQuantity) throws SQLException {
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newQuantity);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
        }
    }

    // Cancella elemento dal carrello per id
    public void delete(int itemId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itemId);
            stmt.executeUpdate();
        }
    }

    // Cancella tutti gli elementi del carrello di un utente
    public void deleteByUserId(int userId) throws SQLException {
        String sql = "DELETE ci FROM cart_items ci " +
                     "JOIN cart c ON ci.cart_id = c.id " +
                     "WHERE c.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
    
    // Cancella tutti gli elementi di un carrello specifico
    public void deleteByCartId(int cartId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartId);
            stmt.executeUpdate();
        }
    }

    // Trova tutti gli elementi del carrello di un utente
    public List<CartItem> findByUserId(int userId) throws SQLException {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT ci.id, ci.cart_id, ci.product_id, ci.quantity, ci.taglia_id, ci.added_at, " +
                     "p.name, p.price, p.immagine, p.description " +
                     "FROM cart_items ci " +
                     "JOIN cart c ON ci.cart_id = c.id " +
                     "JOIN products p ON ci.product_id = p.id " +
                     "WHERE c.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getInt("id"));
                    item.setCartId(rs.getInt("cart_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setTagliaId(rs.getInt("taglia_id"));
                    item.setAddedAt(rs.getTimestamp("added_at"));

                    // Crea oggetto prodotto con i dati dal join
                    Prodotti product = new Prodotti();
                    product.setId_prodotto(rs.getInt("product_id"));
                    product.setNome(rs.getString("name"));
                    product.setPrezzo(rs.getDouble("price"));
                    product.setImmagine(rs.getString("immagine"));
                    product.setDescrizione(rs.getString("description"));

                    item.setProdotto(product);
                    items.add(item);
                }
            }
        }
        return items;
    }

    // Trova elemento carrello per id
    public CartItem findById(int itemId) throws SQLException {
        String sql = "SELECT ci.id, ci.cart_id, ci.product_id, ci.quantity, ci.taglia_id, ci.added_at, " +
                     "p.name, p.price, p.immagine, p.description " +
                     "FROM cart_items ci " +
                     "JOIN products p ON ci.product_id = p.id " +
                     "WHERE ci.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itemId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getInt("id"));
                    item.setCartId(rs.getInt("cart_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setTagliaId(rs.getInt("taglia_id"));
                    item.setAddedAt(rs.getTimestamp("added_at"));

                    // Crea oggetto prodotto
                    Prodotti product = new Prodotti();
                    product.setId_prodotto(rs.getInt("product_id"));
                    product.setNome(rs.getString("name"));
                    product.setPrezzo(rs.getDouble("price"));
                    product.setImmagine(rs.getString("immagine"));
                    product.setDescrizione(rs.getString("description"));

                    item.setProdotto(product);
                    return item;
                }
            }
        }
        return null;
    }

    // Verifica se esiste già un elemento con questo prodotto e taglia per l'utente
    public CartItem findByUserProductAndSize(int userId, int productId, int tagliaId) throws SQLException {
        String sql = "SELECT ci.* FROM cart_items ci " +
                     "JOIN cart c ON ci.cart_id = c.id " +
                     "WHERE c.user_id = ? AND ci.product_id = ? AND ci.taglia_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.setInt(3, tagliaId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getInt("id"));
                    item.setCartId(rs.getInt("cart_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setTagliaId(rs.getInt("taglia_id"));
                    item.setAddedAt(rs.getTimestamp("added_at"));
                    return item;
                }
            }
        }
        return null;
    }
    
    /**
     * Rimuove un prodotto specifico da tutti i carrelli attivi
     * Utilizzato prima di cancellare un prodotto per evitare riferimenti orfani
     */
    public void rimuoviProdottoDaTuttiICarrelli(int prodottoId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prodottoId);
            int righeEliminate = stmt.executeUpdate();
            System.out.println("Rimosso prodotto ID " + prodottoId + " da " + righeEliminate + " carrelli");
        }
    }
}
