package model;

import java.sql.Timestamp;

/**
 * Classe che rappresenta il carrello principale dell'utente (tabella cart)
 */
public class Carrello {
    private int id;
    private int userId;
    private Timestamp createdAt;
    
    public Carrello() {
    }
    
    public Carrello(int id, int userId, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
