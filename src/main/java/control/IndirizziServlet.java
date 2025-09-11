package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name="indirizzi", value="/indirizzi")
public class IndirizziServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("loggedInUser");
        
        // Controlla se l'utente è loggato
        if (utente == null) {
            session.setAttribute("errore", "Devi effettuare il login per gestire gli indirizzi!");
            response.sendRedirect("Login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        if (action == null) action = "lista";
        
        try {
            switch (action) {
                case "lista":
                    listaIndirizzi(request, response, utente.getId());
                    break;
                case "aggiungi":
                    mostraFormAggiungi(request, response);
                    break;
                case "modifica":
                    mostraFormModifica(request, response, utente.getId());
                    break;
                case "elimina":
                    eliminaIndirizzo(request, response, utente.getId());
                    break;
                default:
                    listaIndirizzi(request, response, utente.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errore", "Errore del database: " + e.getMessage());
            response.sendRedirect("indirizzi");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("loggedInUser");
        
        if (utente == null) {
            session.setAttribute("errore", "Devi effettuare il login!");
            response.sendRedirect("Login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            switch (action) {
                case "salva":
                    salvaIndirizzo(request, response, utente.getId());
                    break;
                case "aggiorna":
                    aggiornaIndirizzo(request, response, utente.getId());
                    break;
                case "elimina":
                    eliminaIndirizzo(request, response, utente.getId());
                    break;
                default:
                    response.sendRedirect("indirizzi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errore", "Errore del database: " + e.getMessage());
            response.sendRedirect("indirizzi");
        }
    }
    
    // Mostra lista indirizzi dell'utente
    private void listaIndirizzi(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws SQLException, ServletException, IOException {
        
        IndirizzoDao indirizzoDao = new IndirizzoDao();
        List<Indirizzo> indirizzi = indirizzoDao.findByUserId(userId);
        
        request.setAttribute("indirizzi", indirizzi);
        request.getRequestDispatcher("indirizzi.jsp").forward(request, response);
    }
    
    // Mostra form per aggiungere nuovo indirizzo
    private void mostraFormAggiungi(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("azione", "aggiungi");
        request.getRequestDispatcher("formIndirizzo.jsp").forward(request, response);
    }
    
    // Mostra form per modificare indirizzo esistente
    private void mostraFormModifica(HttpServletRequest request, HttpServletResponse response, int userId)
            throws SQLException, ServletException, IOException {
        
        int indirizzoId = Integer.parseInt(request.getParameter("id"));
        IndirizzoDao indirizzoDao = new IndirizzoDao();
        Indirizzo indirizzo = indirizzoDao.findById(indirizzoId);
        
        // Verifica che l'indirizzo appartenga all'utente loggato
        if (indirizzo == null || indirizzo.getIdUtente() != userId) {
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Indirizzo non trovato o non autorizzato!");
            response.sendRedirect("indirizzi");
            return;
        }
        
        request.setAttribute("indirizzo", indirizzo);
        request.setAttribute("azione", "modifica");
        request.getRequestDispatcher("formIndirizzo.jsp").forward(request, response);
    }
    
    // Salva nuovo indirizzo
    private void salvaIndirizzo(HttpServletRequest request, HttpServletResponse response, int userId)
            throws SQLException, ServletException, IOException {
        
        // Legge i parametri dal form
        String via = request.getParameter("via");
        String citta = request.getParameter("citta");
        String cap = request.getParameter("cap");
        String provincia = request.getParameter("provincia");
        String paese = request.getParameter("paese");
        
        // Validazione basilare
        if (via == null || via.trim().isEmpty() ||
            citta == null || citta.trim().isEmpty() ||
            cap == null || cap.trim().isEmpty() ||
            provincia == null || provincia.trim().isEmpty() ||
            paese == null || paese.trim().isEmpty()) {
            
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Tutti i campi sono obbligatori!");
            response.sendRedirect("indirizzi?action=aggiungi");
            return;
        }
        
        // Crea nuovo indirizzo
        Indirizzo nuovoIndirizzo = new Indirizzo();
        nuovoIndirizzo.setIdUtente(userId);
        nuovoIndirizzo.setVia(via.trim());
        nuovoIndirizzo.setCitta(citta.trim());
        nuovoIndirizzo.setCap(cap.trim());
        nuovoIndirizzo.setProvincia(provincia.trim());
        nuovoIndirizzo.setPaese(paese.trim());
        
        // Salva nel database
        IndirizzoDao indirizzoDao = new IndirizzoDao();
        indirizzoDao.insert(nuovoIndirizzo);
        
        HttpSession session = request.getSession();
        session.setAttribute("successo", "Indirizzo aggiunto con successo!");
        response.sendRedirect("indirizzi");
    }
    
    // Aggiorna indirizzo esistente
    private void aggiornaIndirizzo(HttpServletRequest request, HttpServletResponse response, int userId)
            throws SQLException, ServletException, IOException {
        
        int indirizzoId = Integer.parseInt(request.getParameter("id"));
        
        // Verifica che l'indirizzo appartenga all'utente
        IndirizzoDao indirizzoDao = new IndirizzoDao();
        Indirizzo indirizzoEsistente = indirizzoDao.findById(indirizzoId);
        
        if (indirizzoEsistente == null || indirizzoEsistente.getIdUtente() != userId) {
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Indirizzo non trovato o non autorizzato!");
            response.sendRedirect("indirizzi");
            return;
        }
        
        // Legge i parametri dal form
        String via = request.getParameter("via");
        String citta = request.getParameter("citta");
        String cap = request.getParameter("cap");
        String provincia = request.getParameter("provincia");
        String paese = request.getParameter("paese");
        
        // Validazione basilare
        if (via == null || via.trim().isEmpty() ||
            citta == null || citta.trim().isEmpty() ||
            cap == null || cap.trim().isEmpty() ||
            provincia == null || provincia.trim().isEmpty() ||
            paese == null || paese.trim().isEmpty()) {
            
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Tutti i campi sono obbligatori!");
            response.sendRedirect("indirizzi?action=modifica&id=" + indirizzoId);
            return;
        }
        
        // Aggiorna i dati
        indirizzoEsistente.setVia(via.trim());
        indirizzoEsistente.setCitta(citta.trim());
        indirizzoEsistente.setCap(cap.trim());
        indirizzoEsistente.setProvincia(provincia.trim());
        indirizzoEsistente.setPaese(paese.trim());
        
        // Salva nel database
        indirizzoDao.update(indirizzoEsistente);
        
        HttpSession session = request.getSession();
        session.setAttribute("successo", "Indirizzo aggiornato con successo!");
        response.sendRedirect("indirizzi");
    }
    
    // Elimina indirizzo
    private void eliminaIndirizzo(HttpServletRequest request, HttpServletResponse response, int userId)
            throws SQLException, ServletException, IOException {
        
        int indirizzoId = Integer.parseInt(request.getParameter("id"));
        
        // Verifica che l'indirizzo appartenga all'utente
        IndirizzoDao indirizzoDao = new IndirizzoDao();
        Indirizzo indirizzo = indirizzoDao.findById(indirizzoId);
        
        if (indirizzo == null || indirizzo.getIdUtente() != userId) {
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Indirizzo non trovato o non autorizzato!");
            response.sendRedirect("indirizzi");
            return;
        }
        
        // Elimina l'indirizzo
        indirizzoDao.delete(indirizzoId);
        
        HttpSession session = request.getSession();
        session.setAttribute("successo", "Indirizzo eliminato con successo!");
        response.sendRedirect("indirizzi");
    }
}
