<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*, model.*" %>
<%
    // Controllo accesso admin
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
    if (isAdmin == null || !isAdmin) {
        response.sendRedirect("../Login.jsp");
        return;
    }
    
    // Parametri
    String utenteIdParam = request.getParameter("id");
    if (utenteIdParam == null) {
        response.sendRedirect("utenti.jsp");
        return;
    }
    
    int utenteId = 0;
    Utente utente = null;
    List<Ordine> ordiniUtente = new ArrayList<>();
    List<Indirizzo> indirizzi = new ArrayList<>();
    String errore = null;
    
    // Statistiche
    int totaleOrdini = 0;
    int ordiniCompletati = 0;
    double totaleSpeso = 0;
    Date ultimoAccesso = null;
    
    try {
        utenteId = Integer.parseInt(utenteIdParam);
        
        // Carica utente
        UtenteDao utenteDao = new UtenteDao();
        utente = utenteDao.cercaUtenteById(utenteId);
        
        if (utente != null) {
            // Carica ordini dell'utente
            OrdineDao ordineDao = new OrdineDao();
            ordiniUtente = ordineDao.getOrdiniByUtente(utenteId);
            
            // Carica indirizzi dell'utente
            try {
                IndirizzoDao indirizzoDao = new IndirizzoDao();
                indirizzi = indirizzoDao.findByUserId(utenteId);
            } catch (Exception e) {
                // Indirizzi potrebbero non esistere, lascia lista vuota
                System.err.println("Errore caricamento indirizzi: " + e.getMessage());
            }
            
            // Calcola statistiche
            totaleOrdini = ordiniUtente.size();
            for (Ordine ordine : ordiniUtente) {
                String stato = ordine.getStato();
                if (!"ANNULLATO".equals(stato)) {
                    totaleSpeso += ordine.getTotale();
                }
                if ("CONSEGNATO".equals(stato)) {
                    ordiniCompletati++;
                }
            }
        }
        
    } catch (Exception e) {
        errore = "Errore nel caricamento dell'utente: " + e.getMessage();
        e.printStackTrace();
    }
    
    // Formattazione
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    DecimalFormat df = new DecimalFormat("#,##0.00");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dettaglio Utente #<%= utenteId %> - Admin TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/dettaglio-utente.css">
</head>
<body>
    <div class="header">
        <h1>Dettaglio Utente #<%= utenteId %></h1>
        <a href="utenti.jsp" class="back-btn">← Torna agli Utenti</a>
        <a href="dashboard.jsp" class="dashboard-btn">📦 Dashboard</a>
        <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-btn">🚪 Logout</a>
        <div class="clear"></div>
    </div>
    
    <div class="container">
        <% if (errore != null) { %>
            <div class="error-message">
                <%= errore %>
            </div>
            <a href="utenti.jsp" class="btn">← Torna agli Utenti</a>
        <% } else if (utente == null) { %>
            <div class="error-message">
                Utente non trovato.
            </div>
            <a href="utenti.jsp" class="btn">← Torna agli Utenti</a>
        <% } else { %>
            
            <!-- STATISTICHE RAPIDE -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-number"><%= totaleOrdini %></div>
                    <div class="stat-label">Ordini Totali</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number"><%= ordiniCompletati %></div>
                    <div class="stat-label">Ordini Completati</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number">€ <%= df.format(totaleSpeso) %></div>
                    <div class="stat-label">Totale Speso</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number"><%= indirizzi.size() %></div>
                    <div class="stat-label">Indirizzi Salvati</div>
                </div>
            </div>
            
            <!-- INFORMAZIONI PERSONALI -->
            <div class="info-card">
                <h2>👤 Informazioni Personali</h2>
                <div class="info-grid">
                    <div class="info-item">
                        <strong>ID Utente:</strong> #<%= utente.getId() %>
                    </div>
                    <div class="info-item">
                        <strong>Nome Completo:</strong> <%= utente.getNome() %>
                    </div>
                    <div class="info-item">
                        <strong>Email:</strong> <%= utente.getEmail() %>
                    </div>
                    <div class="info-item">
                        <strong>Status:</strong> Utente Attivo
                    </div>
                </div>
            </div>
            
            <!-- INDIRIZZI -->
            <div class="info-card">
                <h2>📍 Indirizzi di Spedizione</h2>
                <% if (indirizzi.isEmpty()) { %>
                    <p>Nessun indirizzo salvato dall'utente.</p>
                <% } else { %>
                    <div class="indirizzi-grid">
                        <% for (Indirizzo indirizzo : indirizzi) { %>
                        <div class="indirizzo-card">
                            <div class="indirizzo-info">
                                <strong>Indirizzo #<%= indirizzo.getId() %></strong><br>
                                <% if (indirizzo.getVia() != null && !indirizzo.getVia().isEmpty()) { %>
                                    <%= indirizzo.getVia() %><br>
                                <% } %>
                                <% if (indirizzo.getCitta() != null && !indirizzo.getCitta().isEmpty()) { %>
                                    <%= indirizzo.getCitta() %>
                                    <% if (indirizzo.getCap() != null && !indirizzo.getCap().isEmpty()) { %>
                                        , <%= indirizzo.getCap() %>
                                    <% } %>
                                    <br>
                                <% } %>
                                <% if (indirizzo.getProvincia() != null && !indirizzo.getProvincia().isEmpty()) { %>
                                    <small>Provincia: <%= indirizzo.getProvincia() %></small><br>
                                <% } %>
                                <% if (indirizzo.getPaese() != null && !indirizzo.getPaese().isEmpty()) { %>
                                    <small>Paese: <%= indirizzo.getPaese() %></small>
                                <% } %>
                            </div>
                        </div>
                        <% } %>
                    </div>
                <% } %>
            </div>
            
            <!-- CRONOLOGIA ORDINI -->
            <div class="info-card">
                <h2>📋 Cronologia Ordini</h2>
                <% if (ordiniUtente.isEmpty()) { %>
                    <p>Nessun ordine effettuato.</p>
                <% } else { %>
                    <table class="ordini-table">
                        <thead>
                            <tr>
                                <th>ID Ordine</th>
                                <th>Data</th>
                                <th>Totale</th>
                                <th>Stato</th>
                                <th>Azioni</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Ordine ordine : ordiniUtente) { %>
                            <tr>
                                <td><strong>#<%= ordine.getId() %></strong></td>
                                <td>
                                    <% if (ordine.getDataOrdine() != null) { %>
                                        <%= sdf.format(ordine.getDataOrdine()) %>
                                    <% } else { %>
                                        N/A
                                    <% } %>
                                </td>
                                <td>€ <%= df.format(ordine.getTotale()) %></td>
                                <td>
                                    <% 
                                        String stato = ordine.getStato();
                                        String colorClass = "";
                                        String iconaStato = "";
                                        
                                        switch(stato) {
                                            case "PAGATO":
                                                colorClass = "warning";
                                                iconaStato = "💳";
                                                break;
                                            case "IN_LAVORAZIONE":
                                                colorClass = "primary";
                                                iconaStato = "⚙️";
                                                break;
                                            case "SPEDITO":
                                                colorClass = "info";
                                                iconaStato = "🚚";
                                                break;
                                            case "CONSEGNATO":
                                                colorClass = "success";
                                                iconaStato = "✅";
                                                break;
                                            case "ANNULLATO":
                                                colorClass = "danger";
                                                iconaStato = "❌";
                                                break;
                                            default:
                                                colorClass = "primary";
                                                iconaStato = "🔄";
                                        }
                                    %>
                                    <span class="status <%= colorClass %>"><%= iconaStato %> <%= stato %></span>
                                </td>
                                <td>
                                    <a href="dettaglio-ordine.jsp?id=<%= ordine.getId() %>" class="btn-small">Dettagli</a>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } %>
            </div>
            
            <!-- AZIONI ADMIN -->
            <div class="info-card">
                <h2>🔧 Azioni Amministrative</h2>
                <div class="action-buttons">
                    <a href="utenti.jsp" class="btn">← Torna a Tutti gli Utenti</a>
                    <a href="ordini-cliente.jsp?clienteId=<%= utente.getId() %>" class="btn btn-info">Tutti gli Ordini</a>
                    <a href="utenti-attivi.jsp" class="btn btn-secondary">Utenti Attivi</a>
                    <a href="dashboard.jsp" class="btn btn-success">📦 Dashboard</a>
                </div>
            </div>
            
        <% } %>
    </div>
</body>
</html>
