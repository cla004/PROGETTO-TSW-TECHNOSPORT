<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Categoria" %>
<%@ page import="model.Prodotti" %>
<%@ page import="model.CategoriaDao"%>
<%@ page import="model.ProdottiDao" %>
<%@ page import="model.Taglia" %>
<%@ page import="model.TagliaDao" %>
<%@ page import="model.Prodotto_taglia" %>
<%@ page import="model.ProdottoTagliaDao" %>


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
          <li><a href="admin/dashboard.jsp">⚙️ Admin</a></li>
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
      
      <%
          // Recupero le taglie disponibili per questo prodotto
          ProdottoTagliaDao ptDao = new ProdottoTagliaDao();
          TagliaDao tagliaDao = new TagliaDao();
          List<Prodotto_taglia> taglieDisponibili = ptDao.getTaglieDisponibiliPerProdotto(p.getId_prodotto());
      %>
      
      <div class="product-actions">
        <% if (taglieDisponibili.isEmpty()) { %>
          <div class="no-taglie">
            ❌ Prodotto non disponibile
          </div>
        <% } else { %>
          <div class="taglie-preview">
            <p><strong>Taglie disponibili:</strong> 
            <% for (int i = 0; i < Math.min(taglieDisponibili.size(), 3); i++) {
                 Prodotto_taglia pt = taglieDisponibili.get(i);
                 Taglia taglia = tagliaDao.cercaTagliaById(pt.getid_taglia());
            %>
              <span class="taglia-preview"><%= taglia.getEtichetta() %></span><% if (i < Math.min(taglieDisponibili.size(), 3) - 1) { %>, <% } %>
            <% } %>
            <% if (taglieDisponibili.size() > 3) { %>...<% } %>
            </p>
          </div>
        <% } %>
        
        <button type="button" class="btn-vedi-prodotto" onclick="location.href='product.jsp?id=<%= p.getId_prodotto() %>'">
          👁️ Vedi Prodotto
        </button>
      </div>
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

<script src="${pageContext.request.contextPath}/Script/TagliaSelector.js"></script>
</body>
</html>
