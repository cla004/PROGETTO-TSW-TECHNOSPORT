package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UtenteDao;
import model.Utente;
import java.io.IOException;

@WebServlet("/login")
//@WebServlet(  name="login",  value= "/login")
public class LoginServlet extends HttpServlet {

	 public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        // Serve per effettuare il redirect dopo che l'utente si è loggato  e qundi può pagare
	        String redirect = request.getParameter("redirect");
	        request.setAttribute("redirect", redirect);
	        request.getRequestDispatcher("Login.jsp").forward(request, response);
	    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        UtenteDao utenteDao = new UtenteDao();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect"); // valore passato dal form
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        // === Controllo AJAX: solo email ===
        if (isAjax && email != null && password == null) {
            boolean emailEsiste = utenteDao.emailEsiste(email);
            response.getWriter().write("{ \"valido\": " + emailEsiste + " }");
            return;
        }

        // === Controllo AJAX: password ===
        if (isAjax && password != null) {
            Utente utente = utenteDao.cercaUtenteByEmail(email);
            boolean passwordValida = utente != null && utenteDao.verify(password, utente.getPassword());
            response.getWriter().write("{ \"valido\": " + passwordValida + " }");
            return;
        }

        // === Controllo campi vuoti ===
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("loginError", "Inserisci email e password.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        // === Controllo esistenza email ===
        if (!utenteDao.emailEsiste(email)) {
            request.setAttribute("loginError", "Email non registrata.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        // === Recupera utente ===
        Utente utente = utenteDao.cercaUtenteByEmail(email);

        // === Verifica password ===
        if (utente == null || !utenteDao.verify(password, utente.getPassword())) {
            request.setAttribute("loginError", "Credenziali non valide. Riprova.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        // === Login riuscito ===
        HttpSession session = request.getSession();
        session.setAttribute("loggedInUser", utente);
        session.setMaxInactiveInterval(30 * 60); // 30 minuti 
        
        // === Admin redirect ===
        if ("admin@calcioshop.it".equalsIgnoreCase(utente.getEmail())) {
            session.setAttribute("isAdmin", true);
            response.sendRedirect(request.getContextPath() + "/adminDashboard.jsp");
            return;
        }

        session.setAttribute("isAdmin", false);

        // === Redirect personalizzato ===
        if (redirect != null && !redirect.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/" + redirect);
        } else {
            response.sendRedirect(request.getContextPath() + "/Homepage.jsp");
        }
    }
}
