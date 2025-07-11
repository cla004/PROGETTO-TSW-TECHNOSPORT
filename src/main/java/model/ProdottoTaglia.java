package model;

public class Prodotto_taglia{
    private Prodotto id_prodotto;
    private Taglia id_taglia;
    private double quantità_disponibili;
  

    public Taglia(Prodotto id_prodotto, Taglia id_taglia,double quantità_disponibili) {
        this.id_prodotto = id_prodotto;
        this.id_taglia = id_taglia;
        this.quantità_disponibili = quantità_disponibili;
       
    }

  

    // Getters e Setters
    public int getid_prodotto() { 
    	
    	return id_prodotto; 
    	
    	}
    
    public void setid_prodotto(Prodotto id_prodotto) { 
    	
    	this.id_prodotto = id_prodotto;
    	
    	}

    public String getid_taglia() { 
    	
    	return id_taglia; 
    	
    	}
    public void setid_taglia(Taglia id_taglia) { 
    	
    	this.id_taglia = id_taglia; 
    	
    	}
    
  public String getquantità_disponibili() { 
    	
    	return quantità_disponibili; 
    	
    	}
    public void setquantità_disponibili(double id_taglia) { 
    	
    	this.id_taglia = id_taglia; 
    	
    	}
    
    

}