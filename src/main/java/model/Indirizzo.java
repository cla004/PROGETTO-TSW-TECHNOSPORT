package model;

/**
 * Classe che rappresenta un indirizzo dell'utente (tabella indirizzi)
 */
public class Indirizzo {
    private int id;
    private int idUtente;
    private String via;
    private String citta;
    private String cap;
    private String provincia;
    private String paese;
    
    // Oggetto utente collegato per convenience
    private Utente utente;
    
    public Indirizzo() {
    }
    
    public Indirizzo(int id, int idUtente, String via, String citta, String cap, String provincia, String paese) {
        this.id = id;
        this.idUtente = idUtente;
        this.via = via;
        this.citta = citta;
        this.cap = cap;
        this.provincia = provincia;
        this.paese = paese;
    }
    
    // Getter e Setter
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdUtente() {
        return idUtente;
    }
    
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }
    
    public String getVia() {
        return via;
    }
    
    public void setVia(String via) {
        this.via = via;
    }
    
    public String getCitta() {
        return citta;
    }
    
    public void setCitta(String citta) {
        this.citta = citta;
    }
    
    public String getCap() {
        return cap;
    }
    
    public void setCap(String cap) {
        this.cap = cap;
    }
    
    public String getProvincia() {
        return provincia;
    }
    
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
    
    public String getPaese() {
        return paese;
    }
    
    public void setPaese(String paese) {
        this.paese = paese;
    }
    
    // Metodo convenience per oggetto utente
    public Utente getUtente() {
        return utente;
    }
    
    public void setUtente(Utente utente) {
        this.utente = utente;
        if (utente != null) {
            this.idUtente = utente.getId();
        }
    }
    
    // Metodo di utilità per ottenere l'indirizzo completo formattato
    public String getIndirizzoCompleto() {
        String risultato = "";
        
        if (via != null && !via.trim().isEmpty()) {
            risultato += via;
        }
        
        if (citta != null && !citta.trim().isEmpty()) {
            if (!risultato.isEmpty()) {
                risultato += ", ";
            }
            risultato += citta;
        }
        
        if (cap != null && !cap.trim().isEmpty()) {
            if (!risultato.isEmpty()) {
                risultato += " ";
            }
            risultato += cap;
        }
        
        if (provincia != null && !provincia.trim().isEmpty()) {
            if (!risultato.isEmpty()) {
                risultato += ", ";
            }
            risultato += provincia;
        }
        
        if (paese != null && !paese.trim().isEmpty()) {
            if (!risultato.isEmpty()) {
                risultato += ", ";
            }
            risultato += paese;
        }
        
        return risultato;
    }
    
    @Override
    public String toString() {
        return getIndirizzoCompleto();
    }
}
