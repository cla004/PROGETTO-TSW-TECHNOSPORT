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
      
      <%
          // Recupero le taglie disponibili per questo prodotto
          ProdottoTagliaDao ptDao = new ProdottoTagliaDao();
          TagliaDao tagliaDao = new TagliaDao();
          List<Prodotto_taglia> taglieDisponibili = ptDao.getTaglieDisponibiliPerProdotto(p.getId_prodotto());
          
          // DEBUG: Stampa info per capire il problema
          System.out.println("DEBUG - Prodotto ID: " + p.getId_prodotto() + ", Nome: " + p.getNome());
          System.out.println("DEBUG - Taglie trovate: " + taglieDisponibili.size());
          for(Prodotto_taglia pt : taglieDisponibili) {
              System.out.println("  - Taglia ID: " + pt.getid_taglia() + ", Quantità: " + pt.getQuantita_disponibili());
          }
      %>
      
      <% if (taglieDisponibili.isEmpty()) { %>
        <div class="no-taglie">
          ❌ Prodotto non disponibile
        </div>
      <% } else { %>
        <form action="carrello" method="post" onsubmit="return controllaForm(<%= p.getId_prodotto() %>)">
          <input type="hidden" name="action" value="aggiungi">
          <input type="hidden" name="prodottoId" value="<%= p.getId_prodotto() %>">
          <input type="hidden" name="tagliaId" id="taglia-<%= p.getId_prodotto() %>" value="">
          
          <div class="taglia-selector" id="taglia-selector-<%= p.getId_prodotto() %>">
            <p>Taglia:</p>
            <div class="taglia-buttons">
              <% for (Prodotto_taglia pt : taglieDisponibili) {
                   Taglia taglia = tagliaDao.cercaTagliaById(pt.getid_taglia());
              %>
                <button type="button" class="taglia-btn" data-prodotto="<%= p.getId_prodotto() %>" 
                        onclick="selezionaTaglia(<%= p.getId_prodotto() %>, <%= pt.getid_taglia() %>, this)">
                  <%= taglia.getEtichetta() %> (<%= (int)pt.getQuantita_disponibili() %>)
                </button>
              <% } %>
            </div>
          </div>
          
          <div class="quantita-box">
            <button type="button" class="quantita-btn" onclick="diminuisciQuantita(<%= p.getId_prodotto() %>)">-</button>
            <input type="number" name="quantita" id="quantita-<%= p.getId_prodotto() %>" 
                   value="1" min="1" class="quantita-input" readonly>
            <button type="button" class="quantita-btn" onclick="aumentaQuantita(<%= p.getId_prodotto() %>)">+</button>
          </div>
          
          <button type="submit" class="btn-aggiungi-carrello" id="add-btn-<%= p.getId_prodotto() %>" disabled>
            🛒 Aggiungi al carrello
          </button>
        </form>
      <% } %>
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
