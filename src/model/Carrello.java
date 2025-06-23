package tecnosport;

public class Carrello {
    private int id;
    private double quantità;
    private Utente id_utente;    
    private Prodotto prodotto; 

    

    
    public Carrello(int id, int quantità, Utente id_utente, Prodotto prodotto) {
        this.id = id;
        this.quantità = quantità;
        this.id_utente = id_utente;
        this.prodotto = prodotto;
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantità() {
        return quantità;
    }

    public void setQuantità(double quantità) {
        this.quantità = quantità;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Product getProdotto() {
        return product;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotti = prodotti;
    }
}