<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.DecimalFormat, model.*" %>
<%
    // Controllo accesso admin
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
    if (isAdmin == null || !isAdmin) {
        response.sendRedirect("../Login.jsp");
        return;
    }
    
    Utente admin = (Utente) session.getAttribute("loggedInUser");
    
    // Calcolo statistiche
    int totaleProdotti = 0;
    int totaleUtenti = 0;
    int ordiniOggi = 0;
    double venditeMese = 0.0;
    String venditeFormatted = "0,00";
    String erroreStatistiche = null;
    
    try {
        ProdottiDao prodottiDao = new ProdottiDao();
        UtenteDao utenteDao = new UtenteDao();
        OrdineDao ordineDao = new OrdineDao();
        
        totaleProdotti = prodottiDao.contaProdotti();
        totaleUtenti = utenteDao.contaUtenti();
        ordiniOggi = ordineDao.contaOrdiniOggi();
        venditeMese = ordineDao.calcolaVenditeMese();
        
        // Formattazione per visualizzazione
        DecimalFormat df = new DecimalFormat("#,##0.00");
        venditeFormatted = df.format(venditeMese);
        
    } catch (Exception e) {
        erroreStatistiche = "Errore nel caricamento delle statistiche: " + e.getMessage();
        e.printStackTrace();
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dashboard Admin - TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
    <div class="header">
        <div class="header-content">
            <a href="${pageContext.request.contextPath}/Homepage.jsp" class="btn homepage-btn">🏠 Torna alla Home</a>
            <h1>Dashboard Amministratore - TecnoSport</h1>
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">🚪 Logout</a>
        </div>
        <div class="clear"></div>
    </div>
    
    <div class="container">
        <div class="welcome">
            <h2>Benvenuto, <%= admin.getNome() %>!</h2>
            <p>Gestisci il tuo e-commerce TecnoSport da questo pannello di controllo.</p>
            
            <% if (erroreStatistiche != null) { %>
                <div class="error-message">
                    <%= erroreStatistiche %>
                </div>
            <% } %>
        </div>
        
        <!-- Statistiche veloce -->
        <div class="stat-box">
            <div class="stat-number"><%= ordiniOggi %></div>
            <div class="stat-label">Ordini Oggi</div>
        </div>
        <div class="stat-box">
            <div class="stat-number"><%= totaleProdotti %></div>
            <div class="stat-label">Prodotti</div>
        </div>
        <div class="stat-box">
            <div class="stat-number"><%= totaleUtenti %></div>
            <div class="stat-label">Utenti</div>
        </div>
        <div class="stat-box">
            <div class="stat-number"><%= venditeFormatted %> €</div>
            <div class="stat-label">Vendite Mese</div>
        </div>
        <div class="clear"></div>
        
        <!-- Sezioni principali -->
        <div class="card">
            <h3>🛍️ Gestione Catalogo</h3>
            <p>Aggiungi, modifica o elimina prodotti</p>
            <a href="catalogo.jsp">Visualizza Catalogo</a>
            <a href="aggiungi-prodotto.jsp">Aggiungi Prodotto</a>
        </div>
        
        <div class="card">
            <h3>📦 Gestione Ordini</h3>
            <p>Visualizza tutti gli ordini ricevuti</p>
            <a href="ordini.jsp">Tutti gli Ordini</a>
            <a href="ordini-data.jsp">Ordini per Data</a>
            <a href="ordini-cliente.jsp">Ordini per Cliente</a>
        </div>
        <div class="clear"></div>
        
        <div class="card">
            <h3>👥 Gestione Utenti</h3>
            <p>Informazioni sugli utenti registrati</p>
            <a href="utenti.jsp">Lista Utenti</a>
            <a href="utenti-attivi.jsp">Utenti Attivi</a>
        </div>
        
        <div class="card">
            <h3>Statistiche</h3>
            <p>Report e analisi vendite</p>
            <a href="prodotti-popolari.jsp">Prodotti Piu Venduti</a>
            <a href="clienti-top.jsp">Clienti Top</a>
        </div>
        <div class="clear"></div>
    </div>
</body>
</html>
