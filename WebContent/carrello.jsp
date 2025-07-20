<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Carrello" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Il tuo Carrello - TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/Carrello.css">
</head>
<body>

<header>
    <div class="logo">TecnoSport</div>
    <nav>
        <ul>
            <li><a href="Homepage.jsp">🏠 Homepage</a></li>
            <li><a href="carrello">🛒 Carrello</a></li>
        </ul>
    </nav>
</header>

<div class="carrello-container">
    <h1>🛒 Il tuo Carrello</h1>
    
    <!-- Messaggi di successo/errore -->
    <% 
        String successo = (String) session.getAttribute("successo");
        String errore = request.getParameter("errore");
        if (successo != null) { 
    %>
        <div class="messaggio successo"><%= successo %></div>
        <% session.removeAttribute("successo"); %>
    <% } %>
    <% if (errore != null) { %>
        <div class="messaggio errore"><%= errore %></div>
    <% } %>
    
    <%
        List<Carrello> prodottiCarrello = (List<Carrello>) session.getAttribute("carrelloSessione");
        Double totale = 0.0;
        Integer numeroArticoli = 0;
        
        // Calcola totale e numero articoli
        if (prodottiCarrello != null) {
            for (Carrello item : prodottiCarrello) {
                totale += item.getProdotto().getPrezzo() * item.getQuantita();
                numeroArticoli += item.getQuantita();
            }
        }
        
        if (prodottiCarrello == null || prodottiCarrello.isEmpty()) {
    %>
        <div class="empty-cart">
            <h2>Il tuo carrello è vuoto</h2>
            <p>Aggiungi alcuni prodotti dalla nostra homepage!</p>
            <a href="Homepage.jsp" class="btn-continua">Continua lo shopping</a>
        </div>
    <%
        } else {
    %>

        <!-- Pulsante procedi all’ordine -->
        <form action="procediOrdine" method="post" style="margin-top: 20px; text-align: right;">
            <button type="submit" class="btn-checkout">Procedi all'ordine</button>
        </form>
        
        <!-- Pulsante svuota carrello -->
        <form action="carrello" method="post" style="display: inline;">
            <input type="hidden" name="action" value="svuota">
            <button type="submit" class="btn-svuota">
                🗑️ Svuota carrello
            </button>
        </form>
        
        <div style="clear: both; margin-bottom: 20px;"></div>
        
        <!-- Lista prodotti nel carrello -->
        <% for (Carrello item : prodottiCarrello) { %>
            <div class="carrello-item">
                <img src="images/prodotto.jpg" alt="<%= item.getProdotto().getNome() %>" class="item-image">
                
                <div class="item-info">
                    <div class="item-name"><%= item.getProdotto().getNome() %></div>
                    <div class="item-price">€ <%= item.getProdotto().getPrezzo() %></div>
                    <div>Descrizione: <%= item.getProdotto().getDescrizione() %></div>
                </div>
                
                <div class="quantity-controls">
                    <!-- Pulsante - (rimuovi 1) -->
                    <form action="carrello" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="rimuovi">
                        <input type="hidden" name="prodottoId" value="<%= item.getProdotto().getId_prodotto() %>">
                        <button type="submit" class="btn-quantity">-</button>
                    </form>
                    
                    <span class="quantity"><%= item.getQuantita() %></span>
                    
                    <!-- Pulsante + (aggiungi 1) -->
                    <form action="carrello" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="aggiungi">
                        <input type="hidden" name="prodottoId" value="<%= item.getProdotto().getId_prodotto() %>">
                        <button type="submit" class="btn-quantity">+</button>
                    </form>
                    
                    <!-- Pulsante elimina completamente -->
                    <form action="carrello" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="elimina">
                        <input type="hidden" name="prodottoId" value="<%= item.getProdotto().getId_prodotto() %>">
                        <button type="submit" class="btn-remove">
                            ❌ Elimina
                        </button>
                    </form>
                </div>
            </div>
        <% } %>
        
        <!-- Totale -->
        <div class="totale-container">
            <div>Articoli nel carrello: <strong><%= numeroArticoli %></strong></div>
            <div class="totale">Totale: € <%= totale %></div>
        </div>
        
        <a href="Homepage.jsp" class="btn-continua">Continua lo shopping</a>
    
    <% } %>
</div>

</body>
</html>
