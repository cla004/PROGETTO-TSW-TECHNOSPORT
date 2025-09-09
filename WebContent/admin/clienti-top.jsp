<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*, java.sql.*, model.*" %>
<%
    // Controllo accesso admin
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
    if (isAdmin == null || !isAdmin) {
        response.sendRedirect("../Login.jsp");
        return;
    }
    
    Utente admin = (Utente) session.getAttribute("loggedInUser");
    
    // Caricamento clienti top spender
    List<Map<String, Object>> clientiTop = new ArrayList<>();
    String errore = null;
    
    try {
        // Query per trovare i clienti che hanno speso di più
        String sql = "SELECT u.id, u.name, u.email, " +
                     "COUNT(o.id) as numero_ordini, " +
                     "SUM(o.totale) as totale_speso, " +
                     "AVG(o.totale) as spesa_media, " +
                     "MAX(o.data_ordine) as ultimo_ordine " +
                     "FROM users u " +
                     "INNER JOIN orders o ON u.id = o.id_utente " +
                     "WHERE o.stato != 'ANNULLATO' AND u.email != 'admin@calcioshop.it' " +
                     "GROUP BY u.id, u.name, u.email " +
                     "ORDER BY totale_speso DESC " +
                     "LIMIT 20";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> cliente = new HashMap<>();
                cliente.put("id", rs.getInt("id"));
                cliente.put("nome", rs.getString("name"));
                cliente.put("email", rs.getString("email"));
                cliente.put("numeroOrdini", rs.getInt("numero_ordini"));
                cliente.put("totaleSpeso", rs.getDouble("totale_speso"));
                cliente.put("spesaMedia", rs.getDouble("spesa_media"));
                cliente.put("ultimoOrdine", rs.getTimestamp("ultimo_ordine"));
                clientiTop.add(cliente);
            }
        }
        
    } catch (Exception e) {
        errore = "Errore nel caricamento dei clienti: " + e.getMessage();
        e.printStackTrace();
    }
    
    // Formattazione
    DecimalFormat df = new DecimalFormat("#,##0.00");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Clienti Top Spender - Admin TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
    <div class="header">
        <h1>Clienti Top Spender</h1>
        <a href="dashboard.jsp" class="dashboard-btn-green">📦 Dashboard</a>
        <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-btn">🚪 Logout</a>
        <div class="clear"></div>
    </div>
    
    <div class="container">
        <div class="welcome">
            <h2>Migliori Clienti per Valore</h2>
            <p>Classifica dei clienti ordinati per totale speso in ordini completati.</p>
        </div>
        
        <% if (errore != null) { %>
            <div class="error-message">
                <%= errore %>
            </div>
        <% } %>
        
        <% if (clientiTop.isEmpty()) { %>
            <div class="welcome">
                <p>Nessun cliente con ordini completati trovato.</p>
            </div>
        <% } else { %>
            
            <!-- Statistiche generali -->
            <%
                double spesaTotaleClienti = 0.0;
                int ordiniTotaliClienti = 0;
                for (Map<String, Object> c : clientiTop) {
                    spesaTotaleClienti += (Double) c.get("totaleSpeso");
                    ordiniTotaliClienti += (Integer) c.get("numeroOrdini");
                }
                
                Map<String, Object> topCliente = clientiTop.get(0);
                double spesaMediaGenerale = spesaTotaleClienti / clientiTop.size();
            %>
            
            <div class="stat-box">
                <div class="stat-number"><%= clientiTop.size() %></div>
                <div class="stat-label">Clienti Attivi</div>
            </div>
            <div class="stat-box">
                <div class="stat-number"><%= df.format(spesaTotaleClienti) %> €</div>
                <div class="stat-label">Spesa Totale</div>
            </div>
            <div class="stat-box">
                <div class="stat-number"><%= df.format(spesaMediaGenerale) %> €</div>
                <div class="stat-label">Spesa Media per Cliente</div>
            </div>
            <div class="clear"></div>
            
            <div class="welcome">
                <h3>👑 Cliente Top</h3>
                <p><strong><%= topCliente.get("nome") %></strong> (<%= topCliente.get("email") %>)</p>
                <p>Spesa Totale: <strong><%= df.format((Double)topCliente.get("totaleSpeso")) %> €</strong> | 
                   Ordini: <strong><%= topCliente.get("numeroOrdini") %></strong> |
                   Spesa Media: <strong><%= df.format((Double)topCliente.get("spesaMedia")) %> €</strong></p>
            </div>
            
            <table>
                <thead>
                    <tr>
                        <th>Posizione</th>
                        <th>Cliente</th>
                        <th>Email</th>
                        <th>Ordini</th>
                        <th>Spesa Totale</th>
                        <th>Spesa Media</th>
                        <th>Ultimo Ordine</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        int posizione = 1;
                        for (Map<String, Object> cliente : clientiTop) { 
                            double totaleSpeso = (Double) cliente.get("totaleSpeso");
                            int numeroOrdini = (Integer) cliente.get("numeroOrdini");
                            double spesaMedia = (Double) cliente.get("spesaMedia");
                            java.sql.Timestamp ultimoOrdine = (java.sql.Timestamp) cliente.get("ultimoOrdine");
                    %>
                    <tr>
                        <td>
                            <strong>#<%= posizione %></strong>
                            <% if (posizione == 1) { %>👑<% } else if (posizione == 2) { %>🥈<% } else if (posizione == 3) { %>🥉<% } %>
                        </td>
                        <td>
                            <strong><%= cliente.get("nome") %></strong><br>
                            <small>ID: <%= cliente.get("id") %></small>
                        </td>
                        <td><%= cliente.get("email") %></td>
                        <td>
                            <span class="status <% if (numeroOrdini >= 5) { %>success<% } else if (numeroOrdini >= 2) { %>primary<% } else { %>danger<% } %>">
                                <%= numeroOrdini %>
                            </span>
                        </td>
                        <td><strong><%= df.format(totaleSpeso) %> €</strong></td>
                        <td><%= df.format(spesaMedia) %> €</td>
                        <td><%= ultimoOrdine != null ? sdf.format(ultimoOrdine) : "N/A" %></td>
                        <td>
                            <a href="ordini-cliente.jsp?clienteId=<%= cliente.get("id") %>" class="btn">Vedi Ordini</a>
                        </td>
                    </tr>
                    <% 
                        posizione++;
                        } 
                    %>
                </tbody>
            </table>
            
        <% } %>
        
        <div class="card">
            <h3>Segmentazione Clienti</h3>
            <p>Analisi della distribuzione dei clienti per fasce di spesa</p>
            
            <%
                // Analizza la distribuzione per fasce di spesa
                int clientiPremium = 0;   // > 500€
                int clientiGold = 0;      // 200-500€
                int clientiSilver = 0;    // 100-200€
                int clientiBronze = 0;    // < 100€
                
                for (Map<String, Object> cliente : clientiTop) {
                    double spesa = (Double) cliente.get("totaleSpeso");
                    if (spesa > 500) clientiPremium++;
                    else if (spesa >= 200) clientiGold++;
                    else if (spesa >= 100) clientiSilver++;
                    else clientiBronze++;
                }
            %>
            
            <table>
                <tr><th>Fascia</th><th>Range Spesa</th><th>Numero Clienti</th><th>Percentuale</th></tr>
                <tr>
                    <td><strong>👑 Premium</strong></td>
                    <td>> 500 €</td>
                    <td><%= clientiPremium %></td>
                    <td><%= clientiTop.size() > 0 ? String.format("%.1f", (double)clientiPremium / clientiTop.size() * 100) : "0" %>%</td>
                </tr>
                <tr>
                    <td><strong>🥇 Gold</strong></td>
                    <td>200 - 500 €</td>
                    <td><%= clientiGold %></td>
                    <td><%= clientiTop.size() > 0 ? String.format("%.1f", (double)clientiGold / clientiTop.size() * 100) : "0" %>%</td>
                </tr>
                <tr>
                    <td><strong>🥈 Silver</strong></td>
                    <td>100 - 200 €</td>
                    <td><%= clientiSilver %></td>
                    <td><%= clientiTop.size() > 0 ? String.format("%.1f", (double)clientiSilver / clientiTop.size() * 100) : "0" %>%</td>
                </tr>
                <tr>
                    <td><strong>🥉 Bronze</strong></td>
                    <td>< 100 €</td>
                    <td><%= clientiBronze %></td>
                    <td><%= clientiTop.size() > 0 ? String.format("%.1f", (double)clientiBronze / clientiTop.size() * 100) : "0" %>%</td>
                </tr>
            </table>
        </div>
        <div class="clear"></div>
        
        <div class="card">
            <h3>Report Correlati</h3>
            <p>Approfondisci l'analisi con altri report</p>
            <a href="prodotti-popolari.jsp">Prodotti Più Venduti</a>
            <a href="utenti-attivi.jsp">Tutti gli Utenti</a>
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
