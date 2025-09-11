package model;

import java.sql.Timestamp;

/**
 * Classe che rappresenta un ordine (tabella orders)
 */
public class Ordine {
    private int id;
    private int idUtente;
    private int idIndirizzo;
    private int idMetodo;
    private double totale;
    private Timestamp dataOrdine;
    private String stato;
    
    // Oggetti collegati per convenience
    private Utente utente;
    private Indirizzo indirizzo;
    private MetodoPagamento metodoPagamento;
    
    public Ordine() {
    }
    
    public Ordine(int id, int idUtente, int idIndirizzo, int idMetodo, 
                  double totale, Timestamp dataOrdine, String stato) {
        this.id = id;
        this.idUtente = idUtente;
        this.idIndirizzo = idIndirizzo;
        this.idMetodo = idMetodo;
        this.totale = totale;
        this.dataOrdine = dataOrdine;
        this.stato = stato;
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

    public int getIdIndirizzo() {
        return idIndirizzo;
    }
    
    public void setIdIndirizzo(int idIndirizzo) {
        this.idIndirizzo = idIndirizzo;
    }

    public int getIdMetodo() {
        return idMetodo;
    }
    
    public void setIdMetodo(int idMetodo) {
        this.idMetodo = idMetodo;
    }

    public double getTotale() {
        return totale;
    }
    
    public void setTotale(double totale) {
        this.totale = totale;
    }

    public Timestamp getDataOrdine() {
        return dataOrdine;
    }
    
    public void setDataOrdine(Timestamp dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public String getStato() {
        return stato;
    }
    
    public void setStato(String stato) {
        this.stato = stato;
    }
    
    // Metodi convenience per oggetti collegati
    public Utente getUtente() {
        return utente;
    }
    
    public void setUtente(Utente utente) {
        this.utente = utente;
        if (utente != null) {
            this.idUtente = utente.getId();
        }
    }

    public Indirizzo getIndirizzo() {
        return indirizzo;
    }
    
    public void setIndirizzo(Indirizzo indirizzo) {
        this.indirizzo = indirizzo;
        if (indirizzo != null) {
            this.idIndirizzo = indirizzo.getId();
        }
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }
    
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
        if (metodoPagamento != null) {
            this.idMetodo = metodoPagamento.getId();
        }
    }

}