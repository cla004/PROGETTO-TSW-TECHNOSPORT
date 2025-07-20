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

<form action="pagamento" method="post">
    <label>Nome intestatario:
        <input type="text" name="nome" required>
    </label><br><br>

    <label>Numero carta:
        <input type="text" name="numeroCarta" required pattern="\\d{16}" maxlength="16">
    </label><br><br>

    <label>Scadenza (MM/AA):
        <input type="text" name="scadenza" required pattern="\\d{2}/\\d{2}">
    </label><br><br>

    <label>CVV:
        <input type="text" name="cvv" required pattern="\\d{3}" maxlength="3">
    </label><br><br>

    <label>Indirizzo di spedizione:
        <input type="text" name="indirizzoSpedizione" required>
    </label><br><br>

    <button type="submit">Conferma Ordine</button>
</form>

</body>
</html>
