package model;

import java.sql.Timestamp;

/**
 * Classe che rappresenta un pagamento (tabella payments)
 */
public class Payment {
    private int id;
    private int orderId;
    private int metodoId;
    private Timestamp paidAt;
    
    // Oggetti collegati per convenience
    private Ordine ordine;
    private MetodoPagamento metodoPagamento;
    
    public Payment() {
    }
    
    public Payment(int id, int orderId, int metodoId, Timestamp paidAt) {
        this.id = id;
        this.orderId = orderId;
        this.metodoId = metodoId;
        this.paidAt = paidAt;
    }
    
    // Getter e Setter
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public int getMetodoId() {
        return metodoId;
    }
    
    public void setMetodoId(int metodoId) {
        this.metodoId = metodoId;
    }
    
    public Timestamp getPaidAt() {
        return paidAt;
    }
    
    public void setPaidAt(Timestamp paidAt) {
        this.paidAt = paidAt;
    }
    
    // Metodi convenience per oggetti collegati
    public Ordine getOrdine() {
        return ordine;
    }
    
    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
        if (ordine != null) {
            this.orderId = ordine.getId();
        }
    }
    
    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }
    
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
        if (metodoPagamento != null) {
            this.metodoId = metodoPagamento.getId();
        }
    }
    
    // Metodi di utilità
    public String getDescrizionePagamento() {
        String descrizione = "Pagamento #" + id;
        
        if (ordine != null) {
            descrizione += " per Ordine #" + ordine.getId();
        }
        
        if (metodoPagamento != null) {
            descrizione += " con " + metodoPagamento.getTipo();
            if (metodoPagamento.getUltimeQuattroCifre() != null) {
                descrizione += " **** " + metodoPagamento.getUltimeQuattroCifre();
            }
        }
        
        return descrizione;
    }
    
    @Override
    public String toString() {
        return getDescrizionePagamento();
    }
}
