package control;

import model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet(name="carrello", value="/carrello")
public class CarrelloServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        // Ottieni carrello (sessione per ora, DB come backup)
        List<CartItem> prodottiCarrello = (List<CartItem>) session.getAttribute("carrelloSessione");
        if (prodottiCarrello == null) {
            prodottiCarrello = new ArrayList<>();
            session.setAttribute("carrelloSessione", prodottiCarrello);
        }
        
        // Se utente loggato, prova a caricare dal database se sessione vuota
        Utente utente = (Utente) session.getAttribute("loggedInUser");
        if (utente != null && prodottiCarrello.isEmpty()) {
            try {
                assicuraCarrelloEsiste(utente.getId());
                CartItemDao cartDao = new CartItemDao();
                List<CartItem> carrelloDB = cartDao.findByUserId(utente.getId());
                if (carrelloDB != null && !carrelloDB.isEmpty()) {
                    prodottiCarrello = carrelloDB;
                    session.setAttribute("carrelloSessione", prodottiCarrello);
                }
            } catch (Exception e) {
                System.err.println("Errore caricamento carrello DB: " + e.getMessage());
            }
        }

        try {
            if ("visualizza".equals(action) || action == null) {
                // Mostra la pagina carrello
                double totale = 0.0;
                int numeroArticoli = 0;
                
                // Assicurati che i prodotti siano caricati per il calcolo
                ProdottiDao prodottiDao = new ProdottiDao();
                for (CartItem item : prodottiCarrello) {
                    // Se il prodotto non è già caricato, caricalo
                    if (item.getProdotto() == null) {
                        try {
                            Prodotti prodotto = prodottiDao.cercaProdottoById(item.getProductId());
                            item.setProdotto(prodotto);
                        } catch (Exception e) {
                            System.err.println("Errore caricamento prodotto ID " + item.getProductId() + ": " + e.getMessage());
                            continue;
                        }
                    }
                    
                    if (item.getProdotto() != null) {
                        totale += item.getProdotto().getPrezzo() * item.getQuantity();
                        numeroArticoli += item.getQuantity();
                    }
                }
                
                request.setAttribute("prodottiCarrello", prodottiCarrello);
                request.setAttribute("totale", totale);
                request.setAttribute("numeroArticoli", numeroArticoli);
                request.getRequestDispatcher("carrello.jsp").forward(request, response);

            } else if ("aggiungi".equals(action)) {
                int prodottoId = Integer.parseInt(request.getParameter("prodottoId"));
                String tagliaIdStr = request.getParameter("tagliaId");
                String quantitaStr = request.getParameter("quantita");
                
                if (tagliaIdStr == null || tagliaIdStr.isEmpty()) {
                    session.setAttribute("errore", "Seleziona una taglia!");
                    response.sendRedirect("Homepage.jsp");
                    return;
                }
                
                int tagliaId = Integer.parseInt(tagliaIdStr);
                int quantitaRichiesta = (quantitaStr != null && !quantitaStr.isEmpty()) ? 
                                       Integer.parseInt(quantitaStr) : 1;
                
                // Controlla che il prodotto e la taglia esistano (senza verificare disponibilità)
                ProdottoTagliaDao ptDao = new ProdottoTagliaDao();
                Prodotto_taglia pt = ptDao.cerca(prodottoId, tagliaId);
                
                if (pt == null) {
                    session.setAttribute("errore", "Prodotto o taglia non valida!");
                    response.sendRedirect("Homepage.jsp");
                    return;
                }
                
                // Cerca se esiste già nel carrello (stesso prodotto E stessa taglia)
                boolean trovato = false;
                for (CartItem item : prodottiCarrello) {
                    if (item.getProdotto().getId_prodotto() == prodottoId && 
                        item.getTagliaId() == tagliaId) {
                        item.setQuantity(item.getQuantity() + quantitaRichiesta);
                        trovato = true;
                        break;
                    }
                }

                if (!trovato) {
                    ProdottiDao prodottiDao = new ProdottiDao();
                    Prodotti prodotto = prodottiDao.cercaProdottoById(prodottoId);
                    if (prodotto != null) {
                        CartItem nuovoCartItem = new CartItem();
                        nuovoCartItem.setQuantity(quantitaRichiesta);
                        nuovoCartItem.setProdotto(prodotto);
                        nuovoCartItem.setTagliaId(tagliaId);
                        prodottiCarrello.add(nuovoCartItem);
                    }
                }
                
                // Salva nel database se utente loggato
                if (utente != null) {
                    System.out.println("DEBUG: Utente loggato #" + utente.getId() + ", salvo nel DB");
                    salvaOperazioneCarrello(session, "aggiungi", prodottoId, tagliaId, quantitaRichiesta);
                } else {
                    System.out.println("DEBUG: Utente NON loggato, non salvo nel DB");
                }
                
                session.setAttribute("successo", "Prodotto aggiunto al carrello!");
                response.sendRedirect("carrello?action=visualizza");

            } else if ("rimuovi".equals(action)) {
                int prodottoId = Integer.parseInt(request.getParameter("prodottoId"));
                int tagliaId = Integer.parseInt(request.getParameter("tagliaId"));
                Iterator<CartItem> iterator = prodottiCarrello.iterator();

                while (iterator.hasNext()) {
                    CartItem item = iterator.next();
                    if (item.getProdotto().getId_prodotto() == prodottoId && item.getTagliaId() == tagliaId) {
                        if (item.getQuantity() > 1) {
                            item.setQuantity(item.getQuantity() - 1);
                        } else {
                            iterator.remove();
                        }
                        break;
                    }
                }
                
                // Salva nel database se utente loggato
                salvaOperazioneCarrello(session, "rimuovi", prodottoId, tagliaId, 1);

                session.setAttribute("successo", "Prodotto rimosso dal carrello!");
                response.sendRedirect("carrello?action=visualizza");

            } else if ("elimina".equals(action)) {
                int prodottoId = Integer.parseInt(request.getParameter("prodottoId"));
                int tagliaId = Integer.parseInt(request.getParameter("tagliaId"));
                Iterator<CartItem> iterator = prodottiCarrello.iterator();

                while (iterator.hasNext()) {
                    CartItem item = iterator.next();
                    if (item.getProdotto().getId_prodotto() == prodottoId && item.getTagliaId() == tagliaId) {
                        iterator.remove();
                        break;
                    }
                }
                
                // Salva nel database se utente loggato
                salvaOperazioneCarrello(session, "elimina", prodottoId, tagliaId, 0);

                session.setAttribute("successo", "Prodotto eliminato completamente dal carrello!");
                response.sendRedirect("carrello?action=visualizza");

            } else if ("svuota".equals(action)) {
                prodottiCarrello.clear(); // svuota la lista
                
                // Salva nel database se utente loggato
                salvaOperazioneCarrello(session, "svuota", 0, 0, 0);
                
                session.setAttribute("successo", "Carrello svuotato!");
                response.sendRedirect("carrello?action=visualizza");

            } else {
                // Azione non riconosciuta
                response.sendRedirect("carrello?action=visualizza");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("carrello?errore=Errore nella gestione del carrello");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/// facciamo in modo che get si modifica come post 
        doPost(request, response);
    }
    
    /**
     * Sincronizza il carrello tra sessione e database per utente loggato
     */
    private List<CartItem> sincronizzaCarrello(HttpSession session) {
        Utente utente = (Utente) session.getAttribute("loggedInUser");
        
        // Se utente non loggato, usa solo sessione
        if (utente == null) {
            List<CartItem> carrelloSessione = (List<CartItem>) session.getAttribute("carrelloSessione");
            if (carrelloSessione == null) {
                carrelloSessione = new ArrayList<>();
                session.setAttribute("carrelloSessione", carrelloSessione);
            }
            return carrelloSessione;
        }
        
        // Utente loggato: sincronizza con database
        try {
            // Assicurati che esista un record nella tabella cart
            assicuraCarrelloEsiste(utente.getId());
            
            CartItemDao cartDao = new CartItemDao();
            List<CartItem> carrelloDb = cartDao.findByUserId(utente.getId());
            
            // Se c'è carrello in sessione, migralo al database
            List<CartItem> carrelloSessione = (List<CartItem>) session.getAttribute("carrelloSessione");
            if (carrelloSessione != null && !carrelloSessione.isEmpty()) {
                migraCarrelloADatabase(carrelloSessione, utente.getId());
                session.removeAttribute("carrelloSessione");
                
                // Ricarica dal database dopo migrazione
                carrelloDb = cartDao.findByUserId(utente.getId());
            }
            
            // Aggiorna la sessione con il carrello dal database per compatibilità
            if (carrelloDb != null && !carrelloDb.isEmpty()) {
                session.setAttribute("carrelloSessione", carrelloDb);
            }
            
            return carrelloDb != null ? carrelloDb : new ArrayList<>();
            
        } catch (Exception e) {
            System.err.println("Errore sincronizzazione carrello: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback su sessione in caso di errore
            List<CartItem> carrelloSessione = (List<CartItem>) session.getAttribute("carrelloSessione");
            if (carrelloSessione == null) {
                carrelloSessione = new ArrayList<>();
                session.setAttribute("carrelloSessione", carrelloSessione);
            }
            return carrelloSessione;
        }
    }
    
    /**
     * Migra carrello dalla sessione al database
     */
    private void migraCarrelloADatabase(List<CartItem> carrelloSessione, int userId) {
        try {
            // Assicurati che esista il carrello
            CarrelloDao carrelloDao = new CarrelloDao();
            Carrello carrello = carrelloDao.findByUserId(userId);
            if (carrello == null) {
                carrello = new Carrello();
                carrello.setUserId(userId);
                carrello.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                carrelloDao.createCart(carrello);
            }
            
            CartItemDao cartDao = new CartItemDao();
            
            for (CartItem item : carrelloSessione) {
                // Controlla se esiste già
                CartItem esistente = cartDao.findByUserProductAndSize(
                    userId, 
                    item.getProdotto().getId_prodotto(), 
                    item.getTagliaId()
                );
                
                if (esistente != null) {
                    // Aggiorna quantità
                    cartDao.updateQuantity(esistente.getId(), 
                                         esistente.getQuantity() + item.getQuantity());
                } else {
                    // Inserisci nuovo
                    CartItem nuovoItem = new CartItem();
                    nuovoItem.setCartId(carrello.getId());
                    nuovoItem.setProductId(item.getProdotto().getId_prodotto());
                    nuovoItem.setTagliaId(item.getTagliaId());
                    nuovoItem.setQuantity(item.getQuantity());
                    cartDao.insert(nuovoItem);
                }
            }
        } catch (Exception e) {
            System.err.println("Errore migrazione carrello: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Assicura che esista un record nella tabella cart per l'utente
     */
    private void assicuraCarrelloEsiste(int userId) {
        try {
            CarrelloDao carrelloDao = new CarrelloDao();
            Carrello carrelloEsistente = carrelloDao.findByUserId(userId);
            
            if (carrelloEsistente == null) {
                // Crea nuovo carrello
                Carrello nuovoCarrello = new Carrello();
                nuovoCarrello.setUserId(userId);
                nuovoCarrello.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                carrelloDao.createCart(nuovoCarrello);
            }
        } catch (Exception e) {
            System.err.println("Errore creazione carrello per utente #" + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Salva operazione carrello nel database se utente loggato
     */
    private void salvaOperazioneCarrello(HttpSession session, String operazione, 
                                        int prodottoId, int tagliaId, int quantita) {
        Utente utente = (Utente) session.getAttribute("loggedInUser");
        if (utente == null) {
            System.out.println("DEBUG: Utente null in salvaOperazioneCarrello");
            return; // Non salvare se non loggato
        }
        
        System.out.println("DEBUG: Inizio salvaOperazioneCarrello - Operazione: " + operazione + 
                          ", ProdottoId: " + prodottoId + ", TagliaId: " + tagliaId + ", Quantità: " + quantita);
        
        try {
            CartItemDao cartDao = new CartItemDao();
            
            switch (operazione) {
                case "aggiungi":
                    System.out.println("DEBUG: Caso AGGIUNGI - Cerco carrello per utente #" + utente.getId());
                    // Ottieni l'ID del carrello dell'utente
                    CarrelloDao carrelloDao = new CarrelloDao();
                    Carrello carrello = carrelloDao.findByUserId(utente.getId());
                    if (carrello == null) {
                        System.out.println("DEBUG: Carrello non trovato, ne creo uno nuovo");
                        // Crea carrello se non esiste
                        carrello = new Carrello();
                        carrello.setUserId(utente.getId());
                        carrello.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                        carrelloDao.createCart(carrello);
                        System.out.println("DEBUG: Nuovo carrello creato con ID: " + carrello.getId());
                    } else {
                        System.out.println("DEBUG: Carrello esistente trovato con ID: " + carrello.getId());
                    }
                    
                    System.out.println("DEBUG: Cerco CartItem esistente per prodotto " + prodottoId + ", taglia " + tagliaId);
                    CartItem esistente = cartDao.findByUserProductAndSize(utente.getId(), prodottoId, tagliaId);
                    if (esistente != null) {
                        System.out.println("DEBUG: CartItem esistente trovato, aggiorno quantità da " + esistente.getQuantity() + " a " + (esistente.getQuantity() + quantita));
                        cartDao.updateQuantity(esistente.getId(), esistente.getQuantity() + quantita);
                    } else {
                        System.out.println("DEBUG: CartItem NON esistente, ne creo uno nuovo");
                        CartItem nuovoItem = new CartItem();
                        nuovoItem.setCartId(carrello.getId());
                        nuovoItem.setProductId(prodottoId);
                        nuovoItem.setTagliaId(tagliaId);
                        nuovoItem.setQuantity(quantita);
                        System.out.println("DEBUG: Inserisco nuovo CartItem nel database...");
                        cartDao.insert(nuovoItem);
                        System.out.println("DEBUG: CartItem inserito con successo!");
                    }
                    break;
                    
                case "rimuovi":
                    esistente = cartDao.findByUserProductAndSize(utente.getId(), prodottoId, tagliaId);
                    if (esistente != null) {
                        if (esistente.getQuantity() > 1) {
                            cartDao.updateQuantity(esistente.getId(), esistente.getQuantity() - 1);
                        } else {
                            cartDao.delete(esistente.getId());
                        }
                    }
                    break;
                    
                case "elimina":
                    esistente = cartDao.findByUserProductAndSize(utente.getId(), prodottoId, tagliaId);
                    if (esistente != null) {
                        cartDao.delete(esistente.getId());
                    }
                    break;
                    
                case "svuota":
                    cartDao.deleteByUserId(utente.getId());
                    break;
            }
        } catch (Exception e) {
            System.err.println("DEBUG: ERRORE in salvaOperazioneCarrello: " + e.getMessage());
            System.err.println("DEBUG: Operazione era: " + operazione + ", ProdottoId: " + prodottoId);
            e.printStackTrace();
        }
        
        System.out.println("DEBUG: Fine salvaOperazioneCarrello");
    }
}
