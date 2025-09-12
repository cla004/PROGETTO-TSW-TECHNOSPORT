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

@WebServlet(name="checkout", value="/checkout")
public class CheckoutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("loggedInUser");
        
        // Controlla se l'utente è loggato
        if (utente == null) {
            session.setAttribute("errore", "Devi effettuare il login per procedere all'ordine!");
            response.sendRedirect("login?redirect=checkout");
            return;
        }
        
        // Controlla che ci sia un carrello
        List<CartItem> carrello = (List<CartItem>) session.getAttribute("carrelloSessione");
        if (carrello == null || carrello.isEmpty()) {
            session.setAttribute("errore", "Il carrello è vuoto!");
            response.sendRedirect("carrello?action=visualizza");
            return;
        }
        
        String step = request.getParameter("step");
        if (step == null) step = "indirizzo";
        
        try {
            switch (step) {
                case "indirizzo":
                    mostraSceltaIndirizzo(request, response, utente.getId());
                    break;
                case "pagamento":
                    mostraSceltaPagamento(request, response, utente.getId());
                    break;
                case "riepilogo":
                    mostraRiepilogo(request, response, utente.getId());
                    break;
                default:
                    mostraSceltaIndirizzo(request, response, utente.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errore", "Errore del database: " + e.getMessage());
            response.sendRedirect("carrello?action=visualizza");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("loggedInUser");
        
        if (utente == null) {
            response.sendRedirect("login?redirect=checkout");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            switch (action) {
                case "salva-indirizzo":
                    salvaIndirizzoCheckout(request, response, utente.getId());
                    break;
                case "conferma-indirizzo":
                    confermaIndirizzo(request, response);
                    break;
                case "salva-pagamento":
                    salvaPagamentoCheckout(request, response, utente.getId());
                    break;
                case "conferma-pagamento":
                    confermaPagamento(request, response);
                    break;
                case "finalizza-ordine":
                    finalizzaOrdine(request, response, utente.getId());
                    break;
                default:
                    response.sendRedirect("checkout");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errore", "Errore: " + e.getMessage());
            response.sendRedirect("checkout");
        }
    }
    
    // STEP 1: Mostra selezione indirizzo
    private void mostraSceltaIndirizzo(HttpServletRequest request, HttpServletResponse response, int userId)
            throws SQLException, ServletException, IOException {
        
        IndirizzoDao indirizzoDao = new IndirizzoDao();
        List<Indirizzo> indirizzi = indirizzoDao.findByUserId(userId);
        
        request.setAttribute("indirizzi", indirizzi);
        request.setAttribute("step", "indirizzo");
        request.getRequestDispatcher("checkoutIndirizzo.jsp").forward(request, response);
    }
    
    // STEP 2: Mostra selezione metodo pagamento
    private void mostraSceltaPagamento(HttpServletRequest request, HttpServletResponse response, int userId)
            throws SQLException, ServletException, IOException {
        
        // Verifica che sia stato selezionato un indirizzo
        HttpSession session = request.getSession();
        if (session.getAttribute("indirizzoSelezionato") == null) {
            session.setAttribute("errore", "Seleziona prima un indirizzo di spedizione!");
            response.sendRedirect("checkout?step=indirizzo");
            return;
        }
        
        MetodoPagamentoDao metodiDao = new MetodoPagamentoDao();
        List<MetodoPagamento> metodi = metodiDao.findByUserId(userId);
        
        request.setAttribute("metodi", metodi);
        request.setAttribute("step", "pagamento");
        request.getRequestDispatcher("checkoutPagamento.jsp").forward(request, response);
    }
    
    // STEP 3: Mostra riepilogo ordine
    private void mostraRiepilogo(HttpServletRequest request, HttpServletResponse response, int userId)
            throws SQLException, ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Verifica che siano stati selezionati indirizzo e pagamento
        if (session.getAttribute("indirizzoSelezionato") == null || 
            session.getAttribute("pagamentoSelezionato") == null) {
            session.setAttribute("errore", "Completa prima indirizzo e metodo di pagamento!");
            response.sendRedirect("checkout");
            return;
        }
        
        request.setAttribute("step", "riepilogo");
        request.getRequestDispatcher("riepilogoOrdine.jsp").forward(request, response);
    }
    
    // Salva nuovo indirizzo durante checkout
    private void salvaIndirizzoCheckout(HttpServletRequest request, HttpServletResponse response, int userId)
            throws SQLException, ServletException, IOException {
        
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
            response.sendRedirect("checkout?step=indirizzo");
            return;
        }
        
        // Crea e salva nuovo indirizzo
        Indirizzo nuovoIndirizzo = new Indirizzo();
        nuovoIndirizzo.setIdUtente(userId);
        nuovoIndirizzo.setVia(via.trim());
        nuovoIndirizzo.setCitta(citta.trim());
        nuovoIndirizzo.setCap(cap.trim());
        nuovoIndirizzo.setProvincia(provincia.trim());
        nuovoIndirizzo.setPaese(paese.trim());
        
        IndirizzoDao indirizzoDao = new IndirizzoDao();
        indirizzoDao.insert(nuovoIndirizzo);
        
        // Imposta come selezionato
        HttpSession session = request.getSession();
        session.setAttribute("indirizzoSelezionato", nuovoIndirizzo);
        session.setAttribute("successo", "Nuovo indirizzo aggiunto e selezionato!");
        
        response.sendRedirect("checkout?step=pagamento");
    }
    
    // Conferma indirizzo esistente
    private void confermaIndirizzo(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        
        int indirizzoId = Integer.parseInt(request.getParameter("indirizzoId"));
        
        IndirizzoDao indirizzoDao = new IndirizzoDao();
        Indirizzo indirizzo = indirizzoDao.findById(indirizzoId);
        
        if (indirizzo == null) {
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Indirizzo non trovato!");
            response.sendRedirect("checkout?step=indirizzo");
            return;
        }
        
        HttpSession session = request.getSession();
        session.setAttribute("indirizzoSelezionato", indirizzo);
        response.sendRedirect("checkout?step=pagamento");
    }
    
    // Salva nuovo metodo di pagamento durante checkout
    private void salvaPagamentoCheckout(HttpServletRequest request, HttpServletResponse response, int userId)
            throws SQLException, ServletException, IOException {
        
        String tipo = request.getParameter("tipo");
        String numeroCarta = request.getParameter("numeroCarta");
        String intestatario = request.getParameter("intestatario");
        String scadenza = request.getParameter("scadenza");
        
        // Validazione basilare
        if (tipo == null || tipo.trim().isEmpty() ||
            numeroCarta == null || numeroCarta.trim().isEmpty() ||
            intestatario == null || intestatario.trim().isEmpty() ||
            scadenza == null || scadenza.trim().isEmpty()) {
            
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Tutti i campi sono obbligatori!");
            response.sendRedirect("checkout?step=pagamento");
            return;
        }
        
        // Estrae ultime 4 cifre
        String numeroSolo = numeroCarta.replaceAll("[^0-9]", "");
        if (numeroSolo.length() < 4) {
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Numero carta non valido!");
            response.sendRedirect("checkout?step=pagamento");
            return;
        }
        String ultimeQuattro = numeroSolo.substring(numeroSolo.length() - 4);
        
        // Crea e salva nuovo metodo
        MetodoPagamento nuovoMetodo = new MetodoPagamento();
        nuovoMetodo.setIdUtente(userId);
        nuovoMetodo.setTipo(tipo.trim());
        nuovoMetodo.setUltimeQuattroCifre(ultimeQuattro);
        nuovoMetodo.setIntestatario(intestatario.trim());
        nuovoMetodo.setScadenza(scadenza.trim());
        nuovoMetodo.setPredefinito(false); // Non predefinito di default
        
        MetodoPagamentoDao metodiDao = new MetodoPagamentoDao();
        metodiDao.insert(nuovoMetodo);
        
        // Imposta come selezionato
        HttpSession session = request.getSession();
        session.setAttribute("pagamentoSelezionato", nuovoMetodo);
        session.setAttribute("successo", "Nuovo metodo di pagamento aggiunto e selezionato!");
        
        response.sendRedirect("checkout?step=riepilogo");
    }
    
    // Conferma metodo di pagamento esistente
    private void confermaPagamento(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        
        int metodoPagamentoId = Integer.parseInt(request.getParameter("metodoPagamentoId"));
        
        MetodoPagamentoDao metodiDao = new MetodoPagamentoDao();
        MetodoPagamento metodo = metodiDao.findById(metodoPagamentoId);
        
        if (metodo == null) {
            HttpSession session = request.getSession();
            session.setAttribute("errore", "Metodo di pagamento non trovato!");
            response.sendRedirect("checkout?step=pagamento");
            return;
        }
        
        HttpSession session = request.getSession();
        session.setAttribute("pagamentoSelezionato", metodo);
        response.sendRedirect("checkout?step=riepilogo");
    }
    
    // Finalizza ordine (crea ordine nel database e chiama PagamentoServlet)
    private void finalizzaOrdine(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Indirizzo indirizzo = (Indirizzo) session.getAttribute("indirizzoSelezionato");
        MetodoPagamento pagamento = (MetodoPagamento) session.getAttribute("pagamentoSelezionato");
        
        if (indirizzo == null || pagamento == null) {
            session.setAttribute("errore", "Dati mancanti per finalizzare l'ordine!");
            response.sendRedirect("checkout");
            return;
        }
        
        try {
            // Ottieni carrello (prova database, fallback su sessione)
            List<CartItem> carrello = ottieniCarrello(session, userId);
            
            if (carrello == null || carrello.isEmpty()) {
                session.setAttribute("errore", "Carrello vuoto!");
                response.sendRedirect("carrello?action=visualizza");
                return;
            }
            
            // Calcola totale
            double totale = calcolaTotaleCarrello(carrello);
            
            // Crea ordine
            Ordine ordine = new Ordine();
            ordine.setIdUtente(userId);
            ordine.setIdIndirizzo(indirizzo.getId());
            ordine.setIdMetodo(pagamento.getId());
            ordine.setTotale(totale);
            ordine.setStato("In attesa di pagamento");
            
            OrdineDao ordineDao = new OrdineDao();
            ordineDao.inserisciOrdine(ordine); // Imposta anche l'ID generato
            
            // Crea dettagli ordine
            creaDettagliOrdine(ordine.getId(), carrello);
            
            // Passa l'ordine al PagamentoServlet
            request.setAttribute("indirizzoOrdine", indirizzo);
            request.setAttribute("metodoPagamentoOrdine", pagamento);
            request.setAttribute("ordineCreato", ordine);
            
            // Inoltra al PagamentoServlet per il pagamento
            request.getRequestDispatcher("/pagamento").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errore", "Errore nella creazione dell'ordine: " + e.getMessage());
            response.sendRedirect("checkout");
        }
    }
    
    /**
     * Ottiene il carrello dell'utente (database o sessione)
     */
    private List<CartItem> ottieniCarrello(HttpSession session, int userId) throws SQLException {
        try {
            // Prova dal database
            CartItemDao cartDao = new CartItemDao();
            List<CartItem> carrelloDb = cartDao.findByUserId(userId);
            
            if (carrelloDb != null && !carrelloDb.isEmpty()) {
                return carrelloDb;
            }
        } catch (Exception e) {
            System.err.println("Errore lettura carrello DB: " + e.getMessage());
        }
        
        // Fallback su sessione
        return (List<CartItem>) session.getAttribute("carrelloSessione");
    }
    
    /**
     * Calcola il totale del carrello
     */
    private double calcolaTotaleCarrello(List<CartItem> carrello) throws SQLException {
        double totale = 0.0;
        ProdottiDao prodottoDao = new ProdottiDao();
        
        for (CartItem item : carrello) {
            Prodotti prodotto;
            if (item.getProdotto() != null) {
                prodotto = item.getProdotto();
            } else {
                prodotto = prodottoDao.cercaProdottoById(item.getProductId());
                item.setProdotto(prodotto);
            }
            
            if (prodotto != null) {
                totale += prodotto.getPrezzo() * item.getQuantity();
            }
        }
        return totale;
    }
    
    /**
     * Crea i dettagli dell'ordine nella tabella order_items
     */
    private void creaDettagliOrdine(int ordineId, List<CartItem> carrello) throws SQLException {
        DettaglioOrdineDao dettaglioDao = new DettaglioOrdineDao();
        ProdottiDao prodottoDao = new ProdottiDao();
        
        for (CartItem item : carrello) {
            Prodotti prodotto;
            if (item.getProdotto() != null) {
                prodotto = item.getProdotto();
            } else {
                prodotto = prodottoDao.cercaProdottoById(item.getProductId());
            }
            
            if (prodotto != null) {
                DettaglioOrdine dettaglio = new DettaglioOrdine();
                dettaglio.setOrderId(ordineId);
                dettaglio.setProductId(item.getProductId());
                dettaglio.setQuantity(item.getQuantity());
                dettaglio.setPrice(prodotto.getPrezzo());
                dettaglio.setTagliaId(item.getTagliaId());
                dettaglio.setNomeProdottoSalvato(prodotto.getNome()); // Salva il nome per preservarlo
                dettaglioDao.inserisciDettaglio(dettaglio);
            }
        }
    }
}
