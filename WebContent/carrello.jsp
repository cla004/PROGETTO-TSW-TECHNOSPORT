<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Carrello" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8" />
    <title>Il tuo Carrello - TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/Carrello.css" />
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

<div class="carrello-container">
    <h1>🛒 Il tuo Carrello</h1>

    <!-- Messaggi di successo o errore -->
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
        double totale = 0.0;
        int numeroArticoli = 0;

        if (prodottiCarrello != null && !prodottiCarrello.isEmpty()) {
            for (Carrello item : prodottiCarrello) {
                totale += item.getProdotto().getPrezzo() * item.getQuantita();
                numeroArticoli += item.getQuantita();
            }
    %>
            <!-- Pulsante svuota carrello -->
            <form action="carrello" method="post" style="margin-bottom: 15px;">
                <input type="hidden" name="action" value="svuota" />
                <button type="submit" class="btn-svuota">🗑️ Svuota carrello</button>
            </form>

            <%-- Lista prodotti --%>
            <% for (Carrello item : prodottiCarrello) { %>
                <div class="carrello-item">
                    <img src="images/prodotto.jpg" alt="<%= item.getProdotto().getNome() %>" class="item-image" />

                    <div class="item-info">
                        <h3><%= item.getProdotto().getNome() %></h3>
                        <p>Descrizione: <%= item.getProdotto().getDescrizione() %></p>
                        <p>Prezzo unitario: € <%= String.format("%.2f", item.getProdotto().getPrezzo()) %></p>
                    </div>

                    <div class="quantity-controls">
                        <!-- Rimuovi 1 -->
                        <form action="carrello" method="post" style="display: inline;">
                            <input type="hidden" name="action" value="rimuovi" />
                            <input type="hidden" name="prodottoId" value="<%= item.getProdotto().getId_prodotto() %>" />
                            <button type="submit" class="btn-quantity">-</button>
                        </form>

                        <span class="quantity"><%= item.getQuantita() %></span>

                        <!-- Aggiungi 1 -->
                        <form action="carrello" method="post" style="display: inline;">
                            <input type="hidden" name="action" value="aggiungi" />
                            <input type="hidden" name="prodottoId" value="<%= item.getProdotto().getId_prodotto() %>" />
                            <button type="submit" class="btn-quantity">+</button>
                        </form>

                        <!-- Elimina prodotto -->
                        <form action="carrello" method="post" style="display: inline;">
                            <input type="hidden" name="action" value="elimina" />
                            <input type="hidden" name="prodottoId" value="<%= item.getProdotto().getId_prodotto() %>" />
                            <button type="submit" class="btn-remove">❌ Elimina</button>
                        </form>
                    </div>
                </div>
            <% } %>

            <div class="totale-container">
                <strong>Articoli totali: </strong> <%= numeroArticoli %><br/>
                <strong>Totale: </strong>€ <%= String.format("%.2f", totale) %>
            </div>

            <a href="Homepage.jsp" class="btn-continua">Continua lo shopping</a>
            <a href="Pagamento.jsp" class="btn-continua">Procedi all' ordine </a>
		  
    <%
        } else {
    %>
        <div class="empty-cart">
            <h2>Il tuo carrello è vuoto</h2>
            <p>Aggiungi alcuni prodotti dalla nostra homepage!</p>
            <a href="Homepage.jsp" class="btn-continua">Continua lo shopping</a>
        </div>
    <%
        }
    %>
</div>

</body>
</html>
