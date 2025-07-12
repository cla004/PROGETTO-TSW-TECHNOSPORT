<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Registrazione</title>
    <!-- Collegamento al file CSS per lo stile della pagina -->
 <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/Registrazione.css">
</head>
<body>
<div class="background-container">
  <div class="form-container">

        <!-- Inizio form di registrazione -->
        <form class="registration-form" id="registrationForm" method="POST"
              action="${pageContext.request.contextPath}/registrazione">

            <!-- Titolo del form -->
            <h2>Registrati</h2>

            <!-- Campo per inserire il nome -->
            <div>
                <label for="name">Nome</label>
                <input type="text" id="name" name="name" required>
                <!-- Qui verrà mostrato l'errore relativo al campo nome -->
                <span class="error-message" id="nameError"></span>
            </div>

            <!-- Campo per inserire l'email -->
            <div>
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>
                <!-- Qui verrà mostrato l'errore relativo al campo email -->
                <span class="error-message" id="emailError"></span>
            </div>

            <!-- Campo per inserire la password -->
            <div>
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
                <!-- Qui verrà mostrato l'errore relativo al campo password -->
                <span class="error-message" id="passwordError"></span>
            </div>

            <!-- Campo per confermare la password -->
            <div>
                <label for="confirm-password">Conferma Password</label>
                <input type="password" id="confirm-password" name="confirm-password" required>
                <!-- Qui verrà mostrato l'errore relativo al campo conferma password -->
                <span class="error-message" id="confirmPasswordError"></span>
            </div>

            <!--
                Messaggio di successo nascosto di default,
                che apparirà solo dopo una registrazione valida
            -->
            <div id="successMessage" style="color:green;display:none;margin-top:10px">
                ✅ Registrazione avvenuta con successo
            </div>

            <!-- Bottone per inviare il form -->
            <button type="submit">Registrati</button>
        </form>
    </div>
</div>

<!-- Import del file JavaScript che gestisce la validazione e AJAX -->
<script src="${pageContext.request.contextPath}/Script/Validazione.js"></script>
</body>
</html>
