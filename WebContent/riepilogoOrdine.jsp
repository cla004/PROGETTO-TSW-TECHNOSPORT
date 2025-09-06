<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.CartItem" %>
<%@ page import="model.Utente" %>
<%@ page import="model.Indirizzo" %>
<%@ page import="model.MetodoPagamento" %>
<%@ page import="model.Taglia" %>
<%@ page import="model.TagliaDao" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Riepilogo Ordine - TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/checkout.css">
</head>
<body>

<header>
    <div class="logo">TecnoSport</div>
    <div class="checkout-progress">
        <span class="step">1. Indirizzo</span>
        <span class="step">2. Pagamento</span>
        <span class="step active">3. Riepilogo</span>
    </div>
</header>

<div class="container">
    <h1>📋 Riepilogo Finale del tuo Ordine</h1>

    <!-- Messaggi -->
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

    <%
        Indirizzo indirizzo = (Indirizzo) session.getAttribute("indirizzoSelezionato");
        MetodoPagamento metodo = (MetodoPagamento) session.getAttribute("pagamentoSelezionato");
        List<CartItem> prodottiCarrello = (List<CartItem>) session.getAttribute("carrelloSessione");
        
        double totale = 0.0;
        int numeroArticoli = 0;

        if (prodottiCarrello != null && !prodottiCarrello.isEmpty()) {
            for (CartItem item : prodottiCarrello) {
                totale += item.getProdotto().getPrezzo() * item.getQuantity();
                numeroArticoli += item.getQuantity();
            }
    %>

    <!-- Indirizzo di spedizione -->
    <div class="sezione">
        <h2>📍 Indirizzo di Spedizione</h2>
        <div class="info-selezionata">
            <% if (indirizzo != null) { %>
                <p><strong><%= indirizzo.getVia() %></strong></p>
                <p><%= indirizzo.getCitta() %>, <%= indirizzo.getCap() %></p>
                <p><%= indirizzo.getProvincia() %>, <%= indirizzo.getPaese() %></p>
            <% } else { %>
                <p style="color: red;">Nessun indirizzo selezionato</p>
            <% } %>
            <a href="checkout?step=indirizzo" class="link-modifica">Modifica</a>
        </div>
    </div>

    <!-- Metodo di pagamento -->
    <div class="sezione">
        <h2>💳 Metodo di Pagamento</h2>
        <div class="info-selezionata">
            <% if (metodo != null) { %>
                <p><strong><%= metodo.getTipo() %> <%= metodo.getNumeroCartaMascherato() %></strong></p>
                <p>Intestatario: <%= metodo.getIntestatario() %></p>
            <% } else { %>
                <p style="color: red;">Nessun metodo di pagamento selezionato</p>
            <% } %>
            <a href="checkout?step=pagamento" class="link-modifica">Modifica</a>
        </div>
    </div>

    <!-- Prodotti nell'ordine -->
    <div class="sezione">
        <h2>🛒 Prodotti nel tuo Ordine</h2>
        <div class="prodotti-riepilogo">
            <%
                TagliaDao tagliaDao = new TagliaDao();
                for (CartItem item : prodottiCarrello) {
                    Taglia taglia = tagliaDao.cercaTagliaById(item.getTagliaId());
                    String nomeTaglia = (taglia != null) ? taglia.getEtichetta() : "N/A";
            %>
                <div class="prodotto-riepilogo">
                    <img src="<%= request.getContextPath() + "/" + item.getProdotto().getImmagine() %>" 
                         alt="<%= item.getProdotto().getNome() %>" class="prodotto-immagine">
                    <div class="prodotto-dettagli">
                        <h4><%= item.getProdotto().getNome() %></h4>
                        <p><strong>Taglia:</strong> <%= nomeTaglia %></p>
                        <p><strong>Quantità:</strong> <%= item.getQuantity() %></p>
                        <p><strong>Prezzo unitario:</strong> € <%= String.format("%.2f", item.getProdotto().getPrezzo()) %></p>
                        <p><strong>Totale:</strong> € <%= String.format("%.2f", item.getProdotto().getPrezzo() * item.getQuantity()) %></p>
                    </div>
                </div>
            <% } %>
        </div>
    </div>

    <!-- Totale finale -->
    <div class="sezione totale-sezione">
        <h2>💰 Riepilogo Pagamento</h2>
        <div class="totali">
            <div class="totale-riga">
                <span>Articoli (<%= numeroArticoli %> pezzi):</span>
                <span>€ <%= String.format("%.2f", totale) %></span>
            </div>
            <div class="totale-riga">
                <span>Spedizione:</span>
                <span>Gratuita</span>
            </div>
            <div class="totale-riga totale-finale">
                <span><strong>Totale Ordine:</strong></span>
                <span><strong>€ <%= String.format("%.2f", totale) %></strong></span>
            </div>
        </div>
    </div>

    <!-- Finalizza ordine -->
    <div class="sezione">
        <form method="post" action="checkout">
            <input type="hidden" name="action" value="finalizza-ordine">
            <button type="submit" class="btn-finale">
                🎉 Conferma e Paga Ordine (€ <%= String.format("%.2f", totale) %>)
            </button>
        </form>
    </div>

    <!-- Navigazione -->
    <div class="checkout-nav">
        <a href="checkout?step=pagamento" class="btn-indietro">← Torna al pagamento</a>
    </div>

    <%
        } else {
    %>
        <div class="sezione">
            <h2>Carrello vuoto</h2>
            <p>Non ci sono prodotti nel carrello.</p>
            <a href="Homepage.jsp" class="btn-indietro">Torna alla Homepage</a>
        </div>
    <%
        }
    %>
</div>

<jsp:include page="footer.jsp" />

</body>
</html>
