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
    
    // Caricamento categorie dal database
    List<Categoria> categorie = new ArrayList<>();
    String erroreCategorie = null;
    
    try {
        CategoriaDao categoriaDao = new CategoriaDao();
        categorie = categoriaDao.listaCategorie();
    } catch (Exception e) {
        erroreCategorie = "Errore nel caricamento delle categorie: " + e.getMessage();
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Aggiungi Prodotto - Admin TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
    <div class="header">
        <h1>Aggiungi Nuovo Prodotto</h1>
        <a href="dashboard.jsp" class="dashboard-btn">📦 Torna alla Dashboard</a>
        <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-btn">🚪 Logout</a>
        <div class="clear"></div>
    </div>
    
    <div class="container">
        <div class="welcome">
            <h2>Inserisci i dati del nuovo prodotto</h2>
            <p>Compila tutti i campi obbligatori per aggiungere il prodotto al catalogo.</p>
        </div>
        
        <% String errore = (String) request.getAttribute("errore"); %>
        <% if (errore != null) { %>
            <div class="error-message">
                <%= errore %>
            </div>
        <% } %>
        
        <% String successo = (String) request.getAttribute("successo"); %>
        <% if (successo != null) { %>
            <div class="success-message">
                <%= successo %>
            </div>
        <% } %>
        
        <form method="post" action="${pageContext.request.contextPath}/admin/prodotti" enctype="multipart/form-data">
            <input type="hidden" name="action" value="aggiungi">
            
            <div class="form-group">
                <label for="nome">Nome Prodotto *</label>
                <input type="text" id="nome" name="nome" required 
                       placeholder="Es: Scarpe da calcio Nike" 
                       value="<%= request.getParameter("nome") != null ? request.getParameter("nome") : "" %>">
            </div>
            
            <div class="form-group">
                <label for="descrizione">Descrizione</label>
                <textarea id="descrizione" name="descrizione" rows="4" 
                          placeholder="Descrizione dettagliata del prodotto..."><%= request.getParameter("descrizione") != null ? request.getParameter("descrizione") : "" %></textarea>
            </div>
            
            <div class="form-group">
                <label for="prezzo">Prezzo (€) *</label>
                <input type="number" id="prezzo" name="prezzo" step="0.01" min="0" required 
                       placeholder="Es: 99.99"
                       value="<%= request.getParameter("prezzo") != null ? request.getParameter("prezzo") : "" %>">
            </div>
            
            <div class="form-group">
                <label for="categoria">Categoria *</label>
                <% if (erroreCategorie != null) { %>
                    <div class="error-message">
                        <%= erroreCategorie %>
                    </div>
                <% } %>
                <select id="categoria" name="categoria" required>
                    <option value="">Seleziona categoria</option>
                    <% for (Categoria cat : categorie) { %>
                        <option value="<%= cat.getid_categoria() %>">
                            <%= cat.getnome_recensione() %>
                        </option>
                    <% } %>
                    <% if (categorie.isEmpty()) { %>
                        <option value="" disabled>Nessuna categoria disponibile</option>
                    <% } %>
                </select>
            </div>
            
            <div class="form-group">
                <label for="immagine">Immagine Prodotto</label>
                <input type="file" id="immagine" name="immagine" accept="image/*">
                <small>Formati supportati: JPG, PNG, GIF (max 5MB)</small>
            </div>
            
            <div class="form-group">
                <label for="stock">Quantità Disponibile *</label>
                <input type="number" id="stock" name="stock" min="0" required 
                       placeholder="Es: 50"
                       value="<%= request.getParameter("stock") != null ? request.getParameter("stock") : "" %>">
            </div>
            
            <button type="submit" class="btn btn-success">Aggiungi Prodotto</button>
            <a href="catalogo.jsp" class="btn">Annulla</a>
        </form>
    </div>
</body>
</html>
