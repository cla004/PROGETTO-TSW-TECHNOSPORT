package tecnosport;

public class Utente {
    private String name;
    private String email;
    private String password;
    private String indirizzo_di_spedizione;

    public Utente(String name, String email, String password,,String indirizzo_di_spedizione) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.indirizzo_di_spedizione;
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
    

  
    

    
}