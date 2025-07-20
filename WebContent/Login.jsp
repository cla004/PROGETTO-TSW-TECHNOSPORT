<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>TECHNOSPORT - Login</title>
 <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/Login.css">

  <style>
    .error-message {
      color: red;
      font-size: 0.9em;
      margin-left: 10px;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="image-section"></div>
    <div class="form-section">
      <h1>TECHNO<span>SPORT</span></h1>
      <h2>Accedi al tuo account</h2>
      <p>Scopri le offerte esclusive riservate agli iscritti!</p>
      
  <form id="loginForm" action="${pageContext.request.contextPath}/login" method="post">

        <input type="text" id="email" name="email" placeholder="Email" required>
        <span id="emailError" class="error-message"></span>

        <input type="password" id="password" name="password" placeholder="Password" required>
        <span id="passwordError" class="error-message"></span>

        <button type="submit">ENTRA NEL NEGOZIO</button>
      </form>

      <div class="signup">
        Non hai un account? <a href="Registrazione.jsp">Registrati</a>
      </div>
    </div>
  </div>

<script src="${pageContext.request.contextPath}/Script/ValidateLogin.js"></script>

</body>
</html>
