<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Carrello" %>
<%@ page import="model.Utente" %>
<%
    // Controllo se l'utente è loggato
    model.Utente utenteLoggato = (model.Utente) session.getAttribute("loggedInUser");
    if (utenteLoggato == null) {
        // Se non è loggato, reindirizza al login
        response.sendRedirect("Login.jsp?redirect=" + request.getRequestURL());
        return;
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Pagamento - TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/Pagamento.css">
</head>
<body>

<header>
    <div class="logo">TecnoSport</div>
    <nav>
        <ul>
            <li><a href="Homepage.jsp">🏠 Homepage</a></li>
            <li><a href="carrello?action=visualizza">🛒 Carrello</a></li>
            <li><a href="riepilogoOrdine.jsp">📋 Riepilogo Ordine</a></li>
        </ul>
    </nav>
</header>

<div class="pagamento-container">
    <h1>💳 Pagamento Sicuro</h1>

    <form id="pagamentoForm" action="${pageContext.request.contextPath}/pagamento" method="post">
        
        <!-- Sezione Modalità di Pagamento -->
        <div class="payment-section">
            <h3>Modalità di pagamento</h3>
            
            <!-- Campo Numero Carta -->
            <div class="card-number-container">
                <label for="numeroCarta">Dati della carta</label>
                <div class="card-input-wrapper">
                    <input type="text" id="numeroCarta" name="numeroCarta" 
                           placeholder="1234 1234 1234 1234" 
                           required pattern="[0-9\s]{19}" 
                           maxlength="19">
                    <div class="card-icons">
                        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Visa_Inc._logo.svg/2560px-Visa_Inc._logo.svg.png" alt="Visa" class="card-icon">
                        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Mastercard-logo.svg/1280px-Mastercard-logo.svg.png" alt="Mastercard" class="card-icon">
                        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/American_Express_logo_%282018%29.svg/1200px-American_Express_logo_%282018%29.svg.png" alt="American Express" class="card-icon">
                        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/JCB_logo.svg/1280px-JCB_logo.svg.png" alt="JCB" class="card-icon">
                    </div>
                </div>
            </div>
            
            <!-- Scadenza e CVV in riga -->
            <div class="card-details-row">
                <div class="expiry-field">
                    <label for="scadenza">MM / AA</label>
                    <input type="text" id="scadenza" name="scadenza" 
                           placeholder="MM/AA" 
                           required pattern="[0-9]{2}/[0-9]{2}" 
                           maxlength="5">
                </div>
                <div class="cvv-field">
                    <label for="cvv">CVC</label>
                    <input type="text" id="cvv" name="cvv" 
                           placeholder="123" 
                           required pattern="[0-9]{3,4}" 
                           maxlength="4">
                    <span class="cvv-icon">?</span>
                </div>
            </div>
            
            <!-- Nome Titolare -->
            <div class="cardholder-field">
                <label for="nome">Nome del titolare della carta</label>
                <input type="text" id="nome" name="nome" 
                       placeholder="Nome e cognome" 
                       required>
            </div>
        </div>
        
        <!-- Sezione Indirizzo di Spedizione -->
        <div class="shipping-section">
            <h3>📦 Indirizzo di spedizione</h3>
            <div class="address-field">
                <label for="indirizzoSpedizione">Indirizzo completo</label>
                <textarea id="indirizzoSpedizione" name="indirizzoSpedizione" 
                         placeholder="Via, numero civico, città, CAP, provincia" 
                         required rows="3"></textarea>
            </div>
        </div>
        
        <button type="submit" class="pay-button">💳 Conferma Ordine</button>
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

</div>

</body>
</html>
