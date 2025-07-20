package control;

import model.Utente;
import model.UtenteDao;
import model.PasswordHashing;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/registrazione")
public class RegistrazioneServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Metodo GET per test - reindirizza alla pagina di registrazione
        response.getWriter().write("<h1>Servlet Registrazione funziona!</h1><p>La servlet è raggiungibile.</p>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String nome = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");

        try {
            // Validazione email con regex semplice usando matches()
            if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                request.setAttribute("errorEmail", "Email non valida");
                request.getRequestDispatcher("/Registrazione.jsp").forward(request, response);
                return;
            }

            // Validazione password: almeno 8 caratteri e almeno 1 carattere speciale
            if (password == null || !password.matches("^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$")) {
                request.setAttribute("errorPassword", "La password deve contenere almeno 8 caratteri e un carattere speciale");
                request.getRequestDispatcher("/Registrazione.jsp").forward(request, response);
                return;
            }

            // Controllo che password e conferma siano uguali
            if (!password.equals(confirmPassword)) {
                request.setAttribute("errorConfirmPassword", "Le password non coincidono.");
                request.getRequestDispatcher("/Registrazione.jsp").forward(request, response);
                return;
            }

            UtenteDao dao = new UtenteDao();

            // Controllo che email non sia gi� registrata
            if (dao.emailEsiste(email)) {
                request.setAttribute("errorEmail", "Email gi� registrata.");
                request.getRequestDispatcher("/Registrazione.jsp").forward(request, response);
                return;
            }

            // Hashed password per sicurezza
            String hashedPassword = PasswordHashing.toHash(password);
            Utente utente = new Utente();
            utente.setNome(nome);
            utente.setEmail(email);
            utente.setPassword(hashedPassword);

            // Inserimento utente nel database
            dao.inserisciUtente(utente);

            // Reindirizza al login dopo registrazione avvenuta con successo
            response.sendRedirect("Login.jsp");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

