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
    
    // Caricamento prodotti più venduti
    List<Map<String, Object>> prodottiPopolari = new ArrayList<>();
    String errore = null;
    
    try {
        // Query per trovare i prodotti più venduti
        String sql = "SELECT p.id, p.name, p.price, COUNT(oi.product_id) as vendite_totali, " +
                     "SUM(oi.quantity * oi.price) as ricavo_totale " +
                     "FROM products p " +
                     "LEFT JOIN order_items oi ON p.id = oi.product_id " +
                     "LEFT JOIN orders o ON oi.order_id = o.id " +
                     "WHERE o.stato != 'ANNULLATO' OR o.stato IS NULL " +
                     "GROUP BY p.id, p.name, p.price " +
                     "ORDER BY vendite_totali DESC " +
                     "LIMIT 20";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> prodotto = new HashMap<>();
                prodotto.put("id", rs.getInt("id"));
                prodotto.put("nome", rs.getString("name"));
                prodotto.put("prezzo", rs.getDouble("price"));
                prodotto.put("vendite", rs.getInt("vendite_totali"));
                prodotto.put("ricavo", rs.getDouble("ricavo_totale"));
                prodottiPopolari.add(prodotto);
            }
        }
        
    } catch (Exception e) {
        errore = "Errore nel caricamento dei prodotti: " + e.getMessage();
        e.printStackTrace();
    }
    
    // Formattazione
    DecimalFormat df = new DecimalFormat("#,##0.00");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Prodotti Più Venduti - Admin TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
    <div class="header">
        <h1>Prodotti Più Venduti</h1>
        <a href="dashboard.jsp" class="dashboard-btn-green">📦 Dashboard</a>
        <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-btn">🚪 Logout</a>
        <div class="clear"></div>
    </div>
    
    <div class="container">
        <div class="welcome">
            <h2>Top Prodotti per Vendite</h2>
            <p>Classifica dei prodotti ordinati per numero di vendite totali.</p>
        </div>
        
        <% if (errore != null) { %>
            <div class="error-message">
                <%= errore %>
            </div>
        <% } %>
        
        <% if (prodottiPopolari.isEmpty()) { %>
            <div class="welcome">
                <p>Nessuna vendita registrata o prodotti non trovati.</p>
            </div>
        <% } else { %>
            
            <!-- Statistiche top -->
            <%
                double ricavoTotale = 0.0;
                int venditeTotali = 0;
                for (Map<String, Object> p : prodottiPopolari) {
                    ricavoTotale += (Double) p.get("ricavo");
                    venditeTotali += (Integer) p.get("vendite");
                }
                
                Map<String, Object> topProdotto = prodottiPopolari.get(0);
            %>
            
            <div class="stat-box">
                <div class="stat-number"><%= venditeTotali %></div>
                <div class="stat-label">Vendite Totali</div>
            </div>
            <div class="stat-box">
                <div class="stat-number"><%= df.format(ricavoTotale) %> €</div>
                <div class="stat-label">Ricavo Totale</div>
            </div>
            <div class="stat-box">
                <div class="stat-number"><%= topProdotto.get("vendite") %></div>
                <div class="stat-label">Best Seller</div>
            </div>
            <div class="clear"></div>
            
            <div class="welcome">
                <h3>🏆 Prodotto Più Venduto</h3>
                <p><strong><%= topProdotto.get("nome") %></strong></p>
                <p>Vendite: <strong><%= topProdotto.get("vendite") %></strong> | 
                   Ricavo: <strong><%= df.format((Double)topProdotto.get("ricavo")) %> €</strong></p>
            </div>
            
            <table>
                <thead>
                    <tr>
                        <th>Posizione</th>
                        <th>Prodotto</th>
                        <th>Prezzo Unitario</th>
                        <th>Vendite</th>
                        <th>Ricavo Totale</th>
                        <th>% su Totale</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        int posizione = 1;
                        for (Map<String, Object> prodotto : prodottiPopolari) { 
                            int vendite = (Integer) prodotto.get("vendite");
                            double ricavo = (Double) prodotto.get("ricavo");
                            double percentuale = ricavoTotale > 0 ? (ricavo / ricavoTotale) * 100 : 0;
                    %>
                    <tr>
                        <td>
                            <strong>#<%= posizione %></strong>
                            <% if (posizione == 1) { %>🥇<% } else if (posizione == 2) { %>🥈<% } else if (posizione == 3) { %>🥉<% } %>
                        </td>
                        <td>
                            <strong><%= prodotto.get("nome") %></strong><br>
                            <small>ID: <%= prodotto.get("id") %></small>
                        </td>
                        <td><%= df.format((Double)prodotto.get("prezzo")) %> €</td>
                        <td>
                            <span class="status <% if (vendite > 10) { %>success<% } else if (vendite > 5) { %>primary<% } else { %>danger<% } %>">
                                <%= vendite %>
                            </span>
                        </td>
                        <td><%= df.format(ricavo) %> €</td>
                        <td><%= String.format("%.1f", percentuale) %>%</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/catalogo.jsp" class="btn">Vedi Catalogo</a>
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
            <h3>Prodotti Meno Venduti</h3>
            
            <%
                // Mostra gli ultimi 5 prodotti (meno venduti)
                int start;
                if (prodottiPopolari.size() >= 5) {
                    start = prodottiPopolari.size() - 5;
                } else {
                    start = 0;
                }
                
                List<Map<String, Object>> menoVenduti = new ArrayList<>();
                if (!prodottiPopolari.isEmpty()) {
                    for (int i = prodottiPopolari.size() - 1; i >= start; i--) {
                        menoVenduti.add(prodottiPopolari.get(i));
                    }
                }
            %>
            
            <% if (!menoVenduti.isEmpty()) { %>
                <table>
                    <tr><th>Prodotto</th><th>Vendite</th><th>Ricavo</th></tr>
                    <% for (Map<String, Object> prodotto : menoVenduti) { %>
                    <tr>
                        <td><%= prodotto.get("nome") %></td>
                        <td><%= prodotto.get("vendite") %></td>
                        <td><%= df.format((Double)prodotto.get("ricavo")) %> €</td>
                    </tr>
                    <% } %>
                </table>
            <% } %>
        </div>
        <div class="clear"></div>
        
        <div class="card">
            <h3>Report Correlati</h3>
            <p>Approfondisci l'analisi con altri report</p>
            <a href="clienti-top.jsp">Clienti Top Spender</a>
            <a href="ordini.jsp">Visualizza Ordini</a>
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
