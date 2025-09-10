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
    
    // Parametri ricerca
    String clienteId = request.getParameter("clienteId");
    String clienteEmail = request.getParameter("clienteEmail");
    
    // Liste
    List<Utente> tuttiClienti = new ArrayList<>();
    List<Ordine> ordiniCliente = new ArrayList<>();
    Utente clienteSelezionato = null;
    String errore = null;
    boolean ricercaEseguita = false;
    
    try {
        UtenteDao utenteDao = new UtenteDao();
        tuttiClienti = utenteDao.listaUtenti();
        
        // Ricerca per ID cliente
        if (clienteId != null && !clienteId.isEmpty()) {
            ricercaEseguita = true;
            int id = Integer.parseInt(clienteId);
            
            OrdineDao ordineDao = new OrdineDao();
            ordiniCliente = ordineDao.getOrdiniByUtente(id);
            clienteSelezionato = utenteDao.cercaUtenteById(id);
        }
        
        // Ricerca per email cliente
        if (clienteEmail != null && !clienteEmail.isEmpty()) {
            ricercaEseguita = true;
            clienteSelezionato = utenteDao.cercaUtenteByEmail(clienteEmail);
            
            if (clienteSelezionato != null) {
                OrdineDao ordineDao = new OrdineDao();
                ordiniCliente = ordineDao.getOrdiniByUtente(clienteSelezionato.getId());
            }
        }
        
    } catch (Exception e) {
        errore = "Errore nella ricerca: " + e.getMessage();
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
    <title>Ordini per Cliente - Admin TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
    <div class="header">
        <div class="header-buttons">
            <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-btn">🚪 Logout</a>
            <a href="ordini.jsp" class="all-orders-btn">📋 Tutti gli Ordini</a>
            <a href="dashboard.jsp" class="dashboard-btn-green">📦 Dashboard</a>
        </div>
        <h1>Ordini per Cliente</h1>
        <div class="clear"></div>
    </div>
    
    <div class="container">
        <div class="welcome">
            <h2>Cerca Ordini per Cliente</h2>
            <p>Visualizza tutti gli ordini di un cliente specifico.</p>
        </div>
        
        <div class="card">
            <h3>Cerca per ID Cliente</h3>
            <form method="get">
                <div class="form-group">
                    <label for="clienteId">ID Cliente:</label>
                    <input type="number" id="clienteId" name="clienteId" min="1" 
                           placeholder="Es: 123"
                           value="<%= clienteId != null ? clienteId : "" %>">
                </div>
                <button type="submit" class="btn btn-success">Cerca per ID</button>
            </form>
        </div>
        
        <div class="card">
            <h3>Cerca per Email Cliente</h3>
            <form method="get">
                <div class="form-group">
                    <label for="clienteEmail">Email Cliente:</label>
                    <input type="email" id="clienteEmail" name="clienteEmail" 
                           placeholder="cliente@example.com"
                           value="<%= clienteEmail != null ? clienteEmail : "" %>">
                </div>
                <button type="submit" class="btn btn-success">Cerca per Email</button>
            </form>
        </div>
        <div class="clear"></div>
        
        <% if (ricercaEseguita) { %>
            <a href="ordini-cliente.jsp" class="btn">Reset Ricerca</a>
        <% } %>
        
        <% if (errore != null) { %>
            <div class="error-message">
                <%= errore %>
            </div>
        <% } %>
        
        <% if (ricercaEseguita) { %>
            
            <% if (clienteSelezionato == null) { %>
                <div class="error-message">
                    Cliente non trovato.
                </div>
            <% } else { %>
                
                <div class="welcome">
                    <h3>Ordini del Cliente</h3>
                    <p><strong>Nome:</strong> <%= clienteSelezionato.getNome() %></p>
                    <p><strong>Email:</strong> <%= clienteSelezionato.getEmail() %></p>
                    <p><strong>ID:</strong> #<%= clienteSelezionato.getId() %></p>
                </div>
                
                <% if (ordiniCliente.isEmpty()) { %>
                    <div class="error-message">
                        Questo cliente non ha ancora effettuato ordini.
                    </div>
                <% } else { %>
                    
                    <!-- Statistiche cliente -->
                    <% 
                        double totaleSpeso = 0;
                        int ordiniCompletati = 0;
                        for (Ordine o : ordiniCliente) {
                            String s = o.getStato();
                            if (!"ANNULLATO".equals(s)) {
                                totaleSpeso += o.getTotale();
                            }
                            if ("CONSEGNATO".equals(s)) {
                                ordiniCompletati++;
                            }
                        }
                    %>
                    
                    <div class="stat-box">
                        <div class="stat-number"><%= ordiniCliente.size() %></div>
                        <div class="stat-label">Ordini Totali</div>
                    </div>
                    <div class="stat-box">
                        <div class="stat-number"><%= ordiniCompletati %></div>
                        <div class="stat-label">Ordini Consegnati</div>
                    </div>
                    <div class="stat-box">
                        <div class="stat-number"><%= df.format(totaleSpeso) %> €</div>
                        <div class="stat-label">Totale Speso</div>
                    </div>
                    <div class="clear"></div>
                    
                    <!-- Sezione informativa statistiche -->
                    <div class="info-statistiche">
                        <h4>📊 Spiegazione Statistiche</h4>
                        
                        <div class="info-grid">
                            <div class="info-item">
                                <span class="info-icon">📋</span>
                                <div class="info-content">
                                    <strong>Ordini Totali</strong>
                                    <p>Tutti gli ordini effettuati dal cliente, indipendentemente dallo stato</p>
                                </div>
                            </div>
                            
                            <div class="info-item">
                                <span class="info-icon">✅</span>
                                <div class="info-content">
                                    <strong>Ordini Consegnati</strong>
                                    <p>Solo gli ordini con stato "CONSEGNATO" (completamente evasi e ricevuti dal cliente)</p>
                                </div>
                            </div>
                            
                            <div class="info-item">
                                <span class="info-icon">💰</span>
                                <div class="info-content">
                                    <strong>Totale Speso</strong>
                                    <p>Somma dell'importo di tutti gli ordini validi (esclusi solo quelli annullati)</p>
                                </div>
                            </div>
                        </div>
                        
                        <div class="flusso-stati">
                            <strong>Flusso degli stati:</strong>
                            <div class="stati-flow">
                                <span class="stato-step">💳 Pagato</span>
                                <span class="arrow">→</span>
                                <span class="stato-step">⚙️ In Lavorazione</span>
                                <span class="arrow">→</span>
                                <span class="stato-step">🚚 Spedito</span>
                                <span class="arrow">→</span>
                                <span class="stato-step">✅ Consegnato</span>
                            </div>
                        </div>
                    </div>
                    
                    <table>
                        <thead>
                            <tr>
                                <th>ID Ordine</th>
                                <th>Data Ordine</th>
                                <th>Totale</th>
                                <th>Stato</th>
                                <th>Azioni</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Ordine ordine : ordiniCliente) { %>
                            <tr>
                                <td><strong>#<%= ordine.getId() %></strong></td>
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
                                <td>
                                    <a href="dettaglio-ordine.jsp?id=<%= ordine.getId() %>" class="btn">Dettagli</a>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } %>
            <% } %>
        <% } else { %>
            
            <div class="welcome">
                <h3>Tutti i Clienti Registrati</h3>
                <p>Lista dei primi 20 clienti. Usa la ricerca sopra per trovare un cliente specifico.</p>
            </div>
            
            <% if (!tuttiClienti.isEmpty()) { %>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Email</th>
                            <th>Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            int count = 0;
                            for (Utente cliente : tuttiClienti) { 
                                if (count >= 20) break; // Limita a 20 per performance
                                count++;
                        %>
                        <tr>
                            <td>#<%= cliente.getId() %></td>
                            <td><%= cliente.getNome() %></td>
                            <td><%= cliente.getEmail() %></td>
                            <td>
                                <a href="ordini-cliente.jsp?clienteId=<%= cliente.getId() %>" class="btn">Vedi Ordini</a>
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
        .status.warning { background: #fff3cd; color: #856404; }
        .status.info { background: #d1ecf1; color: #0c5460; }
        
        /* Sezione informativa statistiche */
        .info-statistiche {
            background-color: #f8f9fa;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            padding: 20px;
            margin: 20px 0;
        }
        
        .info-statistiche h4 {
            color: #495057;
            margin: 0 0 15px 0;
            font-size: 16px;
            text-align: center;
        }
        
        .info-grid {
            display: flex;
            gap: 15px;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }
        
        .info-item {
            flex: 1;
            min-width: 200px;
            display: flex;
            align-items: flex-start;
            gap: 10px;
            padding: 12px;
            background-color: white;
            border-radius: 6px;
            border: 1px solid #dee2e6;
        }
        
        .info-icon {
            font-size: 20px;
            flex-shrink: 0;
            margin-top: 2px;
        }
        
        .info-content {
            flex: 1;
        }
        
        .info-content strong {
            display: block;
            color: #343a40;
            font-size: 14px;
            margin-bottom: 4px;
        }
        
        .info-content p {
            margin: 0;
            color: #6c757d;
            font-size: 13px;
            line-height: 1.4;
        }
        
        .flusso-stati {
            background-color: white;
            padding: 15px;
            border-radius: 6px;
            border: 1px solid #dee2e6;
            text-align: center;
        }
        
        .flusso-stati strong {
            display: block;
            color: #495057;
            margin-bottom: 10px;
            font-size: 14px;
        }
        
        .stati-flow {
            display: flex;
            align-items: center;
            justify-content: center;
            flex-wrap: wrap;
            gap: 8px;
        }
        
        .stato-step {
            background-color: #e9ecef;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 500;
            color: #495057;
            white-space: nowrap;
        }
        
        .arrow {
            color: #6c757d;
            font-weight: bold;
            margin: 0 5px;
        }
        
        /* Responsive per schermi piccoli */
        @media (max-width: 768px) {
            .info-grid {
                flex-direction: column;
            }
            
            .stati-flow {
                flex-direction: column;
                gap: 5px;
            }
            
            .arrow {
                transform: rotate(90deg);
                margin: 5px 0;
            }
        }
    </style>
</body>
</html>
