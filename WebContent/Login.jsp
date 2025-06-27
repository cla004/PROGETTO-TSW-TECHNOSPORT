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
      <form>
        <input type="text" placeholder="Username o email" required>
        <input type="password" placeholder="Password" required>
        <div class="forgot-password">
          <a href="#">Password dimenticata?</a>
        </div>
        <button type="submit">ENTRA NEL NEGOZIO</button>
      </form>
      <div class="signup">
        Non hai un account? <a href="#">Registrati</a>
      </div>
    </div>
  </div>
</body>
</html>