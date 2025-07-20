


package model; // Package di utilit�, contiene classi riutilizzabili

import java.nio.charset.StandardCharsets;            // Per specificare la codifica UTF-8
import java.security.MessageDigest;                  // Classe per calcolare l'hash
import java.security.NoSuchAlgorithmException;       // Eccezione se l'algoritmo non esiste

/**
 * Classe di utilit� per l'hashing delle password usando SHA-512.
 */

public class PasswordHashing{
public static String toHash(String password) {
    // Iniziamo con una stringa vuota dove metteremo il risultato finale
    String hashString = "";

    try {
        // Creiamo un oggetto che calcola l'hash usando SHA-512
        MessageDigest digest = MessageDigest.getInstance("SHA-512");

        // Prendiamo la password e la trasformiamo in una serie di byte (usando UTF-8)
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        // Per ogni byte che abbiamo calcolato facciamo questo:
        for (int i = 0; i < hash.length; i++) {
            // Convertiamo il byte in un numero esadecimale a 2 cifre e lo aggiungiamo alla stringa
            // (hash[i] & 0xFF) serve a evitare numeri negativi
            // | 0x100 serve a garantire almeno 3 cifre esadecimali (es. 0x1ff)
            // .substring(1, 3) prende solo le ultime 2 cifre esadecimali (es. "ff")
            hashString += Integer.toHexString((hash[i] & 0xFF) | 0x100).substring(1, 3);
        }

    } catch (NoSuchAlgorithmException e) {
        // Se SHA-512 non esiste (molto improbabile), stampiamo l'errore
        System.out.println(e);
    }

    // Torniamo la stringa esadecimale che rappresenta l'hash della password
    return hashString;
}



public static boolean verify(String passwordInput, String storedHash) {
    // Calcola l'hash della password inserita dall'utente usando lo stesso algoritmo (SHA-512)
    String inputHash = toHash(passwordInput);

    // Confronta l'hash calcolato della password inserita con l'hash salvato nel database
    // Se coincidono, la password � corretta e il metodo restituisce true
    // Altrimenti restituisce false
    return inputHash.equals(storedHash);
}
}
