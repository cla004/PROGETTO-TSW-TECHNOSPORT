package control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import model.Prodotti;
import model.ProdottiDao;
import model.Prodotto_taglia;
import model.ProdottoTagliaDao;

@WebServlet("/admin/prodotti")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB max
public class AdminProdottiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        
        // Controllo accesso admin
        if (isAdmin == null || !isAdmin) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("aggiungi".equals(action)) {
            aggiungiProdotto(request, response);
        } else if ("modifica".equals(action)) {
            modificaProdotto(request, response);
        } else {
            request.setAttribute("errore", "Azione non riconosciuta");
            request.getRequestDispatcher("/admin/aggiungi-prodotto.jsp").forward(request, response);
        }
    }
    
    /**
     * Aggiunge un nuovo prodotto con la taglia selezionata
     */
    private void aggiungiProdotto(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Raccogli parametri dal form
            String nome = request.getParameter("nome");
            String descrizione = request.getParameter("descrizione");
            String prezzoStr = request.getParameter("prezzo");
            String categoriaStr = request.getParameter("categoria");
            String stockStr = request.getParameter("stock");
            String quantitaTagliaStr = request.getParameter("quantitaTaglia");
            String tagliaIdStr = request.getParameter("tagliaId");
            
            // Validazione parametri obbligatori
            if (nome == null || nome.trim().isEmpty() ||
                prezzoStr == null || prezzoStr.trim().isEmpty() ||
                categoriaStr == null || categoriaStr.trim().isEmpty() ||
                stockStr == null || stockStr.trim().isEmpty() ||
                quantitaTagliaStr == null || quantitaTagliaStr.trim().isEmpty() ||
                tagliaIdStr == null || tagliaIdStr.trim().isEmpty()) {
                
                request.setAttribute("errore", "Tutti i campi obbligatori devono essere compilati");
                request.getRequestDispatcher("/admin/aggiungi-prodotto.jsp").forward(request, response);
                return;
            }
            
            // Conversioni
            double prezzo = Double.parseDouble(prezzoStr);
            int categoriaId = Integer.parseInt(categoriaStr);
            int stock = Integer.parseInt(stockStr);
            int quantitaTaglia = Integer.parseInt(quantitaTagliaStr);
            int tagliaId = Integer.parseInt(tagliaIdStr);
            
            // Validazione: quantità taglia non può superare stock totale
            if (quantitaTaglia > stock) {
                request.setAttribute("errore", "La quantità per questa taglia (" + quantitaTaglia + ") non può superare lo stock totale (" + stock + ")");
                request.getRequestDispatcher("/admin/aggiungi-prodotto.jsp").forward(request, response);
                return;
            }
            
            // Gestione immagine (opzionale)
            String nomeImmagine = "";
            Part imagePart = request.getPart("immagine");
            if (imagePart != null && imagePart.getSize() > 0) {
                // Estrae il nome originale del file
                String nomeFileOriginale = imagePart.getSubmittedFileName();
                
                if (nomeFileOriginale != null && !nomeFileOriginale.isEmpty()) {
                    // Rimuove l'estensione e aggiunge .jpg
                    String nomeFile;
                    if (nomeFileOriginale.contains(".")) {
                        nomeFile = nomeFileOriginale.substring(0, nomeFileOriginale.lastIndexOf('.'));
                    } else {
                        nomeFile = nomeFileOriginale;
                    }
                    String nomeFileFinal = nomeFile + ".jpg";
                    
                    // Percorso della cartella images
                    String uploadPath = getServletContext().getRealPath("/images");
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }
                    
                    // Salva fisicamente il file usando write()
                    String percorsoCompleto = uploadPath + File.separator + nomeFileFinal;
                    imagePart.write(percorsoCompleto);
                    
                    // Nome da salvare nel database con il percorso
                    nomeImmagine = "images/" + nomeFileFinal;
                }
            }
            
            // Crea oggetto prodotto
            Prodotti prodotto = new Prodotti();
            prodotto.setNome(nome);
            prodotto.setDescrizione(descrizione);
            prodotto.setPrezzo(prezzo);
            prodotto.setQuantita_disponibili(String.valueOf(stock));
            prodotto.setImmagine(nomeImmagine);
            prodotto.setId_categoria(categoriaId);
            
            // Salva prodotto nel database
            ProdottiDao prodottiDao = new ProdottiDao();
            int prodottoId = prodottiDao.inserisciProdottoConId(prodotto);
            
            if (prodottoId > 0) {
                // Crea associazione prodotto-taglia con la quantità specifica per quella taglia
                Prodotto_taglia pt = new Prodotto_taglia(prodottoId, tagliaId, quantitaTaglia);
                ProdottoTagliaDao ptDao = new ProdottoTagliaDao();
                ptDao.inserisci(pt);
                
                // Successo
                request.setAttribute("successo", "Prodotto aggiunto con successo! ID: " + prodottoId);
            } else {
                request.setAttribute("errore", "Errore nell'inserimento del prodotto");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("errore", "Formato numeri non valido. Controlla prezzo, categoria e quantità");
            e.printStackTrace();
        } catch (Exception e) {
            request.setAttribute("errore", "Errore nell'aggiunta del prodotto: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Redirect alla pagina di aggiunta prodotto
        request.getRequestDispatcher("/admin/aggiungi-prodotto.jsp").forward(request, response);
    }
    
    /**
     * Modifica un prodotto esistente
     */
    private void modificaProdotto(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Raccogli parametri dal form
            String idStr = request.getParameter("id");
            String nome = request.getParameter("nome");
            String descrizione = request.getParameter("descrizione");
            String prezzoStr = request.getParameter("prezzo");
            String categoriaStr = request.getParameter("categoria");
            String stockStr = request.getParameter("stock");
            
            // Validazione parametri obbligatori
            if (idStr == null || idStr.trim().isEmpty() ||
                nome == null || nome.trim().isEmpty() ||
                prezzoStr == null || prezzoStr.trim().isEmpty() ||
                categoriaStr == null || categoriaStr.trim().isEmpty() ||
                stockStr == null || stockStr.trim().isEmpty()) {
                
                request.setAttribute("errore", "Tutti i campi obbligatori devono essere compilati");
                request.getRequestDispatcher("/admin/adminEditProdotto.jsp?id=" + idStr).forward(request, response);
                return;
            }
            
            // Conversioni
            int prodottoId = Integer.parseInt(idStr);
            double prezzo = Double.parseDouble(prezzoStr);
            int categoriaId = Integer.parseInt(categoriaStr);
            int stock = Integer.parseInt(stockStr);
            
            // Carica il prodotto esistente
            ProdottiDao prodottiDao = new ProdottiDao();
            Prodotti prodotto = prodottiDao.cercaProdottoById(prodottoId);
            
            if (prodotto == null) {
                request.setAttribute("errore", "Prodotto non trovato");
                request.getRequestDispatcher("/admin/catalogo.jsp").forward(request, response);
                return;
            }
            
            // Gestione immagine (opzionale - mantiene quella esistente se non caricata)
            String nomeImmagine = prodotto.getImmagine(); // Mantiene l'immagine esistente
            Part imagePart = request.getPart("immagine");
            if (imagePart != null && imagePart.getSize() > 0) {
                // Estrae il nome originale del file
                String nomeFileOriginale = imagePart.getSubmittedFileName();
                
                if (nomeFileOriginale != null && !nomeFileOriginale.isEmpty()) {
                    // Rimuove l'estensione e aggiunge .jpg
                    String nomeFile;
                    if (nomeFileOriginale.contains(".")) {
                        nomeFile = nomeFileOriginale.substring(0, nomeFileOriginale.lastIndexOf('.'));
                    } else {
                        nomeFile = nomeFileOriginale;
                    }
                    String nomeFileFinal = nomeFile + ".jpg";
                    
                    // Percorso della cartella images
                    String uploadPath = getServletContext().getRealPath("/images");
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }
                    
                    // Salva fisicamente il file usando write()
                    String percorsoCompleto = uploadPath + File.separator + nomeFileFinal;
                    imagePart.write(percorsoCompleto);
                    
                    // Nome da salvare nel database con il percorso
                    nomeImmagine = "images/" + nomeFileFinal;
                }
            }
            
            // Aggiorna oggetto prodotto
            prodotto.setNome(nome);
            prodotto.setDescrizione(descrizione);
            prodotto.setPrezzo(prezzo);
            prodotto.setQuantita_disponibili(String.valueOf(stock));
            prodotto.setImmagine(nomeImmagine);
            prodotto.setId_categoria(categoriaId);
            
            // Salva modifiche nel database
            prodottiDao.aggiornaProdotto(prodotto);
            
            // Successo
            request.setAttribute("successo", "Prodotto modificato con successo!");
            
        } catch (NumberFormatException e) {
            request.setAttribute("errore", "Formato numeri non valido. Controlla prezzo, categoria e quantit\u00e0");
            e.printStackTrace();
        } catch (Exception e) {
            request.setAttribute("errore", "Errore nella modifica del prodotto: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Redirect alla pagina di modifica prodotto
        String idStr = request.getParameter("id");
        request.getRequestDispatcher("/admin/adminEditProdotto.jsp?id=" + idStr).forward(request, response);
    }
}
