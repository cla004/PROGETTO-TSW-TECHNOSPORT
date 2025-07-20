package control;
 
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
 
import java.io.IOException;
 
@WebServlet(  name="logout",  value= "/logout") // value sarebbe l'url della servlet
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Ottieni la sessione corrente
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Invalida la sessione corrente
            session.invalidate();
        }
        // Redirect alla pagina di login con messaggio di conferma
        
        // Redirect alla pagina di homepage
        response.sendRedirect(request.getContextPath() + "/Homepage.jsp");
    }
}