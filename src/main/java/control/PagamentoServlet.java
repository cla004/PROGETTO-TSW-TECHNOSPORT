package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(  name="pagamento",  value= "/pagamento") // value sarebbe l'url della servlet
public class PagamentoServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        String nome = request.getParameter("nome");
        String numeroCarta = request.getParameter("numeroCarta");
        String scadenza = request.getParameter("scadenza");
        String cvv = request.getParameter("cvv");

        HttpSession session = request.getSession();
        session.removeAttribute("carrelloSessione"); // svuota carrello dopo acquisto

        // Conferma
        request.setAttribute("messaggio", "Ordine confermato! Grazie per l'acquisto.");
        HttpSession session1 = request.getSession();
        session1.setAttribute("ablePay", true);;
        request.getRequestDispatcher("/carrello.jsp").forward(request, response);
    }
}

