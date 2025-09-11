package model;

/**
 * Classe che rappresenta un singolo elemento nel carrello (tabella cart_items)
 */
public class CartItem {
    private int id;
    private int cartId;
    private int productId;
    private int quantity;
    private int tagliaId;
    private java.sql.Timestamp addedAt;
    
    // Oggetti collegati per convenience
    private Carrello carrello;
    private Prodotti prodotto;
    
    public CartItem() {
    }
    
    public CartItem(int id, int cartId, int productId, int quantity, int tagliaId) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
        this.tagliaId = tagliaId;
    }
    
    // Getter e Setter
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getCartId() {
        return cartId;
    }
    
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public int getTagliaId() {
        return tagliaId;
    }
    
    public void setTagliaId(int tagliaId) {
        this.tagliaId = tagliaId;
    }
    
    public java.sql.Timestamp getAddedAt() {
        return addedAt;
    }
    
    public void setAddedAt(java.sql.Timestamp addedAt) {
        this.addedAt = addedAt;
    }
    
    // Metodi convenience per oggetti collegati
    public Carrello getCarrello() {
        return carrello;
    }
    
    public void setCarrello(Carrello carrello) {
        this.carrello = carrello;
        if (carrello != null) {
            this.cartId = carrello.getId();
        }
    }
    
    public Prodotti getProdotto() {
        return prodotto;
    }
    
    public void setProdotto(Prodotti prodotto) {
        this.prodotto = prodotto;
        if (prodotto != null) {
            this.productId = prodotto.getId_prodotto();
        }
    }
    
    // Metodo di convenienza per ottenere l'user ID dal carrello
    public int getUserId() {
        return carrello != null ? carrello.getUserId() : 0;
    }
}
