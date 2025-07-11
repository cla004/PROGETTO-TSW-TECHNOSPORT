package model;

import java.io.Serializable;

public class Utente implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;      
    private String cognome;
    private String username;
    private String email;
    private String password;
  

    public Utente() {
    }

    public Utente(String id,String nome, String cognome, String email, String password) {
    	this.id=id;        
    	this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
       
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
����}


}