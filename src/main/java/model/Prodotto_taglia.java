package model;

import model.Prodotti;

public class Prodotto_taglia{
    private int id_prodotto;
    private int id_taglia;
    private double quantit�_disponibili;
  

    public Prodotto_taglia(int id_prodotto, int id_taglia,double quantit�_disponibili) {
        this.id_prodotto = id_prodotto;
        this.id_taglia = id_taglia;
        this.quantit�_disponibili = quantit�_disponibili;
       
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
    
  public double getquantit�_disponibili() { 
    	
    	return quantit�_disponibili; 
    	
    	}
  
  public void setQuantit�_disponibili(double quantit�_disponibili) { 
	    // assegno il valore passato al campo della classe
	    this.quantit�_disponibili = quantit�_disponibili;
	}
}