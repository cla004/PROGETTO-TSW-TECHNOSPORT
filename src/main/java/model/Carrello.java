package model;

public class Carrello {
    private int id;
    private int quantità;
    private Utente id_utente;    
    private Prodotti prodotto; 

    public Carrello() {
    	
    	
    }

    
    public Carrello(int id, int quantità, Utente id_utente, Prodotti prodotto) {
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

    public void setQuantità(int quantità) {
        this.quantità = quantità;
    }

    public Utente getUtente() {
        return id_utente;
    }

    public void setUtente(Utente id_utente) {
    	this.id_utente=id_utente;
      
    }

    public Prodotti getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotti prodotto) {
        this.prodotto=prodotto;
    }
}