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

        List<Carrello> prodottiCarrello = (List<Carrello>) session.getAttribute("carrelloSessione");
        if (prodottiCarrello == null) {
            prodottiCarrello = new ArrayList<>();
            session.setAttribute("carrelloSessione", prodottiCarrello);
        }

        try {
            if ("visualizza".equals(action) || action == null) {
                // Se action è "visualizza" o nulla, mostra la pagina carrello
                double totale = 0.0;
                int numeroArticoli = 0;
                for (Carrello item : prodottiCarrello) {
                    totale += item.getProdotto().getPrezzo() * item.getQuantita();
                    numeroArticoli += item.getQuantita();
                }
                request.setAttribute("prodottiCarrello", prodottiCarrello);
                request.setAttribute("totale", totale);
                request.setAttribute("numeroArticoli", numeroArticoli);
                request.getRequestDispatcher("carrello.jsp").forward(request, response);

            } else if ("aggiungi".equals(action)) {
                int prodottoId = Integer.parseInt(request.getParameter("prodottoId"));
                boolean trovato = false;

                for (Carrello item : prodottiCarrello) {
                    if (item.getProdotto().getId_prodotto() == prodottoId) {
                        item.setQuantita(item.getQuantita() + 1);
                        trovato = true;
                        break;
                    }
                }

                if (!trovato) {
                    ProdottiDao prodottiDao = new ProdottiDao();
                    Prodotti prodotto = prodottiDao.cercaProdottoById(prodottoId);
                    if (prodotto != null) {
                        Carrello nuovoCarrello = new Carrello();
                        nuovoCarrello.setQuantita(1);
                        nuovoCarrello.setProdotto(prodotto);
                        prodottiCarrello.add(nuovoCarrello);
                    }
                }

                session.setAttribute("successo", "Prodotto aggiunto al carrello!");
                response.sendRedirect("carrello?action=visualizza");

            } else if ("rimuovi".equals(action)) {
                int prodottoId = Integer.parseInt(request.getParameter("prodottoId"));
                Iterator<Carrello> iterator = prodottiCarrello.iterator();

                while (iterator.hasNext()) {
                    Carrello item = iterator.next();
                    if (item.getProdotto().getId_prodotto() == prodottoId) {
                        if (item.getQuantita() > 1) {
                            item.setQuantita(item.getQuantita() - 1);
                        } else {
                            iterator.remove();
                        }
                        break;
                    }
                }

                session.setAttribute("successo", "Prodotto rimosso dal carrello!");
                response.sendRedirect("carrello?action=visualizza");

            } else if ("elimina".equals(action)) {
                int prodottoId = Integer.parseInt(request.getParameter("prodottoId"));
                prodottiCarrello.removeIf(item -> item.getProdotto().getId_prodotto() == prodottoId);

                session.setAttribute("successo", "Prodotto eliminato completamente dal carrello!");
                response.sendRedirect("carrello?action=visualizza");

            } else if ("svuota".equals(action)) {
                session.removeAttribute("carrelloSessione");
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
        // Per comodità facciamo che GET si comporti come POST
        doPost(request, response);
    }
}
