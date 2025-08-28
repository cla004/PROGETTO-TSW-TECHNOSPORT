<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Pagamento - TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/Pagamento.css">
</head>
<body>

<h1>Pagamento</h1>

<form  id="pagamentoForm" action="${pageContext.request.contextPath}/pagamento" method="post">
    <label>Nome intestatario:
        <input type="text" name="nome" required>
    </label><br><br>

  <label>Numero carta:
    <input type="text" name="numeroCarta" required pattern="[0-9]{16}" maxlength="16">
</label><br><br>
   <label>Scadenza (MM/AA):
    <input type="text" name="scadenza" required pattern="[0-9]{2}/[0-9]{2}" placeholder="MM/AA" title="Inserisci la data nel formato MM/AA">
</label><br><br>
  <label>CVV:
    <input type="text" name="cvv" required pattern="[0-9]{3}" maxlength="3" placeholder="123" title="Inserisci un codice CVV di 3 cifre">
</label><br><br>
    <label>Indirizzo di spedizione:
        <input type="text" name="indirizzoSpedizione" required>
    </label><br><br>

    <button type="submit">Conferma Ordine</button>
</form>

<%-- Messaggio dal server --%>
<% 
    String msg = (String) request.getAttribute("messaggio"); 
    if (msg != null) { 
%>
    <p style="color: green;"><%= msg %></p>
<% } %>

<div id="messaggioJS" style="color: blue; margin-top: 20px;"></div>

<script>
    var form = document.getElementById('pagamentoForm');
    var messaggioJS = document.getElementById('messaggioJS');

    form.addEventListener('submit', function(event) {
        event.preventDefault(); // blocca invio immediato

        // Mostra messaggio lato client subito
        messaggioJS.textContent = "Ordine confermato! Grazie per l'acquisto.";

        // Invia il form dopo 10 secondi (10000 ms)
        setTimeout(function() {
            form.submit();
        }, 10000);
    });
</script>