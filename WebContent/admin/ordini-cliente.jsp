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
                            if ("COMPLETATO".equals(o.getStato())) {
                                totaleSpeso += o.getTotale();
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
                        <div class="stat-label">Ordini Completati</div>
                    </div>
                    <div class="stat-box">
                        <div class="stat-number"><%= df.format(totaleSpeso) %> €</div>
                        <div class="stat-label">Totale Speso</div>
                    </div>
                    <div class="clear"></div>
                    
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
    </style>
</body>
</html>
