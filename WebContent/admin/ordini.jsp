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
    
    // Caricamento ordini
    List<Ordine> ordini = new ArrayList<>();
    String errore = null;
    
    try {
        OrdineDao ordineDao = new OrdineDao();
        ordini = ordineDao.getAllOrdini();
    } catch (Exception e) {
        errore = "Errore nel caricamento degli ordini: " + e.getMessage();
    }
    
    // Formattazione date e prezzi
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    DecimalFormat df = new DecimalFormat("#,##0.00");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gestione Ordini - Admin TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
    <div class="header">
        <h1>Gestione Ordini</h1>
        <a href="dashboard.jsp" class="dashboard-btn">📦 Torna alla Dashboard</a>
        <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-btn">🚪 Logout</a>
        <div class="clear"></div>
    </div>
    
    <div class="container">
        <div class="welcome">
            <h2>Tutti gli Ordini Ricevuti</h2>
            <p>Visualizza e gestisci tutti gli ordini del sistema.</p>
        </div>
        
        <% if (errore != null) { %>
            <div class="error-message">
                <%= errore %>
            </div>
        <% } %>
        
        <% if (ordini.isEmpty()) { %>
            <div class="welcome">
                <p>Nessun ordine trovato nel sistema.</p>
            </div>
        <% } else { %>
            
            <table>
                <thead>
                    <tr>
                        <th>ID Ordine</th>
                        <th>ID Utente</th>
                        <th>Data Ordine</th>
                        <th>Totale</th>
                        <th>Stato</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Ordine ordine : ordini) { %>
                    <tr>
                        <td><strong>#<%= ordine.getId() %></strong></td>
                        <td><%= ordine.getIdUtente() %></td>
                        <td><%= sdf.format(ordine.getDataOrdine()) %></td>
                        <td><%= df.format(ordine.getTotale()) %> €</td>
                        <td>
                            <% 
                                String stato = ordine.getStato();
                                String colorClass = "";
                                if ("COMPLETATO".equals(stato)) colorClass = "success";
                                else if ("ANNULLATO".equals(stato)) colorClass = "danger";
                                else colorClass = "primary";
                            %>
                            <span class="status <%= colorClass %>"><%= stato %></span>
                        </td>
                        <td>
                            <a href="dettaglio-ordine.jsp?id=<%= ordine.getId() %>" class="btn">Dettagli</a>
                            <% if (!"ANNULLATO".equals(stato)) { %>
                                <a href="${pageContext.request.contextPath}/admin/ordini?action=aggiorna&id=<%= ordine.getId() %>" class="btn btn-success">Aggiorna</a>
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            
            <div class="clear"></div>
            <p><strong>Totale ordini:</strong> <%= ordini.size() %></p>
        <% } %>
        
        <div class="card">
            <h3>Filtra Ordini</h3>
            <p>Cerca ordini specifici per data o cliente</p>
            <a href="ordini-data.jsp">Ordini per Data</a>
            <a href="ordini-cliente.jsp">Ordini per Cliente</a>
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
