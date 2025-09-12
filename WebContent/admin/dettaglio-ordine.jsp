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
    String ordineIdParam = request.getParameter("id");
    if (ordineIdParam == null) {
        response.sendRedirect("ordini.jsp");
        return;
    }
    
    int ordineId = 0;
    Ordine ordine = null;
    List<DettaglioOrdine> dettagli = new ArrayList<>();
    Utente cliente = null;
    String errore = null;
    
    try {
        ordineId = Integer.parseInt(ordineIdParam);
        
        // Carica ordine
        OrdineDao ordineDao = new OrdineDao();
        ordine = ordineDao.getOrdineById(ordineId);
        
        if (ordine != null) {
            // Carica dettagli ordine con prodotti
            DettaglioOrdineDao dettaglioDao = new DettaglioOrdineDao();
            dettagli = dettaglioDao.findByOrderIdCompleto(ordineId);
            
            // Carica info cliente
            UtenteDao utenteDao = new UtenteDao();
            cliente = utenteDao.cercaUtenteById(ordine.getIdUtente());
        }
        
    } catch (Exception e) {
        errore = "Errore nel caricamento dell'ordine: " + e.getMessage();
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
    <title>Dettaglio Ordine #<%= ordineId %> - Admin TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/dettaglio-ordine.css">
</head>
<body>
    <div class="header">
        <h1>Dettaglio Ordine #<%= ordineId %></h1>
        <a href="ordini.jsp" class="back-btn">← Torna agli Ordini</a>
        <a href="dashboard.jsp" class="dashboard-btn">📦 Dashboard</a>
        <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-btn">🚪 Logout</a>
        <div class="clear"></div>
    </div>
    
    <div class="container">
        <% if (errore != null) { %>
            <div class="error-message">
                <%= errore %>
            </div>
            <a href="ordini.jsp" class="btn">← Torna agli Ordini</a>
        <% } else if (ordine == null) { %>
            <div class="error-message">
                Ordine non trovato.
            </div>
            <a href="ordini.jsp" class="btn">← Torna agli Ordini</a>
        <% } else { %>
            
            <!-- INFO ORDINE -->
            <div class="info-card">
                <h2>📋 Informazioni Ordine</h2>
                <div class="info-grid">
                    <div class="info-item">
                        <strong>ID Ordine:</strong> #<%= ordine.getId() %>
                    </div>
                    <div class="info-item">
                        <strong>Data Ordine:</strong> 
                        <% if (ordine.getDataOrdine() != null) { %>
                            <%= sdf.format(ordine.getDataOrdine()) %>
                        <% } else { %>
                            N/A
                        <% } %>
                    </div>
                    <div class="info-item">
                        <strong>Stato:</strong> 
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
                    </div>
                    <div class="info-item">
                        <strong>Totale Ordine:</strong> 
                        <span class="price-highlight">€ <%= df.format(ordine.getTotale()) %></span>
                    </div>
                </div>
            </div>
            
            <!-- INFO CLIENTE -->
            <% if (cliente != null) { %>
            <div class="info-card">
                <h2>👤 Informazioni Cliente</h2>
                <div class="info-grid">
                    <div class="info-item">
                        <strong>ID Cliente:</strong> #<%= cliente.getId() %>
                    </div>
                    <div class="info-item">
                        <strong>Nome:</strong> <%= cliente.getNome() %>
                    </div>
                    <div class="info-item">
                        <strong>Email:</strong> <%= cliente.getEmail() %>
                    </div>
                    <div class="info-item">
                        <strong>Azioni:</strong>
                        <a href="ordini-cliente.jsp?clienteId=<%= cliente.getId() %>" class="btn-small">Altri Ordini</a>
                        <a href="dettaglio-utente.jsp?id=<%= cliente.getId() %>" class="btn-small">Profilo Cliente</a>
                    </div>
                </div>
            </div>
            <% } %>
            
            <!-- PRODOTTI ORDINATI -->
            <div class="info-card">
                <h2>🛍️ Prodotti Ordinati</h2>
                <% if (dettagli.isEmpty()) { %>
                    <p>Nessun dettaglio prodotto trovato.</p>
                <% } else { %>
                    <table class="prodotti-table">
                        <thead>
                            <tr>
                                <th>Prodotto</th>
                                <th>Quantità</th>
                                <th>Prezzo Unitario</th>
                                <th>Totale Riga</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                                double totaleCalcolato = 0;
                                for (DettaglioOrdine dettaglio : dettagli) { 
                                    double totaleRiga = dettaglio.getTotaleRiga();
                                    totaleCalcolato += totaleRiga;
                            %>
                            <tr>
                                <td class="prodotto-nome">
                                    <strong><%= dettaglio.getNomeProdotto() %></strong>
                                    <% if (dettaglio.getProdotto() == null) { %>
                                        <br><small class="prodotto-cancellato">⚠️ Prodotto non più disponibile</small>
                                    <% } %>
                                </td>
                                <td class="quantita"><%= dettaglio.getQuantity() %></td>
                                <td class="prezzo">€ <%= df.format(dettaglio.getPrezzoUnitario()) %></td>
                                <td class="prezzo totale-riga">€ <%= df.format(totaleRiga) %></td>
                            </tr>
                            <% } %>
                        </tbody>
                        <tfoot>
                            <tr class="totale-finale">
                                <td colspan="3"><strong>TOTALE ORDINE:</strong></td>
                                <td class="prezzo"><strong>€ <%= df.format(ordine.getTotale()) %></strong></td>
                            </tr>
                        </tfoot>
                    </table>
                <% } %>
            </div>
            
            <!-- AZIONI ADMIN -->
            <div class="info-card">
                <h2>🔧 Azioni Amministrative</h2>
                <div class="action-buttons">
                    <a href="ordini.jsp" class="btn">← Torna a Tutti gli Ordini</a>
                    <% if (cliente != null) { %>
                        <a href="ordini-cliente.jsp?clienteId=<%= cliente.getId() %>" class="btn btn-info">Altri Ordini del Cliente</a>
                    <% } %>
                    <a href="dashboard.jsp" class="btn btn-success">📦 Dashboard</a>
                </div>
            </div>
            
        <% } %>
    </div>
</body>
</html>
