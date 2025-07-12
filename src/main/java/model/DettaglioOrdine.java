
package model;

public class DettaglioOrdine {
    private int id;
    private Ordine Id_ordine;
    private Prodotti id_prodotto;
    private int quantit�;
    private double prezzo;
    
    public DettaglioOrdine() {
    	
    	//Costruttore senza argomenti 
    }
    
    public DettaglioOrdine(int id , Ordine Id_ordine , Prodotti id_prodotto, int quantit�,double prezzo) {
    	this.id = id;
        this.Id_ordine = Id_ordine;
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
    	
    	return Id_ordine; 
    	
    	}
    public void setId_ordine(Ordine Id_ordine) {
    	
    	this.Id_ordine = Id_ordine;
    	
    	}

    public Prodotti getId_prodotto() { 
    	
    	return id_prodotto; 
    	
    	}
    public void setId_prodotto(Prodotti id_prodotto) {
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
        this.prezzo = prezzo;
    }

}