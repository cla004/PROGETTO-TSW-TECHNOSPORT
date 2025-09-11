package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.*;

@WebServlet(  name="procediOrdine",  value= "/procediOrdine") // value sarebbe l'url della servlet
public class ProcediOrdine extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object utente = session.getAttribute("loggedInUser");
        Boolean completePay = (Boolean) session.getAttribute("ablePay");
        // Se non loggato → reindirizza al login con redirect
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=procediOrdine");
            return;
        }
        
        if ( completePay == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=pagamento");
            return;
        }

        try {
            // Recuperare i dettagli del carrello dalla sessione
            @SuppressWarnings("unchecked")
            List<CartItem> carrelloItems = (List<CartItem>) session.getAttribute("carrelloSessione");

            // Verificare che il carrello non sia vuoto
            if (carrelloItems == null || carrelloItems.isEmpty()) {
                request.setAttribute("errorMessage", "Il carrello è vuoto. Aggiungi prodotti prima di procedere.");
                request.getRequestDispatcher("/Carrello.jsp").forward(request, response);
                return;
            }

            // Creare un nuovo ordine
            Ordine ordine = new Ordine();
            ordine.setUtente((Utente) utente);
            ordine.setTotale(calcolaTotale(carrelloItems));
            ordine.setStato("In elaborazione");
            ordine.setDataOrdine(new java.sql.Timestamp(System.currentTimeMillis()));

            // Salvare ordine nel database
            OrdineDao ordineDao = new OrdineDao();
            ordineDao.inserisciOrdine(ordine);

            // Salvare dettagli dell'ordine nel database
            DettaglioOrdineDao dettaglioOrdineDao = new DettaglioOrdineDao();
            for (CartItem item : carrelloItems) {
                DettaglioOrdine dettaglio = new DettaglioOrdine();
                dettaglio.setOrdine(ordine);
                dettaglio.setProdotto(item.getProdotto());
                dettaglio.setQuantity(item.getQuantity());
                dettaglio.setPrice(item.getProdotto().getPrezzo());
                dettaglioOrdineDao.inserisciDettaglio(dettaglio);
            }

            // Salvare l'ordine nella sessione per mostrarlo nella pagina di conferma
            session.setAttribute("ultimoOrdine", ordine);
            session.setAttribute("dettagliOrdine", carrelloItems);

            // Reindirizzare alla pagina di conferma
            //request.getRequestDispatcher("/ConfermaOrdine.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante il salvataggio dell'ordine. Riprova più tardi.");
            request.getRequestDispatcher("/Error.jsp").forward(request, response);
        }
        
    }
    
    /**
     * Calcola il totale dell'ordine sommando prezzo * quantità per ogni elemento del carrello
     * @param carrelloItems Lista degli elementi del carrello
     * @return Il totale dell'ordine
     */
    private double calcolaTotale(List<CartItem> carrelloItems) {
        double totale = 0.0;
        for (CartItem item : carrelloItems) {
            totale += item.getProdotto().getPrezzo() * item.getQuantity();
        }
        return totale;
    }
}
