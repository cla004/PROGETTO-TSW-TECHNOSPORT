package tecnosport;

public class User {
    private String name;
    private String email;
    private String password;
    private int telefono;
    private String indirizzo_di_spedizione;
    private String indirizzo_di_fatturazione;

    public User(String name, String email, String password,int telefono,String indirizzo_di_spedizione,String indirizzo_di_fatturazione) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.telefono =  telefono;
        this.indirizzo_di_spedizione;
        this.indirizzo_di_fatturazione;
    }

    // Getters e Setters
    public String getName() {
    	
    	return name; 
    	
    	}
    
    public void setName(String name) {
    	
    	this.name = name; 
    	
    }
    

    public String getEmail() { 
    	
    	return email; 
    	
    	}
    
    public void setEmail(String email) {
    	
    	this.email = email; 
    	
    	}

    public String getPassword() { 
    	
    	return password;
    	
    }
    
    public void setPassword(String password) { 
    	
    	this.password = password; 
    	
    	}
    
 public int getTelefono() {
    	
    	return telefono; 
    	
    	}
    
    public void setTelefono(int telefono) {
    	
    	this.telefono= telefono; 
    	
    }
    
 public String getIndirizzo_di_spedizione() {
    	
    	return Indirizzo_di_spedizione; 
    	
    	}
    
    public void setIndirizzo_di_spedizione(String indirizzo_di_spedizione) {
    	
    	this.indirizzo_di_spedizione = indirizzo_di_spedizione; 
    	
    }
    
}