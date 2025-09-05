<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.CartItem" %>
<%@ page import="model.Utente" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Riepilogo Ordine - TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/riepilogoOrdine.css">
</head>
<body>

<header>
    <div class="logo">TecnoSport</div>
    <nav>
        <ul>
            <li><a href="Homepage.jsp">🏠 Homepage</a></li>
            <li><a href="carrello?action=visualizza">🛒 Carrello</a></li>
        </ul>
    </nav>
</header>

<div class="riepilogo-container">
    <h1>📋 Riepilogo del tuo Ordine</h1>

    <%
        List<CartItem> prodottiCarrello = (List<CartItem>) session.getAttribute("carrelloSessione");
        double totale = 0.0;
        int numeroArticoli = 0;

        if (prodottiCarrello != null && !prodottiCarrello.isEmpty()) {
            for (CartItem item : prodottiCarrello) {
                totale += item.getProdotto().getPrezzo() * item.getQuantity();
                numeroArticoli += item.getQuantity();
            }
    %>
            
            <div class="ordine-dettagli">
                <h2>Prodotti nel tuo ordine:</h2>
                
                <% for (CartItem item : prodottiCarrello) { %>
                    <div class="prodotto-riepilogo">
                        <div class="prodotto-info">
                            <h3><%= item.getProdotto().getNome() %></h3>
                            <p><%= item.getProdotto().getDescrizione() %></p>
                        </div>
                        <div class="prodotto-prezzi">
                            <span class="quantita">Quantità: <%= item.getQuantity() %></span>
                            <span class="prezzo-unitario">€ <%= String.format("%.2f", item.getProdotto().getPrezzo()) %> cad.</span>
                            <span class="prezzo-totale">€ <%= String.format("%.2f", item.getProdotto().getPrezzo() * item.getQuantity()) %></span>
                        </div>
                    </div>
                <% } %>
            </div>

            <div class="totale-finale">
                <div class="totale-riga">
                    <span>Articoli totali: </span>
                    <span><%= numeroArticoli %></span>
                </div>
                <div class="totale-riga totale-principale">
                    <span>Totale da pagare: </span>
                    <span>€ <%= String.format("%.2f", totale) %></span>
                </div>
            </div>

            <div class="azioni-ordine">
                <a href="carrello?action=visualizza" class="btn-torna">← Torna al Carrello</a>
                <a href="javascript:procediAllOrdine()" class="btn-procedi">Procedi al Pagamento 💳</a>
            </div>

    <%
        } else {
    %>
        <div class="carrello-vuoto">
            <h2>Il carrello è vuoto</h2>
            <p>Non ci sono prodotti nel tuo carrello.</p>
            <a href="Homepage.jsp" class="btn-torna">Vai alla Homepage</a>
        </div>
    <%
        }
    %>
</div>

<script>
function procediAllOrdine() {
    // Controlla se l'utente è loggato
    <%
        model.Utente utente = (model.Utente) session.getAttribute("loggedInUser");
        if (utente == null) {
    %>
        // L'utente non è loggato, reindirizza al login
        alert("Devi effettuare il login per completare l'ordine!");
        // Reindirizza al login con il parametro redirect
        window.location.href = "Login.jsp?redirect=" + encodeURIComponent("Pagamento.jsp");
    <%
        } else {
    %>
        // L'utente è loggato, vai alla pagina di pagamento
        window.location.href = "Pagamento.jsp";
    <%
        }
    %>
}
</script>

</body>
</html>
