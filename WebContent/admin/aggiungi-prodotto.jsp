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

    // Caricamento taglie dal database (tutte)
    List<Taglia> tutteLeTaglie = new ArrayList<>();
    String erroreTaglie = null;
    
    try {
        CategoriaDao categoriaDao = new CategoriaDao();
        categorie = categoriaDao.listaCategorie();
        
        TagliaDao tagliaDao = new TagliaDao();
        tutteLeTaglie = tagliaDao.listaTaglie();
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
    <title>Aggiungi Prodotto - Admin TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
    <script src="${pageContext.request.contextPath}/Script/taglie-distributor.js"></script>
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
            <input type="hidden" name="action" value="aggiungi_nuovo">
            
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
                    <option value="" data-catname="">Seleziona categoria</option>
                    <% for (Categoria cat : categorie) { %>
                        <option value="<%= cat.getid_categoria() %>" data-catname="<%= cat.getnome_recensione() %>">
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
            
            <!-- NUOVA SEZIONE: Distribuzione Taglie -->
            <div class="form-group">
                <h3>📏 Distribuzione Taglie</h3>
                <p>Seleziona le taglie disponibili per questo prodotto e distribuisci lo stock totale tra esse.</p>
                
                <div class="form-group">
                    <label for="stock">Stock Totale Prodotto *</label>
                    <input type="number" id="stock" name="stock" min="1" required 
                           placeholder="Es: 100"
                           value="<%= request.getParameter("stock") != null ? request.getParameter("stock") : "" %>">
                    <small>Numero totale di pezzi di questo prodotto da distribuire tra le taglie</small>
                </div>
                
                <!-- Riepilogo Stock in tempo reale -->
                <div class="stock-summary">
                    <div class="stock-info-box">
                        <p><strong>Stock totale:</strong> <span id="stock-totale-display">0</span></p>
                        <p><strong>Già distribuito:</strong> <span id="stock-distribuito-display">0</span></p>
                        <p id="stock-rimasto-line" class="stock-available">
                            <strong>Stock rimasto da distribuire:</strong> <span id="stock-rimasto-display">0</span>
                        </p>
                    </div>
                </div>
                
                <!-- Messaggio di errore dinamico -->
                <div id="errore-distribuzione" class="error-message" style="display: none;"></div>
            
                <!-- Lista Taglie -->
                <% if (erroreTaglie != null) { %>
                    <div class="error-message"><%= erroreTaglie %></div>
                <% } %>
                
                <div class="taglie-distribution">
                    <% for (Taglia t : tutteLeTaglie) { %>
                        <div class="taglia-row">
                            <div class="taglia-checkbox-container">
                                <input type="checkbox" 
                                       id="checkbox_<%= t.getid_taglia() %>" 
                                       name="taglie_selezionate" 
                                       value="<%= t.getid_taglia() %>"
                                       class="taglia-checkbox">
                                <label for="checkbox_<%= t.getid_taglia() %>">📏 <%= t.getEtichetta() %></label>
                            </div>
                            <div class="taglia-quantity-container">
                                <label for="quantita_<%= t.getid_taglia() %>">Quantità:</label>
                                <input type="number" 
                                       id="quantita_<%= t.getid_taglia() %>" 
                                       name="quantita_<%= t.getid_taglia() %>"
                                       min="0" 
                                       placeholder="0"
                                       class="taglia-quantity-input">
                                <small id="max_<%= t.getid_taglia() %>" class="max-label">(Max: 0)</small>
                            </div>
                        </div>
                    <% } %>
                </div>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-success">🎯 Aggiungi Prodotto con Taglie</button>
                <a href="catalogo.jsp" class="btn">❌ Annulla</a>
            </div>
        </form>
    </div>

<style>
/* Stili per il nuovo sistema di distribuzione taglie */
.stock-summary {
    background: #ffffff;
    border: 2px solid #007bff;
    border-radius: 8px;
    padding: 15px;
    margin: 15px 0;
}

.stock-info-box p {
    margin: 5px 0;
    font-size: 16px;
}

.stock-available {
    color: #28a745;
}

.stock-error {
    color: #dc3545;
    font-weight: bold;
}

.stock-perfect {
    color: #007bff;
    font-weight: bold;
}

.taglie-distribution {
    display: grid;
    gap: 10px;
    margin-top: 15px;
}

.taglia-row {
    display: grid;
    grid-template-columns: 1fr 2fr;
    align-items: center;
    gap: 20px;
    background: #f8f9fa;
    border: 1px solid #dee2e6;
    border-radius: 5px;
    padding: 15px;
}

.taglia-checkbox-container {
    display: flex;
    align-items: center;
    gap: 10px;
}

.taglia-checkbox {
    transform: scale(1.2);
}

.taglia-checkbox-container label {
    font-weight: bold;
    color: #343a40;
}

.taglia-quantity-container {
    display: flex;
    align-items: center;
    gap: 10px;
}

.taglia-quantity-container label {
    min-width: 70px;
}

.taglia-quantity-input {
    width: 80px;
    padding: 5px;
    border: 1px solid #ced4da;
    border-radius: 3px;
    text-align: center;
}

.taglia-quantity-input:disabled {
    background-color: #f5f5f5;
    color: #6c757d;
}

.max-label {
    color: #6c757d;
    font-size: 12px;
}

.form-actions {
    text-align: center;
    margin-top: 30px;
}

.form-actions .btn {
    margin: 0 10px;
    padding: 12px 25px;
    font-size: 16px;
}
</style>

</body>
</html>
