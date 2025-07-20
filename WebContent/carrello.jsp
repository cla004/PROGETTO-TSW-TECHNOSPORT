<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Carrello" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Il tuo Carrello - TecnoSport</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/Homepage.css">
    <style>
        .carrello-container {
            max-width: 1200px;
            margin: 50px auto;
            padding: 20px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        .carrello-item {
            display: flex;
            align-items: center;
            padding: 20px;
            border-bottom: 1px solid #eee;
            gap: 20px;
        }
        .carrello-item:last-child {
            border-bottom: none;
        }
        .item-image {
            width: 100px;
            height: 100px;
            object-fit: cover;
            border-radius: 8px;
        }
        .item-info {
            flex: 1;
        }
        .item-name {
            font-size: 1.2em;
            font-weight: bold;
            margin-bottom: 5px;
        }
        .item-price {
            color: #e74c3c;
            font-size: 1.1em;
            font-weight: bold;
        }
        .quantity-controls {
            display: flex;
            align-items: center;
            gap: 15px;
        }
        .btn-quantity {
            width: 40px;
            height: 40px;
            border: none;
            background: #3498db;
            color: white;
            font-size: 1.2em;
            font-weight: bold;
            border-radius: 50%;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .btn-quantity:hover {
            background: #2980b9;
        }
        .btn-remove {
            background: #e74c3c;
            color: white;
            border: none;
            padding: 8px 15px;
            border-radius: 5px;
            cursor: pointer;
        }
        .btn-remove:hover {
            background: #c0392b;
        }
        .quantity {
            font-size: 1.2em;
            font-weight: bold;
            min-width: 30px;
            text-align: center;
        }
        .totale-container {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-top: 20px;
        }
        .totale {
            font-size: 1.5em;
            font-weight: bold;
            color: #2c3e50;
            text-align: right;
        }
        .empty-cart {
            text-align: center;
            padding: 50px;
            color: #666;
        }
        .messaggio {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            font-weight: bold;
        }
        .successo {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .errore {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .btn-continua {
            background: #28a745;
            color: white;
            padding: 15px 30px;
            border: none;
            border-radius: 5px;
            font-size: 1.1em;
            cursor: pointer;
            margin-top: 20px;
        }
        .btn-continua:hover {
            background: #218838;
        }
        .btn-svuota {
            background: #dc3545;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            float: right;
        }
        .btn-svuota:hover {
            background: #c82333;
        }
    </style>
</head>
<body>

<header>
    <div class="logo">TecnoSport</div>
    <nav>
        <ul>
            <li><a href="Homepage.jsp">🏠 Homepage</a></li>
            <li><a href="carrello">🛒 Carrello</a></li>
        </ul>
    </nav>
</header>

<div class="carrello-container">
    <h1>🛒 Il tuo Carrello</h1>
    
    <!-- Messaggi di successo/errore -->
    <% String successo = (String) session.getAttribute("successo");
       String errore = request.getParameter("errore");
       if (successo != null) { %>
        <div class="messaggio successo"><%= successo %></div>
        <% session.removeAttribute("successo"); %>
    <% } %>
    <% if (errore != null) { %>
        <div class="messaggio errore"><%= errore %></div>
    <% } %>
    
    <%
        List<Carrello> prodottiCarrello = (List<Carrello>) session.getAttribute("carrelloSessione");
        Double totale = 0.0;
        Integer numeroArticoli = 0;
        
        // Calcola totale e numero articoli
        if (prodottiCarrello != null) {
            for (Carrello item : prodottiCarrello) {
                totale += item.getProdotto().getPrezzo() * item.getQuantita();
                numeroArticoli += item.getQuantita();
            }
        }
        
        if (prodottiCarrello == null || prodottiCarrello.isEmpty()) {
    %>
        <div class="empty-cart">
            <h2>Il tuo carrello è vuoto</h2>
            <p>Aggiungi alcuni prodotti dalla nostra homepage!</p>
            <a href="Homepage.jsp" class="btn-continua">Continua lo shopping</a>
        </div>
    <%
        } else {
    %>
        
        <!-- Pulsante svuota carrello -->
        <form action="carrello" method="post" style="display: inline;">
            <input type="hidden" name="action" value="svuota">
            <button type="submit" class="btn-svuota">
                🗑️ Svuota carrello
            </button>
        </form>
        
        <div style="clear: both; margin-bottom: 20px;"></div>
        
        <!-- Lista prodotti nel carrello -->
        <% for (Carrello item : prodottiCarrello) { %>
            <div class="carrello-item">
                <img src="images/prodotto.jpg" alt="<%= item.getProdotto().getNome() %>" class="item-image">
                
                <div class="item-info">
                    <div class="item-name"><%= item.getProdotto().getNome() %></div>
                    <div class="item-price">€ <%= item.getProdotto().getPrezzo() %></div>
                    <div>Descrizione: <%= item.getProdotto().getDescrizione() %></div>
                </div>
                
                <div class="quantity-controls">
                    <!-- Pulsante - (rimuovi 1) -->
                    <form action="carrello" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="rimuovi">
                        <input type="hidden" name="prodottoId" value="<%= item.getProdotto().getId_prodotto() %>">
                        <button type="submit" class="btn-quantity">-</button>
                    </form>
                    
                    <span class="quantity"><%= item.getQuantita() %></span>
                    
                    <!-- Pulsante + (aggiungi 1) -->
                    <form action="carrello" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="aggiungi">
                        <input type="hidden" name="prodottoId" value="<%= item.getProdotto().getId_prodotto() %>">
                        <button type="submit" class="btn-quantity">+</button>
                    </form>
                    
                    <!-- Pulsante elimina completamente -->
                    <form action="carrello" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="elimina">
                        <input type="hidden" name="prodottoId" value="<%= item.getProdotto().getId_prodotto() %>">
                        <button type="submit" class="btn-remove">
                            ❌ Elimina
                        </button>
                    </form>
                </div>
            </div>
        <% } %>
        
        <!-- Totale -->
        <div class="totale-container">
            <div>Articoli nel carrello: <strong><%= numeroArticoli %></strong></div>
            <div class="totale">Totale: € <%= totale %></div>
        </div>
        
        <a href="Homepage.jsp" class="btn-continua">Continua lo shopping</a>
    
    <% } %>
</div>

</body>
</html>
