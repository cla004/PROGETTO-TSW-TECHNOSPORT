<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, model.*" %>
<%
    // Controllo accesso admin
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
    if (isAdmin == null || !isAdmin) {
        response.sendRedirect("../Login.jsp");
        return;
    }
    
    Utente admin = (Utente) session.getAttribute("loggedInUser");
    
    // Caricamento categorie
    List<Categoria> categorie = new ArrayList<>();
    String errore = null;
    
    try {
        CategoriaDao categoriaDao = new CategoriaDao();
        categorie = categoriaDao.listaCategorie();
    } catch (Exception e) {
        errore = "Errore nel caricamento delle categorie: " + e.getMessage();
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gestione Categorie - Admin TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
    <div class="header">
        <h1>Gestione Categorie</h1>
        <a href="dashboard.jsp">← Torna alla Dashboard</a>
        <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-btn">Logout</a>
        <div class="clear"></div>
    </div>
    
    <div class="container">
        <div class="welcome">
            <h2>Categorie Prodotti</h2>
            <p>Gestisci le categorie disponibili per i prodotti del catalogo.</p>
        </div>
        
        <% String successo = (String) request.getAttribute("successo"); %>
        <% if (successo != null) { %>
            <div class="success-message">
                <%= successo %>
            </div>
        <% } %>
        
        <% if (errore != null) { %>
            <div class="error-message">
                <%= errore %>
            </div>
        <% } %>
        
        <!-- Form aggiunta categoria -->
        <div class="card">
            <h3>Aggiungi Nuova Categoria</h3>
            <form method="post" action="${pageContext.request.contextPath}/admin/categorie">
                <input type="hidden" name="action" value="aggiungi">
                
                <div class="form-group">
                    <label for="nomeCategoria">Nome Categoria *</label>
                    <input type="text" id="nomeCategoria" name="nomeCategoria" required 
                           placeholder="Es: Calcio, Basket, Tennis..."
                           value="<%= request.getParameter("nomeCategoria") != null ? request.getParameter("nomeCategoria") : "" %>">
                </div>
                
                <button type="submit" class="btn btn-success">Aggiungi Categoria</button>
            </form>
        </div>
        
        <div class="clear"></div>
        
        <!-- Lista categorie esistenti -->
        <% if (categorie.isEmpty()) { %>
            <div class="welcome">
                <p>Nessuna categoria trovata. Aggiungi la prima categoria usando il form sopra.</p>
            </div>
        <% } else { %>
            
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome Categoria</th>
                        <th>Prodotti</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        ProdottiDao prodottiDao = new ProdottiDao();
                        for (Categoria categoria : categorie) { 
                            int numeroProdotti = 0;
                            try {
                                numeroProdotti = prodottiDao.contaProdottiPerCategoria(categoria.getid_categoria());
                            } catch (Exception e) {
                                // Ignora errore conteggio
                            }
                    %>
                    <tr>
                        <td><strong><%= categoria.getid_categoria() %></strong></td>
                        <td><%= categoria.getnome_recensione() %></td>
                        <td><%= numeroProdotti %> prodotti</td>
                        <td>
                            <a href="modifica-categoria.jsp?id=<%= categoria.getid_categoria() %>" class="btn">Modifica</a>
                            <% if (numeroProdotti == 0) { %>
                                <a href="elimina-categoria.jsp?id=<%= categoria.getid_categoria() %>" class="btn btn-danger">
                                   Elimina
                                </a>
                            <% } else { %>
                                <span class="btn btn-danger" title="Non puoi eliminare una categoria con prodotti associati" style="opacity: 0.5; cursor: not-allowed;">
                                    Elimina
                                </span>
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            
            <div class="clear"></div>
            <p><strong>Totale categorie:</strong> <%= categorie.size() %></p>
            
        <% } %>
        
        <div class="card">
            <h3>Informazioni</h3>
            <p>• Le categorie sono utilizzate per organizzare i prodotti nel catalogo</p>
            <p>• Non puoi eliminare una categoria che ha prodotti associati</p>
       
        </div>
        <div class="clear"></div>
    </div>
</body>
</html>
