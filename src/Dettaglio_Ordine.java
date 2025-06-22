package tecnosport;

public class Dettaglio_Ordine{
    private int id_dettaglio;
    private double quantità;
  

    public Pagamento(int id_dettaglio, double quantità) {
        this.id_dettaglio = id_dettaglio;
        this.quantità = quantità;
       
    }

  

    // Getters e Setters
    public int getid_dettaglio() { 
    	
    	return id_dettaglio; 
    	
    	}
    
    public void setid_dettaglio(int id_dettaglio) { 
    	
    	this.id_dettaglio = id_dettaglio;
    	
    	}

    public double getquantità() { 
    	
    	return quantità; 
    	
    	}
    public void setquantità(double quantità) { 
    	
    	this.quantità = qauntità; 
    	
    	}

}
