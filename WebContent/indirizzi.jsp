<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Indirizzo" %>
<%@ page import="model.Utente" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>I miei Indirizzi - TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/indirizzi.css">
</head>
<body>

<header>
    <div class="logo">TecnoSport</div>
    <nav>
        <ul>
            <li><a href="Homepage.jsp">🏠 Homepage</a></li>
            <li><a href="carrello?action=visualizza">🛒 Carrello</a></li>
            <li><a href="indirizzi">📍 I miei Indirizzi</a></li>
            <li><a href="metodiPagamento">💳 Metodi Pagamento</a></li>
            <li><a href="mieiOrdini.jsp">📋 I miei Ordini</a></li>
            <li><a href="logout">🚪 Logout</a></li>
        </ul>
    </nav>
</header>

<div class="container">
    <h1>📍 I miei Indirizzi di Spedizione</h1>

    <!-- Messaggi di successo o errore -->
    <%
        String successo = (String) session.getAttribute("successo");
        String errore = (String) session.getAttribute("errore");
        if (successo != null) {
    %>
        <div class="messaggio successo"><%= successo %></div>
        <% session.removeAttribute("successo"); %>
    <% } %>
    <% if (errore != null) { %>
        <div class="messaggio errore"><%= errore %></div>
        <% session.removeAttribute("errore"); %>
    <% } %>

    <!-- Pulsante per aggiungere nuovo indirizzo -->
    <div class="azioni-top">
        <a href="indirizzi?action=aggiungi" class="btn-aggiungi">➕ Aggiungi Nuovo Indirizzo</a>
    </div>

    <%
        List<Indirizzo> indirizzi = (List<Indirizzo>) request.getAttribute("indirizzi");
        if (indirizzi != null && !indirizzi.isEmpty()) {
    %>
        <div class="indirizzi-grid">
            <% for (Indirizzo indirizzo : indirizzi) { %>
                <div class="indirizzo-card">
                    <div class="indirizzo-info">
                        <h3>📍 Indirizzo</h3>
                        <p><strong>Via:</strong> <%= indirizzo.getVia() %></p>
                        <p><strong>Città:</strong> <%= indirizzo.getCitta() %></p>
                        <p><strong>CAP:</strong> <%= indirizzo.getCap() %></p>
                        <p><strong>Provincia:</strong> <%= indirizzo.getProvincia() %></p>
                        <p><strong>Paese:</strong> <%= indirizzo.getPaese() %></p>
                    </div>
                    
                    <div class="indirizzo-azioni">
                        <a href="indirizzi?action=modifica&id=<%= indirizzo.getId() %>" class="btn-modifica">✏️ Modifica</a>
                        
                        <!-- Form per eliminazione (più sicuro di un semplice link) -->
                        <form method="post" action="indirizzi?action=elimina&id=<%= indirizzo.getId() %>" style="display: inline;">
                            <input type="hidden" name="action" value="elimina">
                            <input type="hidden" name="id" value="<%= indirizzo.getId() %>">
                            <button type="submit" class="btn-elimina">🗑️ Elimina</button>
                        </form>
                    </div>
                </div>
            <% } %>
        </div>
    <%
        } else {
    %>
        <div class="nessun-indirizzo">
            <h2>📭 Nessun indirizzo salvato</h2>
            <p>Non hai ancora aggiunto nessun indirizzo di spedizione.</p>
            <a href="indirizzi?action=aggiungi" class="btn-aggiungi-grande">➕ Aggiungi il tuo primo indirizzo</a>
        </div>
    <% } %>

    <!-- Link di navigazione -->
    <div class="navigazione">
        <a href="metodiPagamento" class="btn-navigazione">💳 Gestisci Metodi di Pagamento</a>
        <a href="Homepage.jsp" class="btn-navigazione">🏠 Torna alla Homepage</a>
    </div>
</div>

<jsp:include page="footer.jsp" />

</body>
</html>
