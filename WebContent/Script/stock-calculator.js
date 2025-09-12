// JavaScript semplice per calcolare lo stock in tempo reale
// Usa getElementsByClassName per essere più facile

function updateStock() {
    // Prende il valore dello stock totale
    var stockTotale = document.getElementById('stock').value;
    if (!stockTotale || stockTotale === '') {
        stockTotale = 0;
    } else {
        stockTotale = parseInt(stockTotale);
    }
    
    // Calcola quanti pezzi sono già distribuiti nelle taglie
    var stockDistribuito = 0;
    var campiTaglie = document.getElementsByClassName('taglia-quantity-input');
    
    for (var i = 0; i < campiTaglie.length; i++) {
        var quantita = campiTaglie[i].value;
        if (quantita && quantita !== '') {
            stockDistribuito = stockDistribuito + parseInt(quantita);
        }
    }
    
    // Calcola quanto stock è ancora disponibile
    var stockDisponibile = stockTotale - stockDistribuito;
    
    // Aggiorna i testi sulla pagina
    document.getElementById('stock-totale').innerHTML = stockTotale;
    document.getElementById('stock-distribuito').innerHTML = stockDistribuito;
    document.getElementById('stock-disponibile').innerHTML = stockDisponibile;
    
    // Cambia colore: rosso se negativo, verde se positivo
    var elementoDisponibile = document.getElementById('stock-disponibile-line');
    if (stockDisponibile < 0) {
        elementoDisponibile.className = 'stock-error';
    } else {
        elementoDisponibile.className = 'stock-available';
    }
    
    // Aggiorna i testi "Massimo" e range usando il tuo metodo migliorato!
    var taglieInfo = document.getElementsByClassName('taglia-info');
    var rangeSpans = document.getElementsByClassName('stock-range');
    
    for (var i = 0; i < campiTaglie.length; i++) {
        var campo = campiTaglie[i];
        var quantitaOriginale = campo.getAttribute('data-quantita-originale');
        
        if (!quantitaOriginale) {
            quantitaOriginale = 0;
        } else {
            quantitaOriginale = parseInt(quantitaOriginale);
        }
        
        // Calcola il massimo per questa taglia specifica
        var massimoConsentito = stockDisponibile + quantitaOriginale;
        if (massimoConsentito < 0) {
            massimoConsentito = 0;
        }
        
        // Aggiorna il testo "Massimo" usando OPZIONE 1: ricostruzione completa
        if (i < taglieInfo.length) {
            var small = taglieInfo[i].getElementsByTagName('small')[0];
            if (small) {
                // Prende la quantità attuale dal campo (con || 0 come fallback)
                var quantitaAttuale = campo.value || 0;
                
                // Ricostruisce tutto il testo da zero - più semplice del replace!
                small.innerHTML = 'Attuale: ' + quantitaAttuale + ' | Massimo: ' + massimoConsentito;
            }
        }
        
        // Aggiorna il range "0 - XX" (tuo metodo!)
        if (i < rangeSpans.length) {
            rangeSpans[i].innerHTML = '0 - ' + massimoConsentito;
        }
    }
}

// Aggiunge l'evento a tutti i campi quando la pagina si carica
window.onload = function() {
    // Campo stock totale
    var campoStock = document.getElementById('stock');
    if (campoStock) {
        campoStock.onchange = updateStock;
        campoStock.oninput = updateStock;
    }
    
    // Campi delle taglie usando la classe
    var campiTaglie = document.getElementsByClassName('taglia-quantity-input');
    for (var i = 0; i < campiTaglie.length; i++) {
        campiTaglie[i].onchange = updateStock;
        campiTaglie[i].oninput = updateStock;
    }
    
    // Calcola subito i valori al caricamento
    updateStock();
};
