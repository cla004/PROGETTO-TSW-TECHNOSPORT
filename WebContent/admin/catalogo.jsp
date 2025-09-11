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
    <div class="header">
        <h1>Gestione Catalogo Prodotti</h1>
        <a href="dashboard.jsp" class="dashboard-btn">📦 Torna alla Dashboard</a>
        <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-btn">🚪 Logout</a>
        <div class="clear"></div>
    </div>
    
    <div class="container">
        <div class="welcome">
            <a href="aggiungi-prodotto.jsp" class="btn btn-success">➕ Aggiungi Nuovo Prodotto</a>
        </div>
    
        <% if (errore != null) { %>
            <div class="error-message"><%= errore %></div>
        <% } %>
        
        <% String successo = (String) request.getAttribute("successo"); %>
        <% if (successo != null) { %>
            <div class="success-message"><%= successo %></div>
        <% } %>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Prezzo</th>
                    <th>Categoria</th>
                    <th>Stock</th>
                    <th>Azioni</th>
                </tr>
            </thead>
            <tbody>
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
                    <td><strong><%= p.getNome() %></strong></td>
                    <td><%= String.format("%.2f", p.getPrezzo()) %> €</td>
                    <td><%= nomeCategoria %></td>
                    <td><%= p.getQuantita_disponibili() %></td>
                    <td>
                        <a href="adminEditProdotto.jsp?id=<%= p.getId_prodotto() %>" class="btn">✏️ Modifica</a>
                        <a href="AdminCancellaProdottoServlet?id=<%= p.getId_prodotto() %>" class="btn btn-danger">🗑️ Elimina</a>
                    </td>
                </tr>
        <%
            }
        %>
            </tbody>
        </table>
    </div>
</body>
</html>
