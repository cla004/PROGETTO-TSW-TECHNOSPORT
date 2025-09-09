<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*, model.*" %>
<%
    // Controllo accesso admin
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
    if (isAdmin == null || !isAdmin) {
        response.sendRedirect("../Login.jsp");
        return;
    }
    
    Utente admin = (Utente) session.getAttribute("loggedInUser");
    
    // Caricamento utenti attivi (semplificato - tutti gli utenti per ora)
    List<Utente> utentiAttivi = new ArrayList<>();
    String errore = null;
    
    try {
        UtenteDao utenteDao = new UtenteDao();
        utentiAttivi = utenteDao.listaUtenti();
        
    } catch (Exception e) {
        errore = "Errore nel caricamento degli utenti: " + e.getMessage();
    }
    
    // Filtra admin dalla lista per mostrare solo clienti
    List<Utente> utentiClienti = new ArrayList<>();
    for (Utente u : utentiAttivi) {
        if (!"admin@calcioshop.it".equals(u.getEmail())) {
            utentiClienti.add(u);
        }
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Utenti Attivi - Admin TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
    <div class="header">
        <div class="header-buttons">
            <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-btn">🚪 Logout</a>
            <a href="utenti.jsp" class="users-btn">👥 Tutti gli Utenti</a>
            <a href="dashboard.jsp" class="dashboard-btn-green">📦 Dashboard</a>
        </div>
        <h1>Utenti Attivi</h1>
        <div class="clear"></div>
    </div>
    
    <div class="container">
        <div class="welcome">
            <h2>Utenti Clienti Attivi</h2>
            <p>Lista di tutti i clienti registrati che possono effettuare acquisti.</p>
        </div>
        
        <% if (errore != null) { %>
            <div class="error-message">
                <%= errore %>
            </div>
        <% } %>
        
        <!-- Statistiche -->
        <div class="stat-box">
            <div class="stat-number"><%= utentiClienti.size() %></div>
            <div class="stat-label">Clienti Attivi</div>
        </div>
        <div class="stat-box">
            <div class="stat-number"><%= utentiAttivi.size() %></div>
            <div class="stat-label">Utenti Totali</div>
        </div>
        <div class="clear"></div>
        
        <% if (utentiClienti.isEmpty()) { %>
            <div class="welcome">
                <p>Nessun cliente trovato nel sistema.</p>
            </div>
        <% } else { %>
            
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Email</th>
                        <th>Ordini</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        OrdineDao ordineDao = new OrdineDao();
                        for (Utente utente : utentiClienti) { 
                            int numeroOrdini = 0;
                            try {
                                List<Ordine> ordiniUtente = ordineDao.getOrdiniByUtente(utente.getId());
                                numeroOrdini = ordiniUtente.size();
                            } catch (Exception e) {
                                // Ignora errore conteggio ordini
                            }
                    %>
                    <tr>
                        <td><strong>#<%= utente.getId() %></strong></td>
                        <td><%= utente.getNome() %></td>
                        <td><%= utente.getEmail() %></td>
                        <td>
                            <% if (numeroOrdini > 0) { %>
                                <span class="status success"><%= numeroOrdini %> ordini</span>
                            <% } else { %>
                                <span class="status primary">Nessun ordine</span>
                            <% } %>
                        </td>
                        <td>
                            <a href="dettaglio-utente.jsp?id=<%= utente.getId() %>" class="btn">Dettagli</a>
                            <% if (numeroOrdini > 0) { %>
                                <a href="ordini-cliente.jsp?clienteId=<%= utente.getId() %>" class="btn btn-success">Vedi Ordini</a>
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            
        <% } %>
        
        <div class="card">
            <h3>Informazioni</h3>
            <p>• Questa pagina mostra solo i clienti (esclude amministratori)</p>
            <p>• Indica il numero di ordini effettuati da ogni cliente</p>
        </div>
        <div class="clear"></div>
    </div>

    <style>
        .status {
            padding: 5px 10px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: bold;
        }
        .status.success { background: #ccffcc; color: #009900; }
        .status.danger { background: #ffcccc; color: #cc0000; }
        .status.primary { background: #cce6ff; color: #0066cc; }
    </style>
</body>
</html>
