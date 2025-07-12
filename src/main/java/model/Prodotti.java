package model;

public class Prodotti {
    private int id_prodotto;
    private String nome;
    private String immagini_url;
    private double prezzo;
    private String descrizione;
    private String quantità_disponibili;
    private int id_categoria;

    public Prodotti() {
    	// costruttore di default senza argomenti
    }

    public Prodotti(int id_prodotto, String nome, String immagini_url, double prezzo, String descrizione, String quanità_disponibili, int id_categoria) {
        this.nome = nome;
        this.immagini_url = immagini_url;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.quantità_disponibili = quantità_disponibili;
        this.id_categoria=id_categoria;
      
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