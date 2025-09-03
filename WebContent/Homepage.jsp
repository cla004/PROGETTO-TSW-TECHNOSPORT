<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Categoria" %>
<%@ page import="model.Prodotti" %>
<%@ page import="model.CategoriaDao"%>
<%@ page import="model.ProdottiDao" %>


<%
    // Controllo login utente
    String username = (String) session.getAttribute("username");
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
    Object loggedInUser = session.getAttribute("loggedInUser");

    CategoriaDao catDao = new CategoriaDao();
    ProdottiDao prodDao = new ProdottiDao();
    List<Categoria> categories = catDao.listaCategorie();
    
   
%>



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
      <%
        // Generazione dinamica dei link di navigazione dalle categorie
        for (Categoria navCat : categories) {
      %>
      <li><a href="#<%= navCat.getnome_recensione() %>"><%= navCat.getnome_recensione().substring(0,1).toUpperCase() + navCat.getnome_recensione().substring(1) %></a></li>
      <%
        }
      %>
      <li><a href="carrello.jsp">🛒 Carrello</a></li>

      <% if (loggedInUser == null) { %>
        <li><a href="Login.jsp">🔐 Login</a></li>
        <li><a href="Registrazione.jsp">📝 Registrati</a></li>
      <% } else { %>
        <li><a href="mieiOrdini.jsp">📋 I miei Ordini</a></li>
        <li><a href="logout">🚪 Logout</a></li>
      <% } %>

      <% if (loggedInUser != null) { %>
        <li><span style="color: #2ecc71; font-weight: bold;">👋 Benvenuto, <%= ((model.Utente) loggedInUser).getNome() %>!</span></li>
        <% if (isAdmin != null && isAdmin) { %>
          <li><a href="adminDashboard.jsp">⚙️ Admin</a></li>
        <% } %>
      <% } %>
    </ul>
  </nav>
</header>

<!-- === HERO === -->

<section class="hero">
  <h1>Scopri tutto per la tua stagione sportiva 2025</h1>
  <p>Più di 100 prodotti in offerta tra maglie, scarpe e accessori</p>
</section>

<%
// da qui in poi non va
    // Loop dinamico su categorie e prodotti
    for (Categoria c : categories) {
        List<Prodotti> prodotti = prodDao.findByCategoriaId(c.getid_categoria());
%>
<section id="<%= c.getnome_recensione() %>">
  <h2><%= c.getnome_recensione().substring(0,1).toUpperCase() + c.getnome_recensione().substring(1) %></h2>
  <div class="grid">
    <%
        for (Prodotti p : prodotti) {
    %>
    <div class="card">
    

      <img src="<%= request.getContextPath() + "/" + p.getImmagine() %>" alt="<%= p.getNome() %>">
      <h3><%= p.getNome() %></h3>
      <p><strong>€<%= p.getPrezzo()%></strong></p>
      <form action="carrello" method="post">
        <input type="hidden" name="action" value="aggiungi">
        <input type="hidden" name="prodottoId" value="<%= p.getId_prodotto() %>">
        <button type="submit" style="background: #28a745; color: white; border: none; padding: 10px 15px; border-radius: 5px; cursor: pointer;">
          🛒 Aggiungi al carrello
        </button>
      </form>
    </div>
    <%
        } // fine prodotti
    %>
  </div>
</section>
<%
    } // fine categorie
%>

<jsp:include page="footer.jsp" />

</body>
</html>
