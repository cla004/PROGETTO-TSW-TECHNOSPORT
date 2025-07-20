package model;

public class Carrello {
    private int id;
    private int quantita;
    private Utente id_utente;    
    private Prodotti prodotto; 

    public Carrello() {
    	
    	
    }

    
    public Carrello(int id, int quantita, Utente id_utente, Prodotti prodotto) {
        this.id = id;
        this.quantita = quantita;
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

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
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
