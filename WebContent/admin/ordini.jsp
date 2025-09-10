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
        
        <!-- Messaggi di successo/errore per aggiornamenti -->
        <% String aggiornato = request.getParameter("aggiornato"); %>
        <% if ("success".equals(aggiornato)) { %>
            <div class="success-message">
                ✅ Ordine aggiornato con successo! Lo stato è stato avanzato al livello successivo.
            </div>
        <% } %>
        
        <% String erroreParam = request.getParameter("errore"); %>
        <% if ("update_failed".equals(erroreParam)) { %>
            <div class="error-message">
                ❌ Errore nell'aggiornamento dell'ordine. Riprova.
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
                        <td class="actions-cell">
                            <div class="action-buttons">
                                <a href="dettaglio-ordine.jsp?id=<%= ordine.getId() %>" class="btn btn-detail">Dettagli</a>
                                
                                <% 
                                    // Mostra controlli di aggiornamento solo se l'ordine può essere modificato
                                    boolean puoAggiornare = !"CONSEGNATO".equals(stato) && !"ANNULLATO".equals(stato);
                                    if (puoAggiornare) {
                                %>
                                    <!-- Form per aggiornamento stato -->
                                    <form method="post" action="${pageContext.request.contextPath}/admin/ordini" class="stato-form">
                                        <input type="hidden" name="action" value="aggiorna">
                                        <input type="hidden" name="id" value="<%= ordine.getId() %>">
                                        
                                        <select name="stato" class="stato-select" onchange="this.form.submit()" title="Seleziona nuovo stato">
                                            <option value="" class="placeholder-option">Cambia Stato</option>
                                            <option value="PAGATO" <%= "PAGATO".equals(stato) ? "disabled selected" : "" %>>💳 Pagato</option>
                                            <option value="IN_LAVORAZIONE" <%= "IN_LAVORAZIONE".equals(stato) ? "disabled selected" : "" %>>⚙️ In Lavorazione</option>
                                            <option value="SPEDITO" <%= "SPEDITO".equals(stato) ? "disabled selected" : "" %>>🚚 Spedito</option>
                                            <option value="CONSEGNATO" <%= "CONSEGNATO".equals(stato) ? "disabled selected" : "" %>>✅ Consegnato</option>
                                        </select>
                                    </form>
                                    
                                <% } else if ("CONSEGNATO".equals(stato)) { %>
                                    <div class="stato-finale stato-completato">✓ Ordine Completato</div>
                                <% } else if ("ANNULLATO".equals(stato)) { %>
                                    <div class="stato-finale stato-annullato">❌ Ordine Annullato</div>
                                <% } %>
                            </div>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            
            <div class="clear"></div>
            <p><strong>Totale ordini:</strong> <%= ordini.size() %></p>
        <% } %>
        
        <div class="card">
            <h3>Gestione Stati Ordini</h3>
            <p>Usa il <strong>dropdown "Cambia Stato"</strong> per impostare direttamente lo stato desiderato per ogni ordine.</p>
            
            <p><strong>Stati Disponibili:</strong><br>
                💳 <strong>PAGATO</strong> → 
                ⚙️ <strong>IN_LAVORAZIONE</strong> → 
                🚚 <strong>SPEDITO</strong> → 
                ✅ <strong>CONSEGNATO</strong>
            </p>
            
            <p><strong>Come funziona:</strong></p>
            <ul>
                <li>Seleziona lo stato dal dropdown per aggiornare immediatamente l'ordine</li>
                <li>Puoi impostare qualsiasi stato (non devi seguire una sequenza)</li>
                <li>Gli ordini <strong>CONSEGNATI</strong> e <strong>ANNULLATI</strong> non possono essere modificati</li>
            </ul>
        </div>
        
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
        .status.warning { background: #fff3cd; color: #856404; }
        .status.info { background: #d1ecf1; color: #0c5460; }
        
        /* Layout celle azioni */
        .actions-cell {
            min-width: 180px;
            vertical-align: top;
        }
        
        .action-buttons {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }
        
        /* Pulsante dettagli */
        .btn-detail {
            background-color: #17a2b8;
            color: white;
            padding: 6px 12px;
            text-decoration: none;
            border-radius: 4px;
            font-size: 13px;
            text-align: center;
            display: inline-block;
        }
        
        .btn-detail:hover {
            background-color: #138496;
            color: white;
            text-decoration: none;
        }
        
        /* Form e dropdown per stato */
        .stato-form {
            margin: 0;
        }
        
        .stato-select {
            padding: 8px 12px;
            border: 2px solid #ddd;
            border-radius: 6px;
            font-size: 13px;
            font-weight: 500;
            background-color: white;
            cursor: pointer;
            width: 160px;
            min-height: 36px;
            transition: all 0.3s ease;
            outline: none;
        }
        
        .stato-select:hover {
            border-color: #007bff;
            box-shadow: 0 2px 4px rgba(0,123,255,0.2);
        }
        
        .stato-select:focus {
            border-color: #007bff;
            box-shadow: 0 0 0 3px rgba(0,123,255,0.1);
        }
        
        .stato-select option {
            padding: 8px 12px;
            font-size: 13px;
        }
        
        .stato-select option:disabled {
            color: #666;
            font-style: italic;
            background-color: #f8f9fa;
        }
        
        .placeholder-option {
            color: #6c757d;
            font-style: italic;
        }
        
        /* Stati finali */
        .stato-finale {
            padding: 8px 12px;
            border-radius: 6px;
            font-size: 13px;
            font-weight: 500;
            text-align: center;
            width: 160px;
        }
        
        .stato-completato {
            background-color: #d4edda;
            color: #155724;
            border: 2px solid #c3e6cb;
        }
        
        .stato-annullato {
            background-color: #f8d7da;
            color: #721c24;
            border: 2px solid #f5c6cb;
        }
    </style>
</body>
</html>
