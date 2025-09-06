<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.MetodoPagamento" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Scegli Metodo di Pagamento - TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/checkout.css">
</head>
<body>

<header>
    <div class="logo">TecnoSport</div>
    <div class="checkout-progress">
        <span class="step">1. Indirizzo</span>
        <span class="step active">2. Pagamento</span>
        <span class="step">3. Riepilogo</span>
    </div>
</header>

<div class="container">
    <h1>💳 Seleziona Metodo di Pagamento</h1>

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
        List<MetodoPagamento> metodi = (List<MetodoPagamento>) request.getAttribute("metodi");
        if (metodi != null && !metodi.isEmpty()) {
    %>
        <!-- Metodi esistenti -->
        <div class="sezione">
            <h2>💳 I tuoi metodi di pagamento salvati</h2>
            <div class="metodi-lista">
                <% for (MetodoPagamento metodo : metodi) { %>
                    <div class="metodo-opzione">
                        <div class="metodo-info">
                            <p><strong><%= metodo.getTipo() %> <%= metodo.getNumeroCartaMascherato() %></strong></p>
                            <p>Intestatario: <%= metodo.getIntestatario() %></p>
                            <p>Scadenza: <%= metodo.getScadenza() %></p>
                            <% if (metodo.isPredefinito()) { %>
                                <p><small>✅ Metodo predefinito</small></p>
                            <% } %>
                        </div>
                        <div class="metodo-azione">
                            <form method="post" action="checkout">
                                <input type="hidden" name="action" value="conferma-pagamento">
                                <input type="hidden" name="metodoPagamentoId" value="<%= metodo.getId() %>">
                                <button type="submit" class="btn-seleziona">✓ Usa questo metodo</button>
                            </form>
                        </div>
                    </div>
                <% } %>
            </div>
        </div>
    <% } %>

    <!-- Aggiungi nuovo metodo -->
    <div class="sezione">
        <h2>➕ Aggiungi Nuovo Metodo di Pagamento</h2>
        <div class="nuovo-indirizzo-form">
            <form method="post" action="checkout">
                <input type="hidden" name="action" value="salva-pagamento">

                <div class="form-row">
                    <div class="form-group">
                        <label for="tipo">Tipo di Carta *</label>
                        <select id="tipo" name="tipo" required>
                            <option value="">Seleziona...</option>
                            <option value="Visa">Visa</option>
                            <option value="Mastercard">Mastercard</option>
                            <option value="American Express">American Express</option>
                            <option value="PayPal">PayPal</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="intestatario">Intestatario *</label>
                        <input type="text" id="intestatario" name="intestatario" required placeholder="Nome e cognome">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="numeroCarta">Numero Carta *</label>
                        <input type="text" id="numeroCarta" name="numeroCarta" required 
                               placeholder="1234 5678 9012 3456" maxlength="19">
                    </div>
                    <div class="form-group">
                        <label for="scadenza">Scadenza *</label>
                        <input type="text" id="scadenza" name="scadenza" required 
                               placeholder="MM/YYYY" pattern="[0-9]{2}/[0-9]{4}" maxlength="7">
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-salva">💾 Salva e Continua</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Info sicurezza -->
    <div class="sezione">
        <h2>🔒 Informazioni sulla Sicurezza</h2>
        <div style="background-color: #f8f9fa; padding: 15px; border-radius: 5px;">
            <p><strong>🛡️ I tuoi dati sono al sicuro!</strong></p>
            <ul>
                <li>✅ Salviamo solo le ultime 4 cifre della carta</li>
                <li>✅ Non memorizziamo mai il CVV</li>
                <li>✅ Tutte le transazioni sono criptate</li>
                <li>✅ Puoi rimuovere i metodi salvati in qualsiasi momento</li>
            </ul>
        </div>
    </div>

    <!-- Navigazione -->
    <div class="checkout-nav">
        <a href="checkout?step=indirizzo" class="btn-indietro">← Torna agli indirizzi</a>
    </div>
</div>

<jsp:include page="footer.jsp" />

<script>
// Script basilare per formattazione numero carta
document.getElementById('numeroCarta').addEventListener('input', function (e) {
    let value = e.target.value.replace(/\s+/g, '').replace(/[^0-9]/gi, '');
    let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
    if (formattedValue.length <= 19) {
        this.value = formattedValue;
    }
});

// Script per formattazione scadenza
document.getElementById('scadenza').addEventListener('input', function (e) {
    let value = e.target.value.replace(/\D/g, '');
    if (value.length >= 2) {
        value = value.substring(0, 2) + '/' + value.substring(2, 6);
    }
    this.value = value;
});
</script>

</body>
</html>
