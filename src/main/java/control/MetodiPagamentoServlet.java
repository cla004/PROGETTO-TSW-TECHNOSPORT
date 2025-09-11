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

@WebServlet(name="metodiPagamento", value="/metodiPagamento")
public class MetodiPagamentoServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("loggedInUser");
        
        // Controlla se l'utente è loggato
        if (utente == null) {
            session.setAttribute("errore", "Devi effettuare il login per gestire i metodi di pagamento!");
            response.sendRedirect("Login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        if (action == null) action = "lista";
        
        try {
            switch (action) {
                case "lista":
                    listaMetodi(request, response, utente.getId());
                    break;
                case "aggiungi":
                    mostraFormAggiungi(request, response);
                    break;
                case "modifica":
                    mostraFormModifica(request, response, utente.getId());
                    break;
                case "elimina":
                    eliminaMetodo(request, response, utente.getId());
                    break;
                case "imposta-predefinito":
                    impostaPredefinito(request, response, utente.getId());
                    break;
                default:
                    listaMetodi(request, response, utente.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errore", "Errore del database: " + e.getMessage());
            response.sendRedirect("metodiPagamento");
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
                    salvaMetodo(request, response, utente.getId());
                    break;
                case "aggiorna":
                    aggiornaMetodo(request, response, utente.getId());
                    break;
                default:
                    response.sendRedirect("metodiPagamento");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errore", "Errore del database: " + e.getMessage());
            response.sendRedirect("metodiPagamento");
        }
    }
    
    // Mostra lista metodi di pagamento dell'utente
    private void listaMetodi(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws SQLException, ServletException, IOException {
        
        MetodoPagamentoDao metodoDao = new MetodoPagamentoDao();
        List<MetodoPagamento> metodi = metodoDao.findByUserId(userId);
        
        request.setAttribute("metodi", metodi);
        request.getRequestDispatcher("metodiPagamento.jsp").forward(request, response);
    }
    
    // Mostra form per aggiungere nuovo metodo
    private void mostraFormAggiungi(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("azione", "aggiungi");
        request.getRequestDispatcher("formMetodoPagamento.jsp").forward(request, response);
    }
    
    // Mostra form per modificare metodo esistente
    private void mostraFormModifica(HttpServletRequest request, HttpServletResponse response, int userId)
            throws SQLException, ServletException, IOException {
        
        int metodoId = Integer.parseInt(request.getParameter("id"));
        MetodoPagamentoDao metodoDao = new MetodoPagamentoDao();
        MetodoPagamento metodo = metodoDao.findById(metodoId);
        
        // Verifica che il metodo appartenga all'utente loggato
        if (metodo == null || metodo.getIdUtente() != userId) {
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Metodo di pagamento non trovato o non autorizzato!");
            response.sendRedirect("metodiPagamento");
            return;
        }
        
        request.setAttribute("metodo", metodo);
        request.setAttribute("azione", "modifica");
        request.getRequestDispatcher("formMetodoPagamento.jsp").forward(request, response);
    }
    
    // Salva nuovo metodo di pagamento
    private void salvaMetodo(HttpServletRequest request, HttpServletResponse response, int userId)
            throws SQLException, ServletException, IOException {
        
        // Legge i parametri dal form
        String tipo = request.getParameter("tipo");
        String numeroCarta = request.getParameter("numeroCarta"); // numero completo dal form
        String intestatario = request.getParameter("intestatario");
        String scadenza = request.getParameter("scadenza");
        String predefinito = request.getParameter("predefinito");
        
        // Validazione basilare
        if (tipo == null || tipo.trim().isEmpty() ||
            numeroCarta == null || numeroCarta.trim().isEmpty() ||
            intestatario == null || intestatario.trim().isEmpty() ||
            scadenza == null || scadenza.trim().isEmpty()) {
            
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Tutti i campi sono obbligatori!");
            response.sendRedirect("metodiPagamento?action=aggiungi");
            return;
        }
        
        // Estrae solo le ultime 4 cifre per sicurezza
        String ultimeQuattro = "";
        String numeroSolo = numeroCarta.replaceAll("[^0-9]", ""); // rimuove spazi e caratteri
        if (numeroSolo.length() >= 4) {
            ultimeQuattro = numeroSolo.substring(numeroSolo.length() - 4);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Numero carta non valido!");
            response.sendRedirect("metodiPagamento?action=aggiungi");
            return;
        }
        
        // Crea nuovo metodo
        MetodoPagamento nuovoMetodo = new MetodoPagamento();
        nuovoMetodo.setIdUtente(userId);
        nuovoMetodo.setTipo(tipo.trim());
        nuovoMetodo.setUltimeQuattroCifre(ultimeQuattro);
        nuovoMetodo.setIntestatario(intestatario.trim());
        nuovoMetodo.setScadenza(scadenza.trim());
        nuovoMetodo.setPredefinito("on".equals(predefinito));
        
        // Salva nel database
        MetodoPagamentoDao metodoDao = new MetodoPagamentoDao();
        metodoDao.insert(nuovoMetodo);
        
        HttpSession session = request.getSession();
        session.setAttribute("successo", "Metodo di pagamento aggiunto con successo!");
        response.sendRedirect("metodiPagamento");
    }
    
    // Aggiorna metodo di pagamento esistente
    private void aggiornaMetodo(HttpServletRequest request, HttpServletResponse response, int userId)
            throws SQLException, ServletException, IOException {
        
        int metodoId = Integer.parseInt(request.getParameter("id"));
        
        // Verifica che il metodo appartenga all'utente
        MetodoPagamentoDao metodoDao = new MetodoPagamentoDao();
        MetodoPagamento metodoEsistente = metodoDao.findById(metodoId);
        
        if (metodoEsistente == null || metodoEsistente.getIdUtente() != userId) {
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Metodo di pagamento non trovato o non autorizzato!");
            response.sendRedirect("metodiPagamento");
            return;
        }
        
        // Legge i parametri dal form (solo alcuni campi modificabili)
        String tipo = request.getParameter("tipo");
        String intestatario = request.getParameter("intestatario");
        String scadenza = request.getParameter("scadenza");
        String predefinito = request.getParameter("predefinito");
        
        // Validazione basilare
        if (tipo == null || tipo.trim().isEmpty() ||
            intestatario == null || intestatario.trim().isEmpty() ||
            scadenza == null || scadenza.trim().isEmpty()) {
            
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Tutti i campi sono obbligatori!");
            response.sendRedirect("metodiPagamento?action=modifica&id=" + metodoId);
            return;
        }
        
        // Aggiorna i dati (non modifichiamo le ultime 4 cifre per sicurezza)
        metodoEsistente.setTipo(tipo.trim());
        metodoEsistente.setIntestatario(intestatario.trim());
        metodoEsistente.setScadenza(scadenza.trim());
        metodoEsistente.setPredefinito("on".equals(predefinito));
        
        // Salva nel database
        metodoDao.update(metodoEsistente);
        
        HttpSession session = request.getSession();
        session.setAttribute("successo", "Metodo di pagamento aggiornato con successo!");
        response.sendRedirect("metodiPagamento");
    }
    
    // Elimina metodo di pagamento
    private void eliminaMetodo(HttpServletRequest request, HttpServletResponse response, int userId)
            throws SQLException, ServletException, IOException {
        
        int metodoId = Integer.parseInt(request.getParameter("id"));
        
        // Verifica che il metodo appartenga all'utente
        MetodoPagamentoDao metodoDao = new MetodoPagamentoDao();
        MetodoPagamento metodo = metodoDao.findById(metodoId);
        
        if (metodo == null || metodo.getIdUtente() != userId) {
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Metodo di pagamento non trovato o non autorizzato!");
            response.sendRedirect("metodiPagamento");
            return;
        }
        
        // Elimina il metodo
        metodoDao.delete(metodoId);
        
        HttpSession session = request.getSession();
        session.setAttribute("successo", "Metodo di pagamento eliminato con successo!");
        response.sendRedirect("metodiPagamento");
    }
    
    // Imposta un metodo come predefinito
    private void impostaPredefinito(HttpServletRequest request, HttpServletResponse response, int userId)
            throws SQLException, ServletException, IOException {
        
        int metodoId = Integer.parseInt(request.getParameter("id"));
        
        // Verifica che il metodo appartenga all'utente
        MetodoPagamentoDao metodoDao = new MetodoPagamentoDao();
        MetodoPagamento metodo = metodoDao.findById(metodoId);
        
        if (metodo == null || metodo.getIdUtente() != userId) {
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Metodo di pagamento non trovato!");
            response.sendRedirect("metodiPagamento");
            return;
        }
        
        // Imposta come predefinito
        metodo.setPredefinito(true);
        metodoDao.update(metodo);
        
        HttpSession session = request.getSession();
        session.setAttribute("successo", "Metodo di pagamento impostato come predefinito!");
        response.sendRedirect("metodiPagamento");
    }
}
