package control;

import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Ordine;
import model.OrdineDao;

@WebServlet("/admin/ordini")
public class AdminOrdiniServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        
        // Controllo accesso admin
        if (isAdmin == null || !isAdmin) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");
        String nuovoStato = request.getParameter("stato");
        
        if ("aggiorna".equals(action) && idParam != null) {
            try {
                int idOrdine = Integer.parseInt(idParam);
                
                if (nuovoStato != null && !nuovoStato.trim().isEmpty()) {
                    // Aggiorna con lo stato selezionato
                    aggiornaStatoOrdine(idOrdine, nuovoStato);
                } else {
                    throw new Exception("Stato non specificato");
                }
                
                // Redirect con messaggio di successo
                response.sendRedirect(request.getContextPath() + "/admin/ordini.jsp?aggiornato=success");
                
            } catch (Exception e) {
                e.printStackTrace();
                // Redirect con messaggio di errore
                response.sendRedirect(request.getContextPath() + "/admin/ordini.jsp?errore=update_failed");
            }
        } else {
            // Parametri mancanti o non validi
            response.sendRedirect(request.getContextPath() + "/admin/ordini.jsp");
        }
    }
    
    /**
     * Aggiorna lo stato dell'ordine con lo stato specificato
     */
    private void aggiornaStatoOrdine(int idOrdine, String nuovoStato) throws Exception {
        // Valida lo stato richiesto
        if (!isStatoValido(nuovoStato)) {
            throw new Exception("Stato non valido: " + nuovoStato);
        }
        
        OrdineDao ordineDao = new OrdineDao();
        
        // Verifica che l'ordine esista
        Ordine ordine = ordineDao.getOrdineById(idOrdine);
        if (ordine == null) {
            throw new Exception("Ordine non trovato");
        }
        
        // Verifica che l'ordine possa essere modificato
        String statoAttuale = ordine.getStato();
        if ("CONSEGNATO".equals(statoAttuale) || "ANNULLATO".equals(statoAttuale)) {
            throw new Exception("Non è possibile modificare un ordine " + statoAttuale.toLowerCase());
        }
        
        // Aggiorna lo stato nel database
        ordineDao.aggiornaStatoOrdine(idOrdine, nuovoStato);
    }
    
    /**
     * Valida se lo stato richiesto è consentito
     */
    private boolean isStatoValido(String stato) {
        return "PAGATO".equals(stato) || 
               "IN_LAVORAZIONE".equals(stato) || 
               "SPEDITO".equals(stato) || 
               "CONSEGNATO".equals(stato);
    }
    
}
