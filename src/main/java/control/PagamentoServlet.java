package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;
import model.Indirizzo;
import model.MetodoPagamento;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="pagamento", value="/pagamento")
public class PagamentoServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        
        // Controlla se arriva dal CheckoutServlet
        Indirizzo indirizzoOrdine = (Indirizzo) request.getAttribute("indirizzoOrdine");
        MetodoPagamento metodoPagamentoOrdine = (MetodoPagamento) request.getAttribute("metodoPagamentoOrdine");
        Ordine ordineCreato = (Ordine) request.getAttribute("ordineCreato");
        
        // Ottieni carrello (database o sessione)
        List<CartItem> prodottiCarrello = ottieniCarrelloPerPagamento(session, ordineCreato);
        
        // Controlla che ci sia un carrello
        if (prodottiCarrello == null || prodottiCarrello.isEmpty()) {
            session.setAttribute("errore", "Carrello vuoto! Non è possibile procedere al pagamento.");
            response.sendRedirect("carrello?action=visualizza");
            return;
        }
        
        // Se arriva dal CheckoutServlet, usa i dati già validati
        String nome = "";
        if (indirizzoOrdine != null && metodoPagamentoOrdine != null) {
            // Chiamata dal nuovo flusso checkout
            nome = metodoPagamentoOrdine.getIntestatario();
        } else {
            // Chiamata dal vecchio flusso (compatibilità)
            nome = request.getParameter("nome");
            String numeroCarta = request.getParameter("numeroCarta");
            String scadenza = request.getParameter("scadenza");
            String cvv = request.getParameter("cvv");
            
            // Valida parametri pagamento (basilare)
            if (nome == null || nome.trim().isEmpty() || 
                numeroCarta == null || numeroCarta.trim().isEmpty() || 
                scadenza == null || scadenza.trim().isEmpty() || 
                cvv == null || cvv.trim().isEmpty()) {
                session.setAttribute("errore", "Tutti i campi del pagamento sono obbligatori!");
                response.sendRedirect("Pagamento.jsp");
                return;
            }
        }
        
        // FASE 1: Controlla disponibilità di tutti i prodotti
        ProdottoTagliaDao ptDao = new ProdottoTagliaDao();
        List<String> erroriDisponibilita = new ArrayList<>();
        
        for (CartItem item : prodottiCarrello) {
            Prodotto_taglia pt = ptDao.cerca(item.getProdotto().getId_prodotto(), item.getTagliaId());
            
            if (pt == null) {
                erroriDisponibilita.add("Prodotto " + item.getProdotto().getNome() + " non più disponibile");
            } else if (pt.getQuantita_disponibili() < item.getQuantity()) {
                TagliaDao tagliaDao = new TagliaDao();
                Taglia taglia = tagliaDao.cercaTagliaById(item.getTagliaId());
                String nomeTaglia = (taglia != null) ? taglia.getEtichetta() : "N/A";
                
                erroriDisponibilita.add("Prodotto " + item.getProdotto().getNome() + 
                                      " taglia " + nomeTaglia + ": richiesti " + item.getQuantity() + 
                                      ", disponibili solo " + (int)pt.getQuantita_disponibili());
            }
        }
        
        // Se ci sono errori di disponibilità, interrompi
        if (!erroriDisponibilita.isEmpty()) {
            String messaggioErrore = "Alcuni prodotti non sono più disponibili nelle quantità richieste:\n";
            for (String errore : erroriDisponibilita) {
                messaggioErrore += "• " + errore + "\n";
            }
            session.setAttribute("errore", messaggioErrore);
            response.sendRedirect("carrello?action=visualizza");
            return;
        }
        
        // FASE 2: Se tutto è disponibile, decrementa le quantità
        try {
            for (CartItem item : prodottiCarrello) {
                boolean success = ptDao.decrementaQuantita(
                    item.getProdotto().getId_prodotto(), 
                    item.getTagliaId(), 
                    item.getQuantity()
                );
                
                if (!success) {
                    throw new RuntimeException("Errore nel decrementare la quantità per il prodotto " + 
                                             item.getProdotto().getNome());
                }
            }
            
            // FASE 3: Pagamento riuscito
            
            // Registra pagamento e aggiorna ordine se presente
            if (ordineCreato != null) {
                registraPagamento(ordineCreato, metodoPagamentoOrdine);
            }
            
            // Svuota carrello (database e sessione)
            svuotaCarrelloDopoOrdine(session, ordineCreato);
            
            // Pulisci sessione
            session.removeAttribute("carrelloSessione");
            session.removeAttribute("indirizzoSelezionato");
            session.removeAttribute("pagamentoSelezionato");
            
            // Conferma successo
            session.setAttribute("successo", "🎉 Ordine confermato! Grazie per l'acquisto, " + nome + "!");
            session.setAttribute("ablePay", true);
            
            // Redirect appropriato
            if (indirizzoOrdine != null && metodoPagamentoOrdine != null) {
                // Dal nuovo flusso checkout
                response.sendRedirect("ordineCompletato.jsp");
            } else {
                // Dal vecchio flusso
                response.sendRedirect("carrello?action=visualizza");
            }
            
        } catch (Exception e) {
            // Errore durante il pagamento - logga e informa l'utente
            System.err.println("Errore durante il processo di pagamento: " + e.getMessage());
            e.printStackTrace();
            
            session.setAttribute("errore", "Errore durante il processo di pagamento. Riprova.");
            response.sendRedirect("Pagamento.jsp");
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect al form di pagamento
        response.sendRedirect("Pagamento.jsp");
    }
    
    /**
     * Ottiene il carrello per il pagamento (database o sessione)
     */
    private List<CartItem> ottieniCarrelloPerPagamento(HttpSession session, Ordine ordineCreato) {
        Utente utente = (Utente) session.getAttribute("loggedInUser");
        
        if (ordineCreato != null && utente != null) {
            // Nuovo flusso: prova database, fallback su sessione
            try {
                CartItemDao cartDao = new CartItemDao();
                List<CartItem> carrelloDb = cartDao.findByUserId(utente.getId());
                
                if (carrelloDb != null && !carrelloDb.isEmpty()) {
                    return carrelloDb;
                }
            } catch (Exception e) {
                System.err.println("Errore lettura carrello DB per pagamento: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // Fallback su sessione
        return (List<CartItem>) session.getAttribute("carrelloSessione");
    }
    
    /**
     * Registra il pagamento nel database
     */
    private void registraPagamento(Ordine ordine, MetodoPagamento metodoPagamento) {
        try {
            // Crea record pagamento
            PaymentDao paymentDao = new PaymentDao();
            Payment payment = new Payment();
            payment.setOrderId(ordine.getId());
            payment.setMetodoId(metodoPagamento.getId());
            payment.setPaidAt(new java.sql.Timestamp(System.currentTimeMillis()));
            paymentDao.inserisciPagamento(payment);
            
            // Aggiorna stato ordine
            OrdineDao ordineDao = new OrdineDao();
            ordineDao.aggiornaStatoOrdine(ordine.getId(), "Pagato");
            
        } catch (Exception e) {
            System.err.println("Errore registrazione pagamento: " + e.getMessage());
            e.printStackTrace();
            // Non blocca l'operazione per errori di logging
        }
    }
    
    /**
     * Svuota il carrello dopo l'ordine completato
     */
    private void svuotaCarrelloDopoOrdine(HttpSession session, Ordine ordineCreato) {
        Utente utente = (Utente) session.getAttribute("loggedInUser");
        
        if (utente != null) {
            try {
                // Svuota solo i prodotti dal carrello, mantieni il carrello stesso
                CartItemDao cartDao = new CartItemDao();
                cartDao.deleteByUserId(utente.getId());
                
                // Il carrello rimane attivo per futuri acquisti
                System.out.println("Prodotti rimossi dal carrello, carrello mantiene attivo per utente #" + utente.getId());
                
            } catch (Exception e) {
                System.err.println("Errore svuotamento carrello DB: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // Svuota anche dalla sessione per sicurezza
        session.removeAttribute("carrelloSessione");
    }
}

