<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Utente" %>
<%@ page import="model.Ordine" %>
<%@ page import="model.OrdineDao" %>
<%@ page import="model.DettaglioOrdine" %>
<%@ page import="model.DettaglioOrdineDao" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.SQLException" %>
<%
    // Controllo se l'utente è loggato
    model.Utente utenteLoggato = (model.Utente) session.getAttribute("loggedInUser");
    if (utenteLoggato == null) {
        // Se non è loggato, reindirizza al login
        response.sendRedirect("Login.jsp?redirect=" + request.getRequestURL());
        return;
    }

    // Recupera gli ordini dell'utente
    List<Ordine> ordiniUtente = null;
    DettaglioOrdineDao dettaglioDao = null;
    try {
        OrdineDao ordineDao = new OrdineDao();
        dettaglioDao = new DettaglioOrdineDao();
        ordiniUtente = ordineDao.getOrdiniByUtente(utenteLoggato.getId());
    } catch (SQLException e) {
        // In caso di errore, inizializza una lista vuota
        ordiniUtente = new java.util.ArrayList<Ordine>();
        e.printStackTrace();
    }
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>I miei Ordini - TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/mieiOrdini.css">
</head>
<body>

<header>
    <div class="logo">TecnoSport</div>
    <nav>
        <ul>
            <li><a href="Homepage.jsp">🏠 Homepage</a></li>
            <li><a href="carrello?action=visualizza">🛒 Carrello</a></li>
            <li><a href="mieiOrdini.jsp" class="active">📋 I miei Ordini</a></li>
            <li><a href="logout">🚪 Logout</a></li>
        </ul>
    </nav>
</header>

<div class="ordini-container">
    <h1>📋 I miei Ordini</h1>
    
    <div class="utente-info">
        <p>Benvenuto, <strong><%= utenteLoggato.getNome() %></strong></p>
        <p>Email: <%= utenteLoggato.getEmail() %></p>
    </div>

    <%
        if (ordiniUtente != null && !ordiniUtente.isEmpty()) {
    %>
            <div class="ordini-lista">
                <% for (Ordine ordine : ordiniUtente) { %>
                    <div class="ordine-card">
                        <div class="ordine-header">
                            <div class="ordine-numero">
                                <h3>Ordine #<%= ordine.getId() %></h3>
                                <span class="data-ordine">
                                    <% if (ordine.getData() != null) { %>
                                        <%= dateFormat.format(ordine.getData()) %>
                                    <% } else { %>
                                        Data non disponibile
                                    <% } %>
                                </span>
                            </div>
                            <div class="ordine-stato">
                                <span class="stato-badge stato-<%= ordine.getStato() != null ? ordine.getStato().toLowerCase() : "sconosciuto" %>">
                                    <%= ordine.getStato() != null ? ordine.getStato() : "N/A" %>
                                </span>
                            </div>
                        </div>
                        
                        <div class="ordine-dettagli">
                            <div class="ordine-info">
                                <p><strong>Totale:</strong> <span class="prezzo-totale">€ <%= String.format("%.2f", ordine.getTotale()) %></span></p>
                            </div>
                            
                            <%-- Recupera i dettagli dell'ordine --%>
                            <%
                                List<DettaglioOrdine> dettagli = null;
                                try {
                                    if (dettaglioDao != null) {
                                        dettagli = dettaglioDao.getDettagliByOrdineId(ordine.getId());
                                    }
                                } catch (SQLException e) {
                                    dettagli = new java.util.ArrayList<DettaglioOrdine>();
                                    e.printStackTrace();
                                }
                                if (dettagli != null && !dettagli.isEmpty()) {
                            %>
                                <div class="prodotti-ordine">
                                    <h4>Prodotti ordinati:</h4>
                                    <% for (DettaglioOrdine dettaglio : dettagli) { %>
                                        <div class="prodotto-dettaglio">
                                            <span class="prodotto-nome"><%= dettaglio. getNomeProdotto() %></span>
                                            <span class="prodotto-quantita">Quantità: <%= dettaglio.getQuantita() %></span>
                                            <span class="prodotto-prezzo">€ <%= String.format("%.2f", dettaglio.getPrezzoUnitario()) %></span>
                                        </div>
                                    <% } %>
                                </div>
                            <% } %>
                        </div>
                    </div>
                <% } %>
            </div>

    <%
        } else {
    %>
        <div class="nessun-ordine">
            <div class="icona-vuoto">📦</div>
            <h2>Nessun ordine trovato</h2>
            <p>Non hai ancora effettuato nessun ordine.</p>
            <a href="Homepage.jsp" class="btn-continua">Inizia lo shopping</a>
        </div>
    <%
        }
    %>
</div>

</body>
</html>
