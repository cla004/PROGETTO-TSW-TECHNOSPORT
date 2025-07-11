package model;

public class Taglia{
    private int id_taglia;
    private String etichetta;
  

    public Taglia(int id_taglia, String etichetta) {
        this.id_taglia = id_taglia;
        this.etichetta = etichetta;
       
    }

  

    // Getters e Setters
    public int getid_taglia() { 
    	
    	return id_taglia; 
    	
    	}
    
    public void setid_taglia(int id_taglia) { 
    	
    	this.id_taglia = id_taglia;
    	
    	}

    public String getid_etichetta() { 
    	
    	return id_etichetta; 
    	
    	}
    public void setid_etichetta(String id_etichetta) { 
    	
    	this.id_etichetta = id_etichetta; 
    	
    	}

}