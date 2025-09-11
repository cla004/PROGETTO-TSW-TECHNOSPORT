package model;

/**
 * Classe che rappresenta un metodo di pagamento dell'utente (tabella metodi_pagamento)
 */
public class MetodoPagamento {
    private int id;
    private int idUtente;
    private String tipo; // es: "Visa", "Mastercard", "PayPal"
    private String ultimeQuattroCifre; // Solo le ultime 4 cifre per visualizzazione
    private String intestatario;
    private String scadenza; // formato "MM/YYYY"
    private boolean predefinito; // metodo di pagamento principale
    
    // Oggetto utente collegato per convenience
    private Utente utente;
    
    public MetodoPagamento() {
    }
    
    public MetodoPagamento(int id, int idUtente, String tipo, String ultimeQuattroCifre, 
                          String intestatario, String scadenza, boolean predefinito) {
        this.id = id;
        this.idUtente = idUtente;
        this.tipo = tipo;
        this.ultimeQuattroCifre = ultimeQuattroCifre;
        this.intestatario = intestatario;
        this.scadenza = scadenza;
        this.predefinito = predefinito;
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
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getUltimeQuattroCifre() {
        return ultimeQuattroCifre;
    }
    
    public void setUltimeQuattroCifre(String ultimeQuattroCifre) {
        this.ultimeQuattroCifre = ultimeQuattroCifre;
    }
    
    public String getIntestatario() {
        return intestatario;
    }
    
    public void setIntestatario(String intestatario) {
        this.intestatario = intestatario;
    }
    
    public String getScadenza() {
        return scadenza;
    }
    
    public void setScadenza(String scadenza) {
        this.scadenza = scadenza;
    }
    
    public boolean isPredefinito() {
        return predefinito;
    }
    
    public void setPredefinito(boolean predefinito) {
        this.predefinito = predefinito;
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
    
    // Metodo per ottenere il numero carta mascherato (per sicurezza)
    public String getNumeroCartaMascherato() {
        if (ultimeQuattroCifre == null || ultimeQuattroCifre.isEmpty()) {
            return "**** **** **** ****";
        }
        return "**** **** **** " + ultimeQuattroCifre;
    }
    
    // Metodo per ottenere una descrizione del metodo di pagamento
    public String getDescrizione() {
        String descrizione = "";
        
        if (tipo != null && !tipo.trim().isEmpty()) {
            descrizione += tipo;
        }
        
        if (ultimeQuattroCifre != null && !ultimeQuattroCifre.trim().isEmpty()) {
            if (!descrizione.isEmpty()) {
                descrizione += " ";
            }
            descrizione += getNumeroCartaMascherato();
        }
        
        if (intestatario != null && !intestatario.trim().isEmpty()) {
            if (!descrizione.isEmpty()) {
                descrizione += " - ";
            }
            descrizione += intestatario;
        }
        
        return descrizione;
    }
    
    @Override
    public String toString() {
        return getDescrizione();
    }
}
