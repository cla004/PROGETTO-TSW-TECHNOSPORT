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
public class LoginServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        request.getRequestDispatcher("Login.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    	 response.setContentType("application/json");
    	    response.setCharacterEncoding("UTF-8");
    	    
    	    
    	  
    	
    	UtenteDao utenteDao = new UtenteDao();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        if (isAjax) {
        // === Controllo AJAX: solo email ===
        if (email != null) {
            boolean emailEsiste = utenteDao.emailEsiste(email);
            response.setContentType("application/json");
            response.getWriter().write("{ \"valido\": " + emailEsiste + " }");
            return;
        }
        }
        
        if(isAjax) {
        // === Controllo AJAX: password
        if (password != null) {
            Utente utente = utenteDao.cercaUtenteByEmail(email);
            boolean passwordValida = utente != null && utenteDao.verify(password, utente.getPassword());

            response.setContentType("application/json");
            response.getWriter().write("{ \"valido\": " + passwordValida + " }");
            return;
        }
        }

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("loginError", "Inserisci email e password.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }


        // Controlla se l'email esiste
        if (!utenteDao.emailEsiste(email)) {
        
            request.setAttribute("loginError", "Email non registrata.");
                    
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        // Recupera utente tramite email
        Utente utente = utenteDao.cercaUtenteByEmail(email);

        // Verifica la password con il metodo verify (che fa hashing)
        if (utente == null || !utenteDao.verify(password, utente.getPassword())) {
            request.setAttribute("loginError", "Credenziali non valide. Riprova.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        // Login riuscito
        HttpSession session = request.getSession();
        session.setAttribute("loggedInUser", utente);
        session.setMaxInactiveInterval(30 * 60);
        response.sendRedirect(request.getContextPath() + "/Homepage.jsp");
    }
}
