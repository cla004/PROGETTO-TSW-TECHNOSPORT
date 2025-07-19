<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>CalcioShop - Articoli Sportivi</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/Homepage.css" />

</head>

<body>
  <header>
    <div class="logo">TecnoSport</div>
    <nav>
      <ul>
        <li><a href="#maglie">Maglie</a></li>
        <li><a href="#scarpe">Scarpe</a></li>
        <li><a href="#tute">Tute</a></li>
        <li><a href="#accessori">Accessori</a></li>
        <li><a href="#nazionali">Nazionali</a></li>
        <li><a href="#allenamento">Allenamento</a></li>
        <li><a href="carrello.jsp">🛒 Carrello</a></li>
      </ul>
    </nav>
  </header>

  <section class="hero">
    <h1>Scopri tutto per la tua stagione sportiva 2025</h1>
    <p>Più di 100 prodotti in offerta tra maglie, scarpe e accessori</p>
  </section>

  <!-- === MAGLIE === -->
  <section id="maglie">
    <h2>Maglie ufficiali 2025</h2>
    <div class="grid">
      <% for (int i = 1; i <= 15; i++) { %>
        <div class="card">
          <img src="Images/maglia<%= i %>.jpg" alt="Maglia <%= i %>">
          <h3>Maglia Club <%= i %></h3>
          <p>Edizione 2025 | Materiale tecnico</p>
          <p><strong>€79,99</strong></p>
        </div>
      <% } %>
    </div>
  </section>

  <!-- === SCARPE === -->
  <section id="scarpe">
    <h2>Scarpe da calcio</h2>
    <div class="grid">
      <% for (int i = 1; i <= 15; i++) { %>
        <div class="card">
          <img src="Images/scarpa<%= i %>.jpg" alt="Scarpa <%= i %>">
          <h3>Nike Phantom <%= i %></h3>
          <p>Suola FG - Tomaia in microfibra</p>
          <p><strong>€109,99</strong></p>
        </div>
      <% } %>
    </div>
  </section>

  <!-- === TUTE === -->
  <section id="tute">
    <h2>Tute da allenamento</h2>
    <div class="grid">
      <% for (int i = 1; i <= 15; i++) { %>
        <div class="card">
          <img src="Images/tuta<%= i %>.jpg" alt="Tuta <%= i %>">
          <h3>Tuta Adidas <%= i %></h3>
          <p>Giacca + pantalone in tessuto tecnico</p>
          <p><strong>€69,99</strong></p>
        </div>
      <% } %>
    </div>
  </section>

  <!-- === ACCESSORI === -->
  <section id="accessori">
    <h2>Accessori da campo</h2>
    <div class="grid">
      <% for (int i = 1; i <= 15; i++) { %>
        <div class="card">
          <img src="Images/accessorio<%= i %>.jpg" alt="Accessorio <%= i %>">
          <h3>Accessorio Sport <%= i %></h3>
          <p>Pallone, borraccia, parastinchi ecc.</p>
          <p><strong>da €9,99</strong></p>
        </div>
      <% } %>
    </div>
  </section>

  <!-- === NAZIONALI === -->
  <section id="nazionali">
    <h2>Maglie Nazionali 2024/25</h2>
    <div class="grid">
      <% String[] nazioni = {"Italia", "Francia", "Argentina", "Germania", "Spagna"}; 
         for (int i = 0; i < 15; i++) { %>
        <div class="card">
          <img src="Images/nazionale<%= i %>.jpg" alt="Maglia Nazionale <%= i %>">
          <h3>Maglia <%= nazioni[i % nazioni.length] %> <%= 2024 + i %></h3>
          <p>Ufficiale | Pro Edition</p>
          <p><strong>€94,99</strong></p>
        </div>
      <% } %>
    </div>
  </section>

  <!-- === ALLENAMENTO === -->
  <section id="allenamento">
    <h2>Prodotti per l’allenamento</h2>
    <div class="grid">
      <% for (int i = 1; i <= 15; i++) { %>
        <div class="card">
          <img src="Images/allenamento<%= i %>.jpg" alt="Allenamento <%= i %>">
          <h3>Kit allenamento <%= i %></h3>
          <p>Top, shorts, felpe e accessori training</p>
          <p><strong>da €29,99</strong></p>
        </div>
      <% } %>
    </div>
  </section>

<jsp:include page="${pageContext.request.contextPath}/footer.jsp" />

</body>
</html>
