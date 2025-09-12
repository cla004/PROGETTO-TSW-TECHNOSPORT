package model;

import java.sql.*;
import java.util.*;

public class ProdottiDao {

    // Inserisce un nuovo prodotto nel database
    public void inserisciProdotto(Prodotti p) {
        String sql = "INSERT INTO products (name, description, price, category_id, immagine, stock) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getDescrizione());
            stmt.setDouble(3, p.getPrezzo());
            stmt.setInt(4, p.getId_categoria());
            stmt.setString(5, p.getImmagine());
            stmt.setInt(6, Integer.parseInt(p.getQuantita_disponibili()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Inserisce un nuovo prodotto e restituisce l'ID generato
    public int inserisciProdottoConId(Prodotti p) {
        String sql = "INSERT INTO products (name, description, price, category_id, immagine, stock) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getDescrizione());
            stmt.setDouble(3, p.getPrezzo());
            stmt.setInt(4, p.getId_categoria());
            stmt.setString(5, p.getImmagine());
            stmt.setInt(6, Integer.parseInt(p.getQuantita_disponibili()));

            stmt.executeUpdate();
            
            // Recupera l'ID generato automaticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Errore nell'inserimento
    }

    // Cerca un prodotto per ID
    public Prodotti cercaProdottoById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        Prodotti p = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Prodotti();
                p.setId_prodotto(rs.getInt("id"));
                p.setNome(rs.getString("name"));
                p.setDescrizione(rs.getString("description"));
                p.setPrezzo(rs.getDouble("price"));
                p.setId_categoria(rs.getInt("category_id"));
                p.setImmagine(rs.getString("immagine"));
                p.setQuantita_disponibili(String.valueOf(rs.getInt("stock")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return p;
    }

    // Restituisce tutti i prodotti
    public List<Prodotti> listaProdotti() {
        List<Prodotti> lista = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Prodotti p = new Prodotti();
                p.setId_prodotto(rs.getInt("id"));
                p.setNome(rs.getString("name"));
                p.setDescrizione(rs.getString("description"));
                p.setPrezzo(rs.getDouble("price"));
                p.setId_categoria(rs.getInt("category_id"));
                p.setImmagine(rs.getString("immagine"));
                p.setQuantita_disponibili(String.valueOf(rs.getInt("stock")));
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Aggiorna un prodotto esistente
    public void aggiornaProdotto(Prodotti p) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, category_id = ?, immagine = ?, stock = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getDescrizione());
            stmt.setDouble(3, p.getPrezzo());
            stmt.setInt(4, p.getId_categoria());
            stmt.setString(5, p.getImmagine());
            stmt.setInt(6, Integer.parseInt(p.getQuantita_disponibili()));
            stmt.setInt(7, p.getId_prodotto());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Elimina un prodotto per ID
    public void eliminaProdotto(int id) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Restituisce il path dell'immagine di un prodotto
    public String getPhotoById(int id) {
        String sql = "SELECT immagine FROM products WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("immagine");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Restituisce il nome della categoria di un prodotto
    public String getNomeCategoriaById(int idCategoria) {
        String sql = "SELECT name FROM categories WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Restituisce tutti i prodotti di una categoria
    public List<Prodotti> findByCategoriaId(int idCategoria) {
        List<Prodotti> lista = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCategoria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Prodotti p = new Prodotti();
                p.setId_prodotto(rs.getInt("id"));
                p.setNome(rs.getString("name"));
                p.setDescrizione(rs.getString("description"));
                p.setPrezzo(rs.getDouble("price"));
                p.setId_categoria(rs.getInt("category_id"));
                p.setImmagine(rs.getString("immagine"));
                p.setQuantita_disponibili(String.valueOf(rs.getInt("stock")));
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    // METODI PER RIUTILIZZO ID
    
    /**
     * Trova il primo ID disponibile (più basso) tra quelli cancellati
     * Se non ci sono "buchi", restituisce il prossimo ID dopo l'ultimo
     */
    public int trovaProximoIdDisponibile() {
        String sql = "SELECT id FROM products ORDER BY id";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int expectedId = 1; // Inizia da 1
            
            while (rs.next()) {
                int actualId = rs.getInt("id");
                
                // Se c'è un "buco", quello è il prossimo ID disponibile
                if (actualId != expectedId) {
                    return expectedId;
                }
                expectedId++;
            }
            
            // Se non ci sono buchi, restituisce il prossimo ID consecutivo
            return expectedId;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 1; // Fallback se tutto fallisce
    }
    
    /**
     * NUOVO METODO: Inserisce un prodotto con ID specificato manualmente
     * Permette di riutilizzare gli ID cancellati
     */
    public int inserisciProdottoConIdSpecifico(Prodotti p, int idDesiderato) {
        String sql = "INSERT INTO products (id, name, description, price, category_id, immagine, stock) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idDesiderato);  // ID specificato manualmente
            stmt.setString(2, p.getNome());
            stmt.setString(3, p.getDescrizione());
            stmt.setDouble(4, p.getPrezzo());
            stmt.setInt(5, p.getId_categoria());
            stmt.setString(6, p.getImmagine());
            stmt.setInt(7, Integer.parseInt(p.getQuantita_disponibili()));
            
            stmt.executeUpdate();
            return idDesiderato; // Restituisce l'ID che abbiamo usato
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0; // Errore nell'inserimento
    }
    
    /**
     * METODO PRINCIPALE: Inserisce un prodotto riutilizzando ID cancellati
     * Combina trovaProximoIdDisponibile() + inserisciProdottoConIdSpecifico()
     */
    public int inserisciProdottoRiutilizzandoId(Prodotti p) {
        // 1. Trova il primo ID disponibile
        int idDisponibile = trovaProximoIdDisponibile();
        
        // 2. Inserisce con quell'ID specifico
        return inserisciProdottoConIdSpecifico(p, idDisponibile);
    }
    
    // METODI PER STATISTICHE ADMIN
    
    /**
     * Conta il numero totale di prodotti
     */
    public int contaProdotti() {
        String sql = "SELECT COUNT(*) as totale FROM products";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("totale");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Conta prodotti per categoria
     */
    public int contaProdottiPerCategoria(int categoriaId) {
        String sql = "SELECT COUNT(*) as totale FROM products WHERE category_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoriaId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("totale");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
}
