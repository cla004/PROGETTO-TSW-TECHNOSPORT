package model;

public class Pagamento{
    private Recensione id_recensione;
    private String metodo;
    private String stato_pagamento;
    private int id_pagamento;
  

    public Pagamento(Recensione id_recensione, String metodo, String stato_pagamento, int id_pagamento) {
        this.id_recensione = id_recensione;
        this.metodo = metodo;
        this.stato_pagamento = stato_pagamento;
        this.id_pagamento = id_pagamento;
    }

  

    // Getters e Setters
    public Recensione getId_recensione() { 
    	
    	return id_recensione; 
    	
    	}
    
    public void setId_recensione(Recensione id_recensione) { 
    	
    	this.id_recensione = id_recensione;
    	
    	}

    public String getMetodo() { 
    	
    	return metodo; 
    	
    	}
    public void setMetodo(String metodo) { 
    	
    	this.metodo = metodo; 
    	
    	}

    public String getStato_pagamento() { 
    	
    	return stato_pagamento; 
    	
    	}
    public void setStato_pagamento(String stato) { 
    	
    	this.stato_pagamento = stato_pagamento; 
    	
    	}

    public int id_pagamento() { 
    	
    	return id_pagamento; 
    	
    	}
    public void setid_pagamento(int id_pagamento ) {
    	
    	this.id_pagamento = id_pagamento;
    	
    	}

}