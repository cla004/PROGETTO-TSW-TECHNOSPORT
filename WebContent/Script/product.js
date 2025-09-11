// JavaScript basico per pagina prodotto

var selectedSize = null;
var maxQuantity = 0;
var hasSizes = false;

// Funzione per mostrare messaggi senza alert
function showMessage(message, type) {
    // Rimuovi eventuali messaggi esistenti
    var existingMessage = document.getElementById('message-display');
    if (existingMessage) {
        existingMessage.remove();
    }
    
    // Crea nuovo messaggio
    var messageDiv = document.createElement('div');
    messageDiv.id = 'message-display';
    messageDiv.textContent = message;
    
    // Stile del messaggio
    messageDiv.style.padding = '10px';
    messageDiv.style.margin = '10px 0';
    messageDiv.style.borderRadius = '5px';
    messageDiv.style.fontSize = '14px';
    
    if (type === 'error') {
        messageDiv.style.backgroundColor = '#ffebee';
        messageDiv.style.color = '#c62828';
        messageDiv.style.border = '1px solid #e57373';
    } else {
        messageDiv.style.backgroundColor = '#e8f5e8';
        messageDiv.style.color = '#2e7d32';
        messageDiv.style.border = '1px solid #81c784';
    }
    
    // Inserisci il messaggio prima del form
    var form = document.getElementById('addToCartForm');
    if (form && form.parentNode) {
        form.parentNode.insertBefore(messageDiv, form);
    }
    
    // Rimuovi il messaggio dopo 5 secondi
    setTimeout(function() {
        if (messageDiv && messageDiv.parentNode) {
            messageDiv.parentNode.removeChild(messageDiv);
        }
    }, 5000);
}

function selectSize(element) {
    // Rimuovi selezione precedente
    var buttons = document.querySelectorAll('.size-button');
    for (var i = 0; i < buttons.length; i++) {
        buttons[i].classList.remove('selected');
    }
    
    // Seleziona la nuova taglia
    element.classList.add('selected');
    selectedSize = element.getAttribute('data-size-id');
    maxQuantity = parseInt(element.getAttribute('data-stock'));
    
    // Aggiorna campo hidden
    document.getElementById('selectedSizeId').value = selectedSize;
    
    // Aggiorna quantità massima
    var quantityInput = document.getElementById('quantityInput');
    quantityInput.setAttribute('max', maxQuantity);
    
    // Se la quantità corrente è maggiore del massimo disponibile, riducila
    if (parseInt(quantityInput.value) > maxQuantity) {
        quantityInput.value = maxQuantity;
    }
    
    // Abilita il pulsante aggiungi al carrello
    document.getElementById('addToCartBtn').disabled = false;
}

function increaseQuantity() {
    var quantityInput = document.getElementById('quantityInput');
    var currentQuantity = parseInt(quantityInput.value);
    var currentMax;
    
    if (hasSizes && selectedSize) {
        currentMax = maxQuantity;
    } else {
        currentMax = parseInt(quantityInput.getAttribute('max'));
    }
    
    if (currentQuantity < currentMax) {
        quantityInput.value = currentQuantity + 1;
    }
}

function decreaseQuantity() {
    var quantityInput = document.getElementById('quantityInput');
    var currentQuantity = parseInt(quantityInput.value);
    
    if (currentQuantity > 1) {
        quantityInput.value = currentQuantity - 1;
    }
}

function validateForm() {
    if (hasSizes && !selectedSize) {
        showMessage('Per favore seleziona una taglia prima di aggiungere al carrello.', 'error');
        return false;
    }
    
    var quantity = parseInt(document.getElementById('quantityInput').value);
    if (quantity < 1) {
        showMessage('La quantità deve essere almeno 1.', 'error');
        return false;
    }
    
    var currentMax;
    if (hasSizes && selectedSize) {
        currentMax = maxQuantity;
    } else {
        currentMax = parseInt(document.getElementById('quantityInput').getAttribute('max'));
    }
    
    if (quantity > currentMax) {
        showMessage('Quantità non disponibile in stock.', 'error');
        return false;
    }
    
    return true;
}

// Inizializzazione quando la pagina è caricata
window.onload = function() {
    var form = document.getElementById('addToCartForm');
    if (form) {
        form.onsubmit = function() {
            return validateForm();
        };
    }
};
