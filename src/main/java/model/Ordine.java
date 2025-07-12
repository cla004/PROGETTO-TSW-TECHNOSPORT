package model;

import java.util.Date;

public class Ordine {
    private int id;
    private Utente user_Id;
    private double totale;
    private String stato;
    private Date data;
    
    public Ordine( int id, Utente user_Id, double totale,String stato,Date data) {
        this.id = id;
        this.user_Id = user_Id;
        this.totale = totale;
        this.stato = stato;
        this.data = data;
    }
   

    public Ordine() {
		// costruttore senza argomenti 
	}


	public int getId() {
    	
    	return id;
    	
    	}
    
    public void setId(int id) {
    	
    	this.id = id; 
    	
    	}

    public Utente getUser_Id() { 
    	
    	return user_Id; 
    	
    	}
    
    public void setUser_Id(Utente user_Id) {
    	
    	this.user_Id = user_Id; 
    	
    	}

    public double getTotale() { 
    	
    	return totale; 
    	
    	}
    
    public void setTotale(double totale) { 
    	
    	this.totale = totale; 
    	
    	}

    public String getStato() {
    	
    	return stato; 
    	
    	}
    
    public void setStato(String stato) {
    	
    	this.stato = stato; 
    	
    	}
 public Date getData() {
    	
    	return data; 
    	
    	}
    
 public void setData(Date data) {
	    this.data = data;
	}

}