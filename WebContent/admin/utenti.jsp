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
    
    // Parametri paginazione
    int limit = 20; // utenti per pagina
    int currentPage = 0;   // pagina corrente (0-based)
    
    String pageParam = request.getParameter("page");
    if (pageParam != null && !pageParam.isEmpty()) {
        try {
            currentPage = Integer.parseInt(pageParam);
            if (currentPage < 0) currentPage = 0;
        } catch (NumberFormatException e) {
            currentPage = 0;
        }
    }
    
    // Calcola OFFSET
    int offset = currentPage * limit;
    
    // Caricamento utenti con paginazione
    List<Utente> utenti = new ArrayList<>();
    int totalLength = 0;
    int totalPages = 0;
    String errore = null;
    
    try {
        UtenteDao utenteDao = new UtenteDao();
        
        // Conta totale utenti
        totalLength = utenteDao.contaUtenti();
        
        // Calcola numero pagine
        totalPages = (int) Math.ceil((double) totalLength / limit);
        
        // Carica utenti della pagina corrente
        utenti = utenteDao.getUtentiConPaginazione(limit, offset);
        
    } catch (Exception e) {
        errore = "Errore nel caricamento degli utenti: " + e.getMessage();
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista Utenti - Admin TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
    <div class="header">
        <h1>Lista Utenti Registrati</h1>
        <a href="dashboard.jsp" class="dashboard-btn-green">📦 Dashboard</a>
        <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-btn">🚪 Logout</a>
        <div class="clear"></div>
    </div>
    
    <div class="container">
        <div class="welcome">
            <h2>Gestione Utenti</h2>
            <p>Visualizza e gestisci tutti gli utenti registrati nel sistema.</p>
        </div>
        
        <% if (errore != null) { %>
            <div class="error-message">
                <%= errore %>
            </div>
        <% } %>
        
        <!-- Statistiche utenti -->
        <div class="stat-box">
            <div class="stat-number"><%= totalLength %></div>
            <div class="stat-label">Utenti Totali</div>
        </div>
        <div class="stat-box">
            <div class="stat-number"><%= totalPages %></div>
            <div class="stat-label">Pagine</div>
        </div>
        <div class="stat-box">
            <div class="stat-number"><%= currentPage + 1 %></div>
            <div class="stat-label">Pagina Corrente</div>
        </div>
        <div class="clear"></div>
        
        <% if (utenti.isEmpty()) { %>
            <div class="welcome">
                <p>Nessun utente trovato.</p>
            </div>
        <% } else { %>
            
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Email</th>
                        <th>Tipo</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Utente utente : utenti) { %>
                    <tr>
                        <td><strong>#<%= utente.getId() %></strong></td>
                        <td><%= utente.getNome() %></td>
                        <td><%= utente.getEmail() %></td>
                        <td>
                            <% if ("admin@calcioshop.it".equals(utente.getEmail())) { %>
                                <span class="status success">ADMIN</span>
                            <% } else { %>
                                <span class="status primary">CLIENTE</span>
                            <% } %>
                        </td>
                        <td>
                            <a href="dettaglio-utente.jsp?id=<%= utente.getId() %>" class="btn">Dettagli</a>
                            <a href="ordini-cliente.jsp?clienteId=<%= utente.getId() %>" class="btn">Ordini</a>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            
            <!-- Paginazione -->
            <% if (totalPages > 1) { %>
                <div class="welcome">
                    <h3>Paginazione</h3>
                    <p>
                        <!-- Prima pagina -->
                        <% if (currentPage > 0) { %>
                            <a href="utenti.jsp?page=0" class="btn">Prima</a>
                        <% } %>
                        
                        <!-- Pagina precedente -->
                        <% if (currentPage > 0) { %>
                            <a href="utenti.jsp?page=<%= currentPage - 1 %>" class="btn">← Precedente</a>
                        <% } %>
                        
                        <!-- Info pagina corrente -->
                        <strong>Pagina <%= currentPage + 1 %> di <%= totalPages %></strong>
                        
                        <!-- Pagina successiva -->
                        <% if (currentPage < totalPages - 1) { %>
                            <a href="utenti.jsp?page=<%= currentPage + 1 %>" class="btn">Successiva →</a>
                        <% } %>
                        
                        <!-- Ultima pagina -->
                        <% if (currentPage < totalPages - 1) { %>
                            <a href="utenti.jsp?page=<%= totalPages - 1 %>" class="btn">Ultima</a>
                        <% } %>
                    </p>
                </div>
            <% } %>
            
            <p><strong>Utenti visualizzati:</strong> <%= utenti.size() %> di <%= totalLength %></p>
            
        <% } %>
        
        <div class="card">
            <h3>Azioni Rapide</h3>
            <p>Gestisci gli utenti del sistema</p>
            <a href="utenti-attivi.jsp">Visualizza Solo Utenti Attivi</a>
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
