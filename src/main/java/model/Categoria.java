package model;

public class Categoria{
    private String nome_recensione;
    private int id_categoria;
  public Categoria() {
	  
	  // costruttore di default senza argomenti 
  }

    public Categoria(String nome_recensione, int id_categoria) {
        this.nome_recensione = nome_recensione;
        this.id_categoria = id_categoria;
       
    }

  

    // Getters e Setters
    public String getnome_recensione() { 
    	
    	return nome_recensione; 
    	
    	}
    
    public void setnome_recensione(String nome_recensione) { 
    	
    	this.nome_recensione = nome_recensione;
    	
    	}

    public int getid_categoria() { 
    	
    	return id_categoria; 
    	
    	}
    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }


}