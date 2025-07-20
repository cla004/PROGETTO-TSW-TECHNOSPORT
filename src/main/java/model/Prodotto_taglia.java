package model;

import model.Prodotti;

public class Prodotto_taglia{
    private int id_prodotto;
    private int id_taglia;
    private double quantita_disponibili;
  

    public Prodotto_taglia(int id_prodotto, int id_taglia,double quantita_disponibili) {
        this.id_prodotto = id_prodotto;
        this.id_taglia = id_taglia;
        this.quantita_disponibili = quantita_disponibili;
       
    }

  

    // Getters e Setters
    public int getIdProdotto() {
        return id_prodotto;
    }
    
    public void setid_prodotto(int  id_prodotto) { 
    	
    	this.id_prodotto = id_prodotto;
    	
    	}

    public int getid_taglia() { 
    	
    	return id_taglia;
    	
    	}
    public void setid_taglia(int id_taglia) { 
    	
    	this.id_taglia = id_taglia; 
    	
    	}
    
  public double getQuantita_disponibili() { 
    	
    	return quantita_disponibili; 
    	
    	}
  
  public void setQuantita_disponibili(double quantita_disponibili) { 
	    // assegno il valore passato al campo della classe
	    this.quantita_disponibili = quantita_disponibili;
	}
}
