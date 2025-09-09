<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, model.Prodotti, model.ProdottiDao" %>
<%
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
    if (isAdmin == null || !isAdmin) {
        response.sendRedirect("Login.jsp");
        return;
    }

    ProdottiDao dao = new ProdottiDao();
    
    String errore = null;

    List<Prodotti> prodotti = dao.listaProdotti();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gestione Catalogo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css" />
</head>
<body>
    <h1>Gestione Catalogo Prodotti</h1>

    <a href="adminAddProdotto.jsp">➕ Aggiungi Nuovo Prodotto</a>
    
    <% if (errore != null) { %>
    <div class="error-message"><%= errore %></div>
<% } %>

    <table border="1">
        <tr>
            <th>ID</th><th>Nome</th><th>Prezzo</th><th>Categoria</th><th>Azioni</th>
        </tr>
        <%
            for (Prodotti p : prodotti) {
                String nomeCategoria = "Sconosciuto";
                try {
                    nomeCategoria = dao.getNomeCategoriaById(p.getId_categoria());
                } catch (Exception e) {
                	 errore = "Errore durante il recupero dei prodotti: " + e.getMessage();
                }
        %>
        <tr>
            <td><%= p.getId_prodotto() %></td>
            <td><%= p.getNome() %></td>
            <td><%= p.getPrezzo() %>€</td>
            <td><%= nomeCategoria %></td>
            <td>
                <a href="adminEditProdotto.jsp?id=<%= p.getId_prodotto() %>" class="btn">Modifica</a>
                <a href="AdminCancellaProdottoServlet?id=<%= p.getId_prodotto() %>" class="btn btn-danger">Elimina</a>
            </td>
        </tr>
        <%
            }
        %>
    </table>
</body>
</html>
