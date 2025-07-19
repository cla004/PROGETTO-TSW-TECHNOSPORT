package model;

public class Categoria{
    private String nome_categoria;
    private int id_categoria;
   
  public Categoria() {
	  
	  // costruttore di default senza argomenti 
  }

    public Categoria(String nome_recensione, int id_categoria) {
        this.nome_categoria= nome_recensione;
        this.id_categoria = id_categoria;
       
    }

  

    // Getters e Setters
    public String getnome_recensione() { 
    	
    	return nome_categoria; 
    	
    	}
    
    public void setnome_recensione(String nome_categoria) { 
    	
    	this.nome_categoria = nome_categoria;
    	
    	}

    public int getid_categoria() { 
    	
    	return id_categoria; 
    	
    	}
    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }


}