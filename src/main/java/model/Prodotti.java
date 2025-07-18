package model;

public class Prodotti {
    private int id_prodotto;
    private String nome;
    private byte[] immagine; // BLOB
    private double prezzo;
    private String descrizione;
    private String quantit�_disponibili;
    private int id_categoria;

    public Prodotti() {
        // Costruttore di default
    }

    public Prodotti(int id_prodotto, String nome, byte[] immagine, double prezzo, String descrizione, String quantit�_disponibili, int id_categoria) {
        this.id_prodotto = id_prodotto;
        this.nome = nome;
        this.immagine = immagine;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.quantit�_disponibili = quantit�_disponibili;
        this.id_categoria = id_categoria;
    }

    // Getters e Setters

    public int getId_prodotto() {
        return id_prodotto;
    }

    public void setId_prodotto(int id_prodotto) {
        this.id_prodotto = id_prodotto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getImmagine() {
        return immagine;
    }

    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getQuantit�_disponibili() {
        return quantit�_disponibili;
    }

    public void setQuantit�_disponibili(String quantit�_disponibili) {
        this.quantit�_disponibili = quantit�_disponibili;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }
}
