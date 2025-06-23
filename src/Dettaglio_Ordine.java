public class Dettaglio_Ordine {
    private int id;
    private Ordine Id_ordine;
    private Prodotto id_prodotto;
    private double quantità;
    private double prezzo;
    
    public Dettaglio_Ordine(int id , Ordine id_ordine, , Prodotto id_prodotto,double quantità,double prezzo) {
    	this.id = id;
        this.id_ordine = id_ordine;
        this.id_prodotto = id_prodotto;
        this.quantità = quantità;
        this.prezzo = prezzo;
    }

    public int getId() { 
    	
    	return id; 
    	
    	}
    public void setId(int id) { 
    	
    	this.id = id; 
    	
    	}

    public Ordine getId_ordine() {
    	
    	return id_ordine; 
    	
    	}
    public void setId_ordine(Ordine id_ordine) {
    	
    	this.id_ordine = id_ordine;
    	
    	}

    public Prodotto getId_prodotto() { 
    	
    	return id_prodotto; 
    	
    	}
    public void setId_prodotto(Prodotto Id_prodotto) { 
    	
    	this.id_prodotto = id_prodotto; 
    	
    	}

    public int getQuantità() {
    	
    	return quantità;
    	
    	}
    public void setQuantità(int quantità) {
    	
    	this.quantità = quantità; 
    	
    	}

    public double getPrezzo() {
    	
    	return prezzo;
    	
    	}
    public void setPrezzo(double prezzo) { 
    	
    	this.prezzo = prezzo; 
    	
    	}
}