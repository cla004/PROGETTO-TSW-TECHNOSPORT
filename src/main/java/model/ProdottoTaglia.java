package model;

public class Prodotto_taglia{
    private Prodotto id_prodotto;
    private Taglia id_taglia;
    private double quantit�_disponibili;
  

    public Taglia(Prodotto id_prodotto, Taglia id_taglia,double quantit�_disponibili) {
        this.id_prodotto = id_prodotto;
        this.id_taglia = id_taglia;
        this.quantit�_disponibili = quantit�_disponibili;
       
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
    
  public String getquantit�_disponibili() { 
    	
    	return quantit�_disponibili; 
    	
    	}
    public void setquantit�_disponibili(double id_taglia) { 
    	
    	this.id_taglia = id_taglia; 
    	
   �	}
����
����

}