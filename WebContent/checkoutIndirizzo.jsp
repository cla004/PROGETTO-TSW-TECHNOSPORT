<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Indirizzo" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Scegli Indirizzo - TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/checkout.css">
</head>
<body>

<header>
    <div class="logo">TecnoSport</div>
    <div class="checkout-progress">
        <span class="step active">1. Indirizzo</span>
        <span class="step">2. Pagamento</span>
        <span class="step">3. Riepilogo</span>
    </div>
</header>

<div class="container">
    <h1>📍 Seleziona Indirizzo di Spedizione</h1>

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
        List<Indirizzo> indirizzi = (List<Indirizzo>) request.getAttribute("indirizzi");
        if (indirizzi != null && !indirizzi.isEmpty()) {
    %>
        <!-- Indirizzi esistenti -->
        <div class="sezione">
            <h2>🏠 I tuoi indirizzi salvati</h2>
            <div class="indirizzi-lista">
                <% for (Indirizzo indirizzo : indirizzi) { %>
                    <div class="indirizzo-opzione">
                        <div class="indirizzo-info">
                            <p><strong><%= indirizzo.getVia() %></strong></p>
                            <p><%= indirizzo.getCitta() %>, <%= indirizzo.getCap() %></p>
                            <p><%= indirizzo.getProvincia() %>, <%= indirizzo.getPaese() %></p>
                        </div>
                        <div class="indirizzo-azione">
                            <form method="post" action="checkout">
                                <input type="hidden" name="action" value="conferma-indirizzo">
                                <input type="hidden" name="indirizzoId" value="<%= indirizzo.getId() %>">
                                <button type="submit" class="btn-seleziona">✓ Usa questo indirizzo</button>
                            </form>
                        </div>
                    </div>
                <% } %>
            </div>
        </div>
    <% } %>

    <!-- Aggiungi nuovo indirizzo -->
    <div class="sezione">
        <h2>➕ Aggiungi Nuovo Indirizzo</h2>
        <div class="nuovo-indirizzo-form">
            <form method="post" action="checkout">
                <input type="hidden" name="action" value="salva-indirizzo">

                <div class="form-row">
                    <div class="form-group">
                        <label for="via">Via *</label>
                        <input type="text" id="via" name="via" required placeholder="Es: Via Roma 123">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="citta">Città *</label>
                        <input type="text" id="citta" name="citta" required placeholder="Es: Milano">
                    </div>
                    <div class="form-group">
                        <label for="cap">CAP *</label>
                        <input type="text" id="cap" name="cap" required pattern="[0-9]{5}" maxlength="5" placeholder="20100">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="provincia">Provincia *</label>
                        <input type="text" id="provincia" name="provincia" required maxlength="2" placeholder="MI">
                    </div>
                    <div class="form-group">
                        <label for="paese">Paese *</label>
                        <input type="text" id="paese" name="paese" required value="Italia">
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-salva">💾 Salva e Continua</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Navigazione -->
    <div class="checkout-nav">
        <a href="carrello?action=visualizza" class="btn-indietro">← Torna al carrello</a>
    </div>
</div>

<jsp:include page="footer.jsp" />

</body>
</html>
