<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
   <meta name="viewport" content="width=device-width, initial-scale=1.0, viewport-fit=cover">
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
              
            <%-- Campo nascosto per conservare il redirect --%>
            <%-- Usa il parametro dal server se presente, altrimenti quello dalla URL --%>
            <input type="hidden" name="redirect" value="${not empty requestScope.redirectParam ? requestScope.redirectParam : param.redirect}">

            <!-- Titolo del form -->
            <h2>Registrati</h2>

            <!-- Campo per inserire il nome -->
            <div>
                <label for="name">Nome</label>
                <input type="text" id="name" name="name" required>
                <!-- Qui verrà mostrato l'errore relativo al campo nome -->
                <span class="error-message" id="nameError">${requestScope.errorName}</span>
            </div>

            <!-- Campo per inserire l'email -->
            <div>
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>
                <!-- Qui verrà mostrato l'errore relativo al campo email -->
                <span class="error-message" id="emailError">${requestScope.errorEmail}</span>
            </div>

            <!-- Campo per inserire la password -->
            <div>
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
                <!-- Qui verrà mostrato l'errore relativo al campo password -->
                <span class="error-message" id="passwordError">${requestScope.errorPassword}</span>
            </div>

            <!-- Campo per confermare la password -->
            <div>
                <label for="confirm-password">Conferma Password</label>
                <input type="password" id="confirm-password" name="confirm-password" required>
                <!-- Qui verrà mostrato l'errore relativo al campo conferma password -->
                <span class="error-message" id="confirmPasswordError">${requestScope.errorConfirmPassword}</span>
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
            
            <!-- Link per tornare al login -->
            <div class="login-link" style="text-align:center; margin-top: 15px;">
                <%-- Mantiene il parametro redirect anche tornando al login --%>
                <%-- Prende il parametro dal server se presente, altrimenti dalla URL --%>
                <% 
                    String redirectParam = (String) request.getAttribute("redirectParam");
                    if (redirectParam == null || redirectParam.isEmpty()) {
                        redirectParam = request.getParameter("redirect");
                    }
                %>
                <% if (redirectParam != null && !redirectParam.isEmpty()) { %>
                  Hai già un account? <a href="Login.jsp?redirect=<%= redirectParam %>" style="color: #007bff; text-decoration: none;">Accedi</a>
                <% } else { %>
                  Hai già un account? <a href="Login.jsp" style="color: #007bff; text-decoration: none;">Accedi</a>
                <% } %>
            </div>
        </form>
    </div>
</div>

<!-- Import del file JavaScript che gestisce la validazione e AJAX -->
<script src="${pageContext.request.contextPath}/Script/Validazione.js"></script>
</body>
</html>
