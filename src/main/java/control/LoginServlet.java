package control;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UtenteDAO;
import model.UtenteDAOImpl;
import model.Utente;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		request.getRequestDispatcher("Login.jsp").forward(request, response);
	}

    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        UtenteDAO utenteDAO;
        //request.setCharacterEncoding("UTF-8");
        String usernameOrEmail = request.getParameter("usernameOrEmail");
        String password = request.getParameter("password"); // Password in chiaro dal form

        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("loginError", "Inserisci username/email e password.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        try {
            // Prova prima con email, poi con username
            Utente utente = utenteDAO.getUtenteByEmail(usernameOrEmail);
            if (utente == null) {
                utente = utenteDAO.getUtenteByUsername(usernameOrEmail);
            }

            // Verifica della password (ORA IN CHIARO - FORTEMENTE SCONSIGLIATO)
            if (utente == null || !password.equals(utente.getPassword())) { // Confronto semplice di stringhe
                request.setAttribute("loginError", "Credenziali non valide. Riprova.");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
                return;
            }

            // Login avvenuto con successo
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", utente); // Salva l'oggetto Utente nella sessione
            session.setMaxInactiveInterval(30 * 60);
            response.sendRedirect("${pageContext.request.contextPath}/Homepage.jsp");

        } catch (SQLException e) {
            System.err.println("Errore SQL durante il login: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("loginError", "Si è verificato un errore durante il login. Riprova più tardi.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }
}
