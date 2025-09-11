package model;

/**
 * Classe che rappresenta un elemento dell'ordine (tabella order_items)
 */
public class DettaglioOrdine {
    private int id;
    private int orderId;
    private int productId;
    private int quantity;
    private double price;
    private int tagliaId;
    
    // Oggetti collegati per convenience
    private Ordine ordine;
    private Prodotti prodotto;
    private Taglia taglia;
    
    public DettaglioOrdine() {
    }
    
    public DettaglioOrdine(int id, int orderId, int productId, int quantity, double price, int tagliaId) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.tagliaId = tagliaId;
    }

    // Getter e Setter
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getTagliaId() {
        return tagliaId;
    }
    
    public void setTagliaId(int tagliaId) {
        this.tagliaId = tagliaId;
    }
    
    // Metodi convenience per oggetti collegati
    public Ordine getOrdine() {
        return ordine;
    }
    
    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
        if (ordine != null) {
            this.orderId = ordine.getId();
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

    public Taglia getTaglia() {
        return taglia;
    }
    
    public void setTaglia(Taglia taglia) {
        this.taglia = taglia;
        if (taglia != null) {
            this.tagliaId = taglia.getid_taglia();
        }
    }
    
    // Metodi di utilità per la JSP
    public String getNomeProdotto() {
        return prodotto != null ? prodotto.getNome() : "Prodotto non disponibile";
    }
    
    public String getNomeTaglia() {
        return taglia != null ? taglia.getEtichetta() : "Taglia non disponibile";
    }
    
    public double getPrezzoUnitario() {
        return price;
    }
    
    public double getTotaleRiga() {
        return price * quantity;
    }

}
