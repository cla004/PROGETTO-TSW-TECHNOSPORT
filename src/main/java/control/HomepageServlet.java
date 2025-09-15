
package control;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Prodotti;
import model.ProdottiDao;



@WebServlet("/homepage")
public class HomepageServlet extends HttpServlet {

    private ProdottiDao prodottoDAO;

    @Override
    public void init() {
        prodottoDAO = new ProdottiDao();  
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Prodotti> prodotti = prodottoDAO.listaProdotti();  // Metodo che restituisce tutti i prodotti 

        request.setAttribute("prodotti", prodotti);

        RequestDispatcher dispatcher = request.getRequestDispatcher("homepage.jsp");
        dispatcher.forward(request, response);
    }
}