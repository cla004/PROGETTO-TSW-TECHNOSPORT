
package model;

public class Dettaglio_Ordine {
    private int id;
    private Ordine Id_ordine;
    private Prodotto id_prodotto;
    private double quantit�;
    private double prezzo;
    
    public Dettaglio_Ordine(int id , Ordine id_ordine , Prodotto id_prodotto,double quantit�,double prezzo) {
    	this.id = id;
        this.id_ordine = id_ordine;
        this.id_prodotto = id_prodotto;
        this.quantit� = quantit�;
        this.prezzo = prezzo;
    }

    public int getId() { 
    	
    	return id; 
    	
    	}
    public void setId(int id) { 
    	
    	this.id = id; 
    	
    	}

    public Ordine getId_ordine() {
    	
    	return id_ordine; 
    	
    	}
    public void setId_ordine(Ordine id_ordine) {
    	
    	this.id_ordine = id_ordine;
    	
    	}

    public Prodotto getId_prodotto() { 
    	
    	return id_prodotto; 
    	
    	}
    public void setId_prodotto(Prodotto Id_prodotto) { 
    	
    	this.id_prodotto = id_prodotto; 
    	
    	}

    public int getQuantit�() {
    	
    	return quantit�;
    	
    	}
    public void setQuantit�(int quantit�) {
    	
    	this.quantit� = quantit�; 
    	
    	}

    public double getPrezzo() {
    	
    	return prezzo;
    	
    	}
    public void setPrezzo(double prezzo) { 
    	
    	this.prezzo = prezzo;�
����	
����	}
}