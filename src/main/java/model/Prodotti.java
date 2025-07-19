package model;

public class Prodotti {
    private int id_prodotto;
    private String nome;
 // Rappresenta l'immagine del prodotto in formato binario (BLOB del database)
    private byte[] immagine;
    private double prezzo;
    private String descrizione;
    private String quantità_disponibili;
    private int id_categoria;

    public Prodotti() {
    	// costruttore di default senza argomenti
    }
    //Costruttore con argomenti (attributi della classe Prodotti usando la parola chiave this)

    public Prodotti(int id_prodotto, String nome, byte[] immagine, double prezzo, String descrizione, String quantità_disponibili, int id_categoria) {
this.id_prodotto = id_prodotto;
this.nome = nome;
this.immagine = immagine;
this.prezzo = prezzo;
this.descrizione = descrizione;
this.quantità_disponibili = quantità_disponibili;
this.id_categoria = id_categoria;
}

  

    


	// Getters e Setters
    
   
    public int getId_prodotto() { 
    	
    	return id_prodotto; 
    	
    	}
    
    public int getId_categoria() {
		return id_categoria;
	}

	public void setId_categoria(int id_categoria) {
		this.id_categoria = id_categoria;
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
    	
    	return immagini_url; 
    	
    	}
    public void setImmagini(String immagini) { 
    	
    	this.immagini_url = immagini; 
    	
    	}

    public double getPrezzo() { 
    	
    	return prezzo; 
    	
    	}
    public void setPrezzo(double prezzo) {
    	
    	this.prezzo = prezzo;
    	
    	}
 public String getDescrizione() { 
    	
    	return descrizione; 
    	
    	}
public void setDescrizione(String descrizione) {
    	
    	this.descrizione = descrizione;
    	
    	}

public String getQuantità_disponibili() { 
	
	return quantità_disponibili; 
	
	}
public void setQuantità_disponibili(String descrizione) {
	
	this.descrizione = descrizione;
	
	}



}