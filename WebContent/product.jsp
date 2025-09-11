<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Prodotti" %>
<%@ page import="model.Taglia" %>
<%@ page import="model.ProdottiDao" %>
<%@ page import="model.TagliaDao" %>
<%@ page import="model.Prodotto_taglia" %>
<%@ page import="model.ProdottoTagliaDao" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<%
    // Recupero l'ID del prodotto dai parametri URL
    String prodottoIdParam = request.getParameter("id");
    
    if (prodottoIdParam == null || prodottoIdParam.trim().isEmpty()) {
        response.sendRedirect(request.getContextPath() + "/Homepage.jsp");
        return;
    }
    
    int prodottoId;
    try {
        prodottoId = Integer.parseInt(prodottoIdParam);
    } catch (NumberFormatException e) {
        response.sendRedirect(request.getContextPath() + "/Homepage.jsp");
        return;
    }
    
    // Carico il prodotto dal database
    ProdottiDao prodottiDao = new ProdottiDao();
    Prodotti prodotto = prodottiDao.cercaProdottoById(prodottoId);
    
    if (prodotto == null) {
        response.sendRedirect(request.getContextPath() + "/Homepage.jsp");
        return;
    }
    
    // Carico le taglie disponibili per questo prodotto
    ProdottoTagliaDao ptDao = new ProdottoTagliaDao();
    TagliaDao tagliaDao = new TagliaDao();
    List<Prodotto_taglia> taglieDisponibili = ptDao.getTaglieDisponibiliPerProdotto(prodottoId);
    
    // Calcolo lo stock totale
    int stockTotale = 0;
    Map<Integer, Integer> stockPerTaglia = new HashMap<>();
    List<Taglia> taglie = new java.util.ArrayList<>();
    
    if (taglieDisponibili != null && !taglieDisponibili.isEmpty()) {
        for (Prodotto_taglia pt : taglieDisponibili) {
            Taglia taglia = tagliaDao.cercaTagliaById(pt.getid_taglia());
            if (taglia != null) {
                taglie.add(taglia);
                int stock = (int) pt.getQuantita_disponibili();
                stockPerTaglia.put(taglia.getid_taglia(), stock);
                stockTotale += stock;
            }
        }
    } else {
        // Se non ci sono taglie, il prodotto non è disponibile
        stockTotale = 0;
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= prodotto.getNome() %> - TecnoSport</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/product.css">
</head>
<body>
    <div class="container">
        <button type="button" class="back-button" onclick="location.href='<%= request.getContextPath() %>/Homepage.jsp'">← Torna alla Homepage</button>
        
        <div class="header">
            <h1>TecnoSport 🏃‍♂️</h1>
        </div>
        
        <div class="product-info">
            <div class="product-image">
                <% if (prodotto.getImmagine() != null && !prodotto.getImmagine().trim().isEmpty()) { %>
                    <img src="<%= request.getContextPath() + "/" + prodotto.getImmagine() %>" 
                         alt="<%= prodotto.getNome() %>" 
                         onerror="this.src='<%= request.getContextPath() %>/images/default-product.jpg'">
                <% } else { %>
                    <img src="<%= request.getContextPath() %>/images/default-product.jpg" 
                         alt="<%= prodotto.getNome() %>">
                <% } %>
            </div>
            
            <h2><%= prodotto.getNome() %></h2>
            
            <div class="price">€ <%= String.format("%.2f", prodotto.getPrezzo()) %></div>
            
            <div class="description">
                <% if (prodotto.getDescrizione() != null) { %>
                    <%= prodotto.getDescrizione() %>
                <% } else { %>
                    Nessuna descrizione disponibile.
                <% } %>
            </div>
            
            <div class="stock-info">
                <strong>Stock totale disponibile: </strong>
                <% if (stockTotale > 10) { %>
                    <span class="stock-available"><%= stockTotale %> disponibili ✅</span>
                <% } else if (stockTotale > 0) { %>
                    <span class="stock-low">Solo <%= stockTotale %> rimasti! ⚠️</span>
                <% } else { %>
                    <span class="stock-out">Prodotto esaurito ❌</span>
                <% } %>
            </div>
        </div>
        
        <% if (stockTotale > 0) { %>
        <div class="add-to-cart-section">
            <h3>Aggiungi al Carrello</h3>
            
            <form action="<%= request.getContextPath() %>/carrello" method="post" id="addToCartForm">
                <input type="hidden" name="action" value="aggiungi">
                <input type="hidden" name="prodottoId" value="<%= prodotto.getId_prodotto() %>">
                
                <% if (taglie != null && !taglie.isEmpty()) { %>
                    <div class="form-group">
                        <label>Seleziona Taglia:</label>
                        <div class="size-selector">
                            <% for (Taglia taglia : taglie) { 
                                Integer stockTagliaObj = stockPerTaglia.get(taglia.getid_taglia());
                                int stockTaglia;
                                if (stockTagliaObj != null) {
                                    stockTaglia = stockTagliaObj;
                                } else {
                                    stockTaglia = 0;
                                }
                            %>
                                <% if (stockTaglia == 0) { %>
                                <div class="size-button disabled" 
                                     data-size-id="<%= taglia.getid_taglia() %>"
                                     data-stock="<%= stockTaglia %>">
                                    <strong><%= taglia.getEtichetta() %></strong>
                                    <div class="size-stock-info">
                                        Esaurita
                                    </div>
                                </div>
                                <% } else { %>
                                <div class="size-button" 
                                     data-size-id="<%= taglia.getid_taglia() %>"
                                     data-stock="<%= stockTaglia %>"
                                     onclick="selectSize(this)">
                                    <strong><%= taglia.getEtichetta() %></strong>
                                    <div class="size-stock-info">
                                        Stock: <%= stockTaglia %>
                                    </div>
                                </div>
                                <% } %>
                            <% } %>
                        </div>
                        <input type="hidden" name="tagliaId" id="selectedSizeId" value="">
                    </div>
                <% } %>
                
                <div class="form-group">
                    <label>Quantità:</label>
                    <div class="quantity-selector">
                        <button type="button" class="quantity-btn" onclick="decreaseQuantity()">-</button>
                        <input type="number" name="quantita" id="quantityInput" value="1" min="1" max="<%= stockTotale %>" class="quantity-input" readonly>
                        <button type="button" class="quantity-btn" onclick="increaseQuantity()">+</button>
                    </div>
                </div>
                
                <% if (taglie != null && !taglie.isEmpty()) { %>
                <button type="submit" class="add-to-cart-btn" id="addToCartBtn" disabled>
                    🛒 Aggiungi al Carrello
                </button>
                <% } else { %>
                <button type="submit" class="add-to-cart-btn" id="addToCartBtn">
                    🛒 Aggiungi al Carrello
                </button>
                <% } %>
            </form>
        </div>
        <% } %>
    </div>
    
    <script>
        // Imposta variabili globali per il JavaScript
        <% if (taglie != null && !taglie.isEmpty()) { %>
        hasSizes = true;
        <% } else { %>
        hasSizes = false;
        <% } %>
        maxQuantity = <%= stockTotale %>;
    </script>
    <script src="<%= request.getContextPath() %>/Script/product.js"></script>
</body>
</html>
