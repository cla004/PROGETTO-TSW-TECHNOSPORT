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
    
    // Ottengo l'ID del prodotto da modificare
    String idStr = request.getParameter("id");
    if (idStr == null || idStr.trim().isEmpty()) {
        response.sendRedirect("catalogo.jsp");
        return;
    }
    
    int prodottoId = Integer.parseInt(idStr);
    
    // Carico il prodotto dal database
    ProdottiDao prodottiDao = new ProdottiDao();
    Prodotti prodotto = prodottiDao.cercaProdottoById(prodottoId);
    
    if (prodotto == null) {
        request.setAttribute("errore", "Prodotto non trovato");
        response.sendRedirect("catalogo.jsp");
        return;
    }
    
    // Caricamento categorie dal database
    List<Categoria> categorie = new ArrayList<>();
    String erroreCategorie = null;

    // Caricamento taglie dal database
    List<Taglia> tutteLeTaglie = new ArrayList<>();
    String erroreTaglie = null;
    
    // Caricamento associazioni prodotto-taglia
    List<Prodotto_taglia> taglieAssociate = new ArrayList<>();
    
    try {
        CategoriaDao categoriaDao = new CategoriaDao();
        categorie = categoriaDao.listaCategorie();
        
        TagliaDao tagliaDao = new TagliaDao();
        tutteLeTaglie = tagliaDao.listaTaglie();
        
        ProdottoTagliaDao ptDao = new ProdottoTagliaDao();
        taglieAssociate = ptDao.getTaglieDisponibiliPerProdotto(prodottoId);
        
    } catch (Exception e) {
        if (erroreCategorie == null) erroreCategorie = "Errore nel caricamento delle categorie: " + e.getMessage();
        erroreTaglie = "Errore nel caricamento delle taglie: " + e.getMessage();
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Modifica Prodotto - Admin TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
    <div class="header">
        <h1>Modifica Prodotto</h1>
        <a href="catalogo.jsp" class="dashboard-btn">📦 Torna al Catalogo</a>
        <a href="dashboard.jsp" class="dashboard-btn">🏠 Dashboard</a>
        <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-btn">🚪 Logout</a>
        <div class="clear"></div>
    </div>
    
    <div class="container">
        <div class="welcome">
            <h2>Modifica: <%= prodotto.getNome() %></h2>
            <p>Aggiorna i dati del prodotto. I campi contrassegnati con * sono obbligatori.</p>
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
            <input type="hidden" name="action" value="modifica">
            <input type="hidden" name="id" value="<%= prodotto.getId_prodotto() %>">
            
            <div class="form-group">
                <label for="nome">Nome Prodotto *</label>
                <input type="text" id="nome" name="nome" required 
                       placeholder="Es: Scarpe da calcio Nike" 
                       value="<%= prodotto.getNome() %>">
            </div>
            
            <div class="form-group">
                <label for="descrizione">Descrizione</label>
                <textarea id="descrizione" name="descrizione" rows="4" 
                          placeholder="Descrizione dettagliata del prodotto..."><%= prodotto.getDescrizione() != null ? prodotto.getDescrizione() : "" %></textarea>
            </div>
            
            <div class="form-group">
                <label for="prezzo">Prezzo (€) *</label>
                <input type="number" id="prezzo" name="prezzo" step="0.01" min="0" required 
                       placeholder="Es: 99.99"
                       value="<%= prodotto.getPrezzo() %>">
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
                        <option value="<%= cat.getid_categoria() %>" 
                                <%= (cat.getid_categoria() == prodotto.getId_categoria()) ? "selected" : "" %>>
                            <%= cat.getnome_recensione() %>
                        </option>
                    <% } %>
                </select>
            </div>
            
            <div class="form-group">
                <label for="immagine">Immagine Prodotto</label>
                <% if (prodotto.getImmagine() != null && !prodotto.getImmagine().isEmpty()) { %>
                    <div style="margin-bottom: 10px;">
                        <small>Immagine attuale:</small><br>
                        <img src="${pageContext.request.contextPath}/<%= prodotto.getImmagine() %>" 
                             alt="<%= prodotto.getNome() %>" style="max-width: 200px; max-height: 200px;">
                    </div>
                <% } %>
                <input type="file" id="immagine" name="immagine" accept="image/*">
                <small>Formati supportati: JPG, PNG, GIF (max 5MB). Lascia vuoto per mantenere l'immagine attuale.</small>
            </div>
            
            <div class="form-group">
                <label for="stock">Stock Totale Prodotto *</label>
                <input type="number" id="stock" name="stock" min="1" required 
                       placeholder="Es: 100"
                       value="<%= prodotto.getQuantita_disponibili() %>">
                <small>Numero totale di pezzi di questo prodotto</small>
            </div>
            
            <div class="form-group">
                <label>Taglie Associate</label>
                <% if (taglieAssociate.isEmpty()) { %>
                    <p style="color: #666;">Nessuna taglia associata a questo prodotto.</p>
                <% } else { %>
                    <div class="taglie-associate">
                        <% 
                        TagliaDao tagliaDao = new TagliaDao();
                        for (Prodotto_taglia pt : taglieAssociate) { 
                            Taglia taglia = tagliaDao.cercaTagliaById(pt.getid_taglia());
                        %>
                            <div class="taglia-item">
                                <strong><%= taglia.getEtichetta() %></strong>: 
                                <%= (int)pt.getQuantita_disponibili() %> pezzi disponibili
                            </div>
                        <% } %>
                    </div>
                <% } %>
                <small>Le associazioni taglia-quantità vanno gestite separatamente.</small>
            </div>
            
            <button type="submit" class="btn btn-success">💾 Salva Modifiche</button>
            <a href="catalogo.jsp" class="btn">❌ Annulla</a>
        </form>
    </div>

<style>
.taglie-associate {
    border: 1px solid #ddd;
    padding: 10px;
    border-radius: 4px;
    background-color: #f9f9f9;
}
.taglia-item {
    padding: 5px 0;
    border-bottom: 1px solid #eee;
}
.taglia-item:last-child {
    border-bottom: none;
}
</style>

</body>
</html>
