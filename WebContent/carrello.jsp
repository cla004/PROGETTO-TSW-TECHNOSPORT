<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.CartItem" %>
<%@ page import="model.TagliaDao" %>
<%@ page import="model.Taglia" %>
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
        List<CartItem> prodottiCarrello = (List<CartItem>) session.getAttribute("carrelloSessione");
        double totale = 0.0;
        int numeroArticoli = 0;

        if (prodottiCarrello != null && !prodottiCarrello.isEmpty()) {
            for (CartItem item : prodottiCarrello) {
                totale += item.getProdotto().getPrezzo() * item.getQuantity();
                numeroArticoli += item.getQuantity();
            }
    %>
            <!-- Pulsante svuota carrello -->
            <form action="carrello" method="post" style="margin-bottom: 15px;">
                <input type="hidden" name="action" value="svuota" />
                <button type="submit" class="btn-svuota">🗑️ Svuota carrello</button>
            </form>

            <%-- Lista prodotti --%>
            <% for (CartItem item : prodottiCarrello) { %>
                <div class="carrello-item">
                    <img src="<%= request.getContextPath() + "/" + item.getProdotto().getImmagine() %>" alt="<%= item.getProdotto().getNome() %>" class="item-image" />

                    <div class="item-info">
                        <h3><%= item.getProdotto().getNome() %></h3>
                        <p>Descrizione: <%= item.getProdotto().getDescrizione() %></p>
                        <%
                            TagliaDao tagliaDao = new TagliaDao();
                            Taglia taglia = tagliaDao.cercaTagliaById(item.getTagliaId());
                            String nomeTaglia = (taglia != null) ? taglia.getEtichetta() : "N/A";
                        %>
                        <p><strong>Taglia:</strong> <%= nomeTaglia %></p>
                        <p>Prezzo unitario: € <%= String.format("%.2f", item.getProdotto().getPrezzo()) %></p>
                    </div>

                    <div class="quantity-controls">
                        <!-- Rimuovi 1 -->
                        <form action="carrello" method="post" style="display: inline;">
                            <input type="hidden" name="action" value="rimuovi" />
                            <input type="hidden" name="prodottoId" value="<%= item.getProdotto().getId_prodotto() %>" />
                            <input type="hidden" name="tagliaId" value="<%= item.getTagliaId() %>" />
                            <button type="submit" class="btn-quantity">-</button>
                        </form>

                        <span class="quantity"><%= item.getQuantity() %></span>

                        <!-- Aggiungi 1 -->
                        <form action="carrello" method="post" style="display: inline;">
                            <input type="hidden" name="action" value="aggiungi" />
                            <input type="hidden" name="prodottoId" value="<%= item.getProdotto().getId_prodotto() %>" />
                            <input type="hidden" name="tagliaId" value="<%= item.getTagliaId() %>" />
                            <input type="hidden" name="quantita" value="1" />
                            <button type="submit" class="btn-quantity">+</button>
                        </form>

                        <!-- Elimina prodotto -->
                        <form action="carrello" method="post" style="display: inline;">
                            <input type="hidden" name="action" value="elimina" />
                            <input type="hidden" name="prodottoId" value="<%= item.getProdotto().getId_prodotto() %>" />
                            <input type="hidden" name="tagliaId" value="<%= item.getTagliaId() %>" />
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
            <a href="checkout" class="btn-continua">📋 Procedi all'Ordine</a>
		  
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
