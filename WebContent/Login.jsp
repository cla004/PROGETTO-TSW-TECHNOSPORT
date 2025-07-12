<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>TECHNOSPORT - Login</title>
  <link rel="stylesheet" href="styles/Login.css">
</head>
<body>
  <div class="container">
    <div class="image-section"></div>
    <div class="form-section">
      <h1>TECHNO<span>SPORT</span></h1>
      <h2>Accedi al tuo account</h2>
      <p>Scopri le offerte esclusive riservate agli iscritti!</p>
      <form action="${pageContext.request.contextPath}/login" method="POST">
        <% String loginError = (String) request.getAttribute("loginError");
           if (loginError != null && !loginError.isEmpty()) { %>
            <p style="color: red; text-align: center; margin-bottom: 15px;"><%= loginError %></p>
        <% } %>
        <% String registrationSuccess = (String) request.getAttribute("registrationSuccess");
           if (registrationSuccess != null && !registrationSuccess.isEmpty()) { %>
            <p style="color: green; text-align: center; margin-bottom: 15px;"><%= registrationSuccess %></p>
        <% } %>

        <input type="text" placeholder="Username o email" name="usernameOrEmail" required value="<%= request.getParameter("usernameOrEmail") != null ? request.getParameter("usernameOrEmail") : "" %>">
        <input type="password" placeholder="Password" name="password" required>
        <button type="submit">ENTRA NEL NEGOZIO</button>
      </form>
      <div class="signup">
        Non hai un account? <a href="Registrazione.jsp">Registrati</a>
      </div>
    </div>
  </div>
</body>
</html>