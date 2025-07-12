package model;

public class Carrello {
    private int id;
    private int quantit�;
    private Utente id_utente;    
    private Prodotti prodotto; 

    public Carrello() {
    	
    	
    }

    
    public Carrello(int id, int quantit�, Utente id_utente, Prodotti prodotto) {
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

    public void setQuantit�(int quantit�) {
        this.quantit� = quantit�;
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