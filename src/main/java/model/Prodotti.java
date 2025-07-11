package model;



public class Prodotti {
    private int id_prodotto;
    private String nome;
    private String immagini url;
    private double prezzo;
    private String descrizione;
    private String quantit�_disponibili;

    public Prodotti(int id prodotto, String nome, String immagini url, double prezzo,String descrizione,String quantit�_disponibili) {
        this.id = id_prodotto;
        this.name = nome;
        this.immagini = immagini;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.quantit�_disponibili = quantit�_disponibili;
    }

  

    // Getters e Setters
    public int getId_prodotto() { 
    	
    	return id_prodotto; 
    	
    	}
    
    public void setId(int id_prodotto) { 
    	
    	this.id_prodotto = id_prodotto;
    	
    	}

    public String getNome() { 
    	
    	return nome; 
    	
    	}
    public void setNome(String nome) { 
    	
    	this.nome = nome; 
    	
    	}

    public String getImmagini() { 
    	
    	return immagini; 
    	
    	}
    public void setImmagini(String immagini) { 
    	
    	this.immagini = immagini; 
    	
    	}

    public double getPrezzo() { 
    	
    	return prezzo; 
    	
    	}
    public void setPrezzo(double prezzo) {
    	
    	this.prezzo = prezzo;
    	
    	}
 public double getDescrizione() { 
    	
    	return descrizione; 
    	
    	}
public void setDescrizione(String descrizione) {
    	
    	this.descrizione = descrizione;
    	
    	}

public double getQuantit�_disponibili() { 
	
	return quantit�_disponibili; 
	
	}
public void setQuantit�_disponibili(String descrizione) {
	
	this.descrizione = descrizione;
	
	}

}
