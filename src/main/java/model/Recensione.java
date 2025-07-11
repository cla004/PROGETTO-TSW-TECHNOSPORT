package model;

import java.util.Date;

public class Recensione{
    private int id_recensione;
    private String commento;
    private String valutazione;
    private Date data_recensione;
    private Utente id_utente;
  

    public Prodotti(int id_recensione, String commento, String valutazione, Date data_recensione,Utente id_utente) {
        this.id_recensione = id_recensione;
        this.commento = commento;
        this.valutazione = valutazione;
        this.data_recensione = data_recensione;
        this.id_utente = id_utente;
    }

  

    // Getters e Setters
    public int getId_recensione() { 
    	
    	return id_recensione; 
    	
    	}
    
    public void setId_recensione(int id_recensione) { 
    	
    	this.id_recensione = id_recensione;
    	
    	}

    public String getCommento() { 
    	
    	return commento; 
    	
    	}
    public void setCommento(String commento) { 
    	
    	this.nome = nome; 
    	
    	}

    public String getValutazione() { 
    	
    	return valutazione; 
    	
    	}
    public void setValutazione(String valutazione) { 
    	
    	this.valutazione = valutazione; 
    	
    	}

    public Date data_recensione() { 
    	
    	return data_recensione; 
    	
    	}
    public void setData_recensione(int data_recensione ) {
    	
    	this.data_recensione = data_recensione;
    	
    	}
    
 public int getId_utente () { 
    	
    	return id_utente; 
    	
    	}
    public void setId_utente(Utente id_utente ) {
    	
    	this.id_utente = id_utente;
    	
    	}


}