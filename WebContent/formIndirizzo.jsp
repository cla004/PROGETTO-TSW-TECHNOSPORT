<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Indirizzo" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <%= "modifica".equals(request.getAttribute("azione")) ? "Modifica" : "Aggiungi" %> 
        Indirizzo - TecnoSport
    </title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/formIndirizzi.css">
</head>
<body>

<header>
    <div class="logo">TecnoSport</div>
    <nav>
        <ul>
            <li><a href="Homepage.jsp">🏠 Homepage</a></li>
            <li><a href="indirizzi">📍 I miei Indirizzi</a></li>
            <li><a href="metodiPagamento">💳 Metodi Pagamento</a></li>
        </ul>
    </nav>
</header>

<div class="container">
    <%
        String azione = (String) request.getAttribute("azione");
        Indirizzo indirizzo = (Indirizzo) request.getAttribute("indirizzo");
        boolean isModifica = "modifica".equals(azione);
        
        String titolo = isModifica ? "✏️ Modifica Indirizzo" : "➕ Aggiungi Nuovo Indirizzo";
        String actionForm = isModifica ? "aggiorna" : "salva";
        String testoBottone = isModifica ? "Aggiorna Indirizzo" : "Salva Indirizzo";
    %>
    
    <h1><%= titolo %></h1>

    <!-- Messaggi di errore -->
    <%
        String errore = (String) session.getAttribute("errore");
        if (errore != null) {
    %>
        <div class="messaggio errore"><%= errore %></div>
        <% session.removeAttribute("errore"); %>
    <% } %>

    <div class="form-container">
        <form method="post" action="indirizzi">
            <input type="hidden" name="action" value="<%= actionForm %>">
            <% if (isModifica && indirizzo != null) { %>
                <input type="hidden" name="id" value="<%= indirizzo.getId() %>">
            <% } %>

            <div class="form-group">
                <label for="via">Via *</label>
                <input type="text" 
                       id="via" 
                       name="via" 
                       value="<%= (indirizzo != null && indirizzo.getVia() != null) ? indirizzo.getVia() : "" %>"
                       required>
                <small>Es: Via Roma 123</small>
            </div>

            <div class="form-group">
                <label for="citta">Città *</label>
                <input type="text" 
                       id="citta" 
                       name="citta" 
                       value="<%= (indirizzo != null && indirizzo.getCitta() != null) ? indirizzo.getCitta() : "" %>"
                       required>
                <small>Es: Milano</small>
            </div>

            <div class="form-group">
                <label for="cap">CAP *</label>
                <input type="text" 
                       id="cap" 
                       name="cap" 
                       value="<%= (indirizzo != null && indirizzo.getCap() != null) ? indirizzo.getCap() : "" %>"
                       required
                       pattern="[0-9]{5}"
                       maxlength="5">
                <small>5 cifre, es: 20100</small>
            </div>

            <div class="form-group">
                <label for="provincia">Provincia *</label>
                <input type="text" 
                       id="provincia" 
                       name="provincia" 
                       value="<%= (indirizzo != null && indirizzo.getProvincia() != null) ? indirizzo.getProvincia() : "" %>"
                       required
                       maxlength="2">
                <small>2 lettere, es: MI</small>
            </div>

            <div class="form-group">
                <label for="paese">Paese *</label>
                <input type="text" 
                       id="paese" 
                       name="paese" 
                       value="<%= (indirizzo != null && indirizzo.getPaese() != null) ? indirizzo.getPaese() : "Italia" %>"
                       required>
                <small>Es: Italia</small>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn-salva"><%= testoBottone %></button>
                <a href="indirizzi" class="btn-annulla">Annulla</a>
            </div>
        </form>
    </div>

    <div class="info-box">
        <h3>ℹ️ Informazioni</h3>
        <ul>
            <li>Tutti i campi contrassegnati con * sono obbligatori</li>
            <li>L'indirizzo verrà utilizzato per le tue spedizioni</li>
            <li>Puoi modificare o eliminare l'indirizzo in qualsiasi momento</li>
        </ul>
    </div>
</div>

<jsp:include page="footer.jsp" />

</body>
</html>
