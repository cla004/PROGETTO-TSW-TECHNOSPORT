package model;

public class Carrello {
    private int id;
    private double quantit�;
    private Utente id_utente;    
    private Prodotto prodotto; 

    

    
    public Carrello(int id, int quantit�, Utente id_utente, Prodotto prodotto) {
        this.id = id;
        this.quantit� = quantit�;
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

    public int getQuantit�() {
        return quantit�;
    }

    public void setQuantit�(double quantit�) {
        this.quantit� = quantit�;
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
����}
}