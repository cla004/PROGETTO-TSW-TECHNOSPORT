package control;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.ProdottiDao;
import model.ProdottoTagliaDao;

@WebServlet("/admin/AdminCancellaProdottoServlet")
public class AdminCancellaProdottoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        
        // Controllo accesso admin
        if (isAdmin == null || !isAdmin) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            request.setAttribute("errore", "ID prodotto non specificato");
            request.getRequestDispatcher("/admin/catalogo.jsp").forward(request, response);
            return;
        }
        
        try {
            int prodottoId = Integer.parseInt(idStr);
            
            // Prima elimina le associazioni prodotto-taglia
            ProdottoTagliaDao ptDao = new ProdottoTagliaDao();
            ptDao.eliminaByProdottoId(prodottoId);
            
            // Poi elimina il prodotto
            ProdottiDao prodottiDao = new ProdottiDao();
            prodottiDao.eliminaProdotto(prodottoId);
            
            // Redirect al catalogo con messaggio di successo
            request.setAttribute("successo", "Prodotto eliminato con successo!");
            
        } catch (NumberFormatException e) {
            request.setAttribute("errore", "ID prodotto non valido");
        } catch (Exception e) {
            request.setAttribute("errore", "Errore durante l'eliminazione: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Redirect al catalogo
        request.getRequestDispatcher("/admin/catalogo.jsp").forward(request, response);
    }
}
