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
    
    // Parametri filtro
    String dataInizio = request.getParameter("dataInizio");
    String dataFine = request.getParameter("dataFine");
    
    // Lista ordini filtrati
    List<Ordine> ordiniFiltrati = new ArrayList<>();
    String errore = null;
    boolean ricercaEseguita = false;
    
    if (dataInizio != null && dataFine != null && !dataInizio.isEmpty() && !dataFine.isEmpty()) {
        ricercaEseguita = true;
        try {
            // TODO: Implementare filtro per date nel DAO
            // Per ora carichiamo tutti e filtriamo in memoria
            OrdineDao ordineDao = new OrdineDao();
            List<Ordine> tuttiOrdini = ordineDao.getAllOrdini();
            
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dataInizioDate = inputFormat.parse(dataInizio);
            Date dataFineDate = inputFormat.parse(dataFine);
            
            // Aggiungi 23:59:59 alla data fine per includere tutto il giorno
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataFineDate);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            dataFineDate = cal.getTime();
            
            for (Ordine ordine : tuttiOrdini) {
                Date dataOrdine = new Date(ordine.getDataOrdine().getTime());
                if (dataOrdine.compareTo(dataInizioDate) >= 0 && dataOrdine.compareTo(dataFineDate) <= 0) {
                    ordiniFiltrati.add(ordine);
                }
            }
            
        } catch (Exception e) {
            errore = "Errore nel filtro degli ordini: " + e.getMessage();
        }
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
    <title>Ordini per Data - Admin TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
    <div class="header">
        <h1>Ordini per Data</h1>
        <a href="dashboard.jsp" class="dashboard-btn-green">📦 Dashboard</a>
        <a href="ordini.jsp" class="all-orders-btn">📋 Tutti gli Ordini</a>
        <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-btn">🚪 Logout</a>
        <div class="clear"></div>
    </div>
    
    <div class="container">
        <div class="welcome">
            <h2>Filtra Ordini per Periodo</h2>
            <p>Seleziona un range di date per visualizzare gli ordini del periodo.</p>
        </div>
        
        <div class="card">
            <h3>Seleziona Periodo</h3>
            <form method="get">
                <div class="form-group">
                    <label for="dataInizio">Data Inizio:</label>
                    <input type="date" id="dataInizio" name="dataInizio" 
                           value="<%= dataInizio != null ? dataInizio : "" %>" required>
                </div>
                
                <div class="form-group">
                    <label for="dataFine">Data Fine:</label>
                    <input type="date" id="dataFine" name="dataFine" 
                           value="<%= dataFine != null ? dataFine : "" %>" required>
                </div>
                
                <button type="submit" class="btn btn-success">Cerca Ordini</button>
                <% if (ricercaEseguita) { %>
                    <a href="ordini-data.jsp" class="btn">Reset Filtri</a>
                <% } %>
            </form>
        </div>
        <div class="clear"></div>
        
        <% if (errore != null) { %>
            <div class="error-message">
                <%= errore %>
            </div>
        <% } %>
        
        <% if (ricercaEseguita) { %>
            <div class="welcome">
                <h3>Risultati Ricerca</h3>
                <p>Ordini dal <strong><%= dataInizio %></strong> al <strong><%= dataFine %></strong></p>
            </div>
            
            <% if (ordiniFiltrati.isEmpty()) { %>
                <div class="error-message">
                    Nessun ordine trovato nel periodo selezionato.
                </div>
            <% } else { %>
                
                <!-- Statistiche periodo -->
                <% 
                    double totaleVendite = 0;
                    for (Ordine o : ordiniFiltrati) {
                        if (!"ANNULLATO".equals(o.getStato())) {
                            totaleVendite += o.getTotale();
                        }
                    }
                %>
                
                <div class="stat-box">
                    <div class="stat-number"><%= ordiniFiltrati.size() %></div>
                    <div class="stat-label">Ordini nel Periodo</div>
                </div>
                <div class="stat-box">
                    <div class="stat-number"><%= df.format(totaleVendite) %> €</div>
                    <div class="stat-label">Vendite Totali</div>
                </div>
                <div class="clear"></div>
                
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
                        <% for (Ordine ordine : ordiniFiltrati) { %>
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
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %>
        <% } %>
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
