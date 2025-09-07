<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Utente" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ordine Completato - TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/ordineCompletato.css">
</head>
<body>

<div class="container">
    <div class="icona-successo">🎉</div>
    
    <h1 class="titolo">Ordine Completato con Successo!</h1>
    
    <div class="messaggio">
        <%
            String messaggioSuccesso = (String) session.getAttribute("successo");
            Utente utente = (Utente) session.getAttribute("loggedInUser");
            String nomeCliente = (utente != null) ? utente.getNome() : "Cliente";
            
            if (messaggioSuccesso != null) {
        %>
            <p><%= messaggioSuccesso %></p>
            <% session.removeAttribute("successo"); %>
        <% } else { %>
            <p>🎉 Ordine confermato! Grazie per l'acquisto, <%= nomeCliente %>!</p>
        <% } %>
        
        <p>Riceverai a breve una email di conferma con tutti i dettagli.</p>
        <p>I prodotti verranno spediti all'indirizzo selezionato nei tempi previsti.</p>
    </div>

    <div class="dettagli">
        <h3>📋 Dettagli Ordine</h3>
        <div class="info">
            <strong>Data:</strong> <%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()) %>
        </div>
        <div class="info">
            <strong>Stato:</strong> <span style="color: #28a745;">✓ Confermato</span>
        </div>
        <div class="info">
            <strong>Cliente:</strong> <%= nomeCliente %>
        </div>
    </div>

    <div class="pulsanti">
        <a href="Homepage.jsp" class="btn">🏠 Torna alla Homepage</a>
        <a href="mieiOrdini.jsp" class="btn btn-secondario">📦 I miei ordini</a>
    </div>
</div>

</body>
</html>
