package Control;

import model.Utente;
import model.UtenteDAO;
import model.PasswordHashing;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/registrazione")
public class RegistrazioneServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
    	String nome = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");

        try {
            UtenteDAO dao = new UtenteDAO();

            if (dao.emailEsiste(email)) {
                request.setAttribute("error", "Email già registrata.");
                request.getRequestDispatcher("/Registrazione.jsp").forward(request, response);
                return;
            }

            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "Le password non coincidono.");
                request.getRequestDispatcher("/Registrazione.jsp").forward(request, response);
                return;
            }

            String hashedPassword = PasswordHashing.toHash(password);
            Utente utente = new Utente();
            utente.setNome(nome);
            utente.setEmail(email);
            utente.setPassword(hashedPassword);

            dao.inserisciUtente(utente);

            response.sendRedirect("Login.jsp");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
package control;

import model.Utente;
import model.UtenteDAO;
import model.PasswordHashing;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/registrazione")
public class RegistrazioneServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
    	String nome = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");

        try {
            UtenteDAO dao = new UtenteDAO();

            if (dao.emailEsiste(email)) {
                request.setAttribute("error", "Email già registrata.");
                request.getRequestDispatcher("/Registrazione.jsp").forward(request, response);
                return;
            }

            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "Le password non coincidono.");
                request.getRequestDispatcher("/Registrazione.jsp").forward(request, response);
                return;
            }

            String hashedPassword = PasswordHashing.toHash(password);
            Utente utente = new Utente();
            utente.setNome(nome);
            utente.setEmail(email);
            utente.setPassword(hashedPassword);

            dao.inserisciUtente(utente);

            response.sendRedirect("Login.jsp");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
