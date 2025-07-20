package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(  name="procediOrdine",  value= "/procediOrdine") // value sarebbe l'url della servlet
public class ProcediOrdine extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object utente = session.getAttribute("loggedInUser");

        // Se non loggato → reindirizza al login con redirect
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=procediOrdine");
            return;
        }

        // Se loggato → vai alla pagina di pagamento
        request.getRequestDispatcher("/Pagamento.jsp").forward(request, response);
    }
}
