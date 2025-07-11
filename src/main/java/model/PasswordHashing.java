
package model; // Package di utilità, contiene classi riutilizzabili

import java.nio.charset.StandardCharsets;            // Per specificare la codifica UTF-8
import java.security.MessageDigest;                  // Classe per calcolare l'hash
import java.security.NoSuchAlgorithmException;       // Eccezione se l'algoritmo non esiste

/**
 * Classe di utilità per l'hashing delle password usando SHA-512.
 */
public class PasswordHashing {

    /**
     * Genera l'hash SHA-512 di una password in input.
     *
     * @param password la password in chiaro da hashare
     * @return una stringa esadecimale rappresentante l'hash
     */
    public static String toHash(String password) {
        String hashString = null; // Stringa che conterrà il risultato finale

        try {
            // Ottiene un'istanza di MessageDigest che usa SHA-512
            MessageDigest digest = MessageDigest.getInstance("SHA-512");

            // Converte la password in un array di byte usando UTF-8 e calcola l'hash
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Inizializza la stringa dell'hash
            hashString = "";

            // Cicla su ogni byte dell'hash per convertirlo in una rappresentazione esadecimale
            for (int i = 0; i < hash.length; i++) {
                // Appende i due caratteri esadecimali corrispondenti al byte corrente
                // (hash[i] & 0xFF) rende il byte positivo
                // | 0x100 garantisce che ci siano almeno 3 cifre in esadecimale
                // substring(1, 3) prende solo le ultime due cifre (senza 0 iniziale)
                hashString += Integer.toHexString((hash[i] & 0xFF) | 0x100).substring(1, 3);
            }

        } catch (NoSuchAlgorithmException e) {
            // Gestisce il caso in cui l'algoritmo SHA-512 non è supportato
            System.out.println(e);
        }

        // Ritorna l'hash sotto forma di stringa esadecimale
        return hashString;
    }
}


public static boolean verify(String passwordInput, String storedHash) {
    // Calcola l'hash della password inserita dall'utente usando lo stesso algoritmo (SHA-512)
    String inputHash = toHash(passwordInput);

    // Confronta l'hash calcolato della password inserita con l'hash salvato nel database
    // Se coincidono, la password è corretta e il metodo restituisce true
    // Altrimenti restituisce false
    return inputHash.equals(storedHash);
}