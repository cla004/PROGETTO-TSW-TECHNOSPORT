package controller;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import tecnosport.Utente;
import tecnosport.UtenteDAO;

@WebServlet("/registrazione")
public class RegistrazioneServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String indirizzo = request.getParameter("indirizzo");

        UtenteDAO dao = new UtenteDAO();

        boolean errore = false;

        if (dao.emailEsiste(email)) {
            request.setAttribute("erroreEmail", "Email già utilizzata.");
            errore = true;
        }

        if (dao.passwordEsiste(password)) {
            request.setAttribute("errorePassword", "Password già utilizzata.");
            errore = true;
        }

        if (!errore) {
            Utente utente = new Utente(nome, email, password, indirizzo);
            dao.inserisciUtente(utente);
            request.setAttribute("successo", true);
        }

        // reinserisce i dati nel form in caso di errore
        request.setAttribute("nome", nome);
        request.setAttribute("email", email);
        request.setAttribute("indirizzo", indirizzo);

        RequestDispatcher dispatcher = request.getRequestDispatcher("registrazione.jsp");
        dispatcher.forward(request, response);
    }
}
