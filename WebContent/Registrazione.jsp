<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrazione - Maglie Calcio Online</title>
    <link rel="stylesheet" href="styles/Registrazione.css">
</head>
<body>


<div class="background-container">
 <div class="form-container">

<form class="registration-form" action="${pageContext.request.contextPath}/registrazione"  method="POST" id="registrationForm">


    <h2>Registrati</h2>

    <div>
        <label for="name">Nome</label>
        <input type="text" id="name" name="name" required>
        <small class="error-message" id="nameError"></small>
    </div>

    <div>
        <label for="email">Email</label>
        <input type="email" id="email" name="email" required>
        <small class="error-message" id="emailError"></small>
    </div>

    <div>
        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>
        <small class="error-message" id="passwordError"></small>
    </div>

    <div>
        <label for="confirm-password">Conferma Password</label>
        <input type="password" id="confirm-password" name="confirm-password" required>
        <small class="error-message" id="confirmPasswordError"></small>
    </div>

    <small class="error-message">${error}</small>

    <button type="submit">Registrati</button>
</form>
</div>
</div>
<script src="script/validazione.js"></script>