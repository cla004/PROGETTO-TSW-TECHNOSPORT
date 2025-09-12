// JavaScript per distribuzione dinamica delle taglie
// Calcola in tempo reale quanto stock rimane da distribuire

function updateStockDistribution() {
    // Prende lo stock totale inserito dall'admin
    var stockTotaleInput = document.getElementById('stock');
    var stockTotale = parseInt(stockTotaleInput.value) || 0;
    
    // Calcola la somma di tutte le quantità delle taglie attive
    var totaleSommaTaglie = 0;
    var checkboxTaglie = document.getElementsByClassName('taglia-checkbox');
    
    for (var i = 0; i < checkboxTaglie.length; i++) {
        var checkbox = checkboxTaglie[i];
        if (checkbox.checked) {
            var tagliaId = checkbox.value;
            var inputQuantita = document.getElementById('quantita_' + tagliaId);
            var quantita = parseInt(inputQuantita.value) || 0;
            totaleSommaTaglie += quantita;
        }
    }
    
    // Calcola quanto stock rimane da distribuire
    var stockRimasto = stockTotale - totaleSommaTaglie;
    
    // Aggiorna il display
    var stockTotaleDisplay = document.getElementById('stock-totale-display');
    var stockDistribuitoDisplay = document.getElementById('stock-distribuito-display');
    var stockRimastoDisplay = document.getElementById('stock-rimasto-display');
    var stockRimastoLine = document.getElementById('stock-rimasto-line');
    
    if (stockTotaleDisplay) stockTotaleDisplay.innerHTML = stockTotale;
    if (stockDistribuitoDisplay) stockDistribuitoDisplay.innerHTML = totaleSommaTaglie;
    if (stockRimastoDisplay) stockRimastoDisplay.innerHTML = stockRimasto;
    
    // Cambia colore in base alla situazione
    if (stockRimastoLine) {
        if (stockRimasto < 0) {
            stockRimastoLine.className = 'stock-error';
        } else if (stockRimasto === 0) {
            stockRimastoLine.className = 'stock-perfect';
        } else {
            stockRimastoLine.className = 'stock-available';
        }
    }
    
    // Aggiorna i massimi consentiti per ogni taglia
    for (var i = 0; i < checkboxTaglie.length; i++) {
        var checkbox = checkboxTaglie[i];
        if (checkbox.checked) {
            var tagliaId = checkbox.value;
            var inputQuantita = document.getElementById('quantita_' + tagliaId);
            var quantitaAttuale = parseInt(inputQuantita.value) || 0;
            
            // Il massimo è: stock rimasto + quantità già assegnata a questa taglia
            var massimo = stockRimasto + quantitaAttuale;
            if (massimo < 0) massimo = 0;
            
            inputQuantita.max = massimo;
            
            // Aggiorna il testo "Max: XX"
            var maxLabel = document.getElementById('max_' + tagliaId);
            if (maxLabel) {
                maxLabel.innerHTML = '(Max: ' + massimo + ')';
            }
        }
    }
    
    // Mostra/nasconde messaggio di errore
    var erroreMessaggio = document.getElementById('errore-distribuzione');
    if (stockRimasto < 0) {
        if (erroreMessaggio) {
            erroreMessaggio.innerHTML = '⚠️ ATTENZIONE: Hai distribuito ' + Math.abs(stockRimasto) + ' unità in più dello stock disponibile!';
            erroreMessaggio.style.display = 'block';
        }
        return false; // Validazione fallita
    } else {
        if (erroreMessaggio) {
            erroreMessaggio.style.display = 'none';
        }
        return true; // Validazione OK
    }
}

// Abilita/disabilita campo quantità quando checkbox viene selezionato
function toggleTagliaQuantita(checkbox) {
    var tagliaId = checkbox.value;
    var inputQuantita = document.getElementById('quantita_' + tagliaId);
    
    if (checkbox.checked) {
        // Abilita il campo quantità
        inputQuantita.disabled = false;
        inputQuantita.style.backgroundColor = '';
        
        // Se è vuoto, mette 0 come default
        if (!inputQuantita.value) {
            inputQuantita.value = 0;
        }
    } else {
        // Disabilita il campo e azzera
        inputQuantita.disabled = true;
        inputQuantita.value = '';
        inputQuantita.style.backgroundColor = '#f5f5f5';
    }
    
    // Aggiorna i calcoli
    updateStockDistribution();
}

// Validazione prima del submit del form
function validateForm() {
    // Controlla che almeno una taglia sia selezionata
    var checkboxTaglie = document.getElementsByClassName('taglia-checkbox');
    var almenoUnaTagliaSelezionata = false;
    
    for (var i = 0; i < checkboxTaglie.length; i++) {
        if (checkboxTaglie[i].checked) {
            almenoUnaTagliaSelezionata = true;
            break;
        }
    }
    
    if (!almenoUnaTagliaSelezionata) {
        alert('Seleziona almeno una taglia per questo prodotto!');
        return false;
    }
    
    // Controlla che la distribuzione sia valida
    if (!updateStockDistribution()) {
        alert('La somma delle quantità delle taglie supera lo stock totale. Correggi i valori prima di continuare.');
        return false;
    }
    
    return true;
}

// Inizializzazione quando la pagina si carica
window.onload = function() {
    // Campo stock totale
    var stockInput = document.getElementById('stock');
    if (stockInput) {
        stockInput.addEventListener('input', updateStockDistribution);
        stockInput.addEventListener('change', updateStockDistribution);
    }
    
    // Checkbox delle taglie
    var checkboxTaglie = document.getElementsByClassName('taglia-checkbox');
    for (var i = 0; i < checkboxTaglie.length; i++) {
        var checkbox = checkboxTaglie[i];
        checkbox.addEventListener('change', function() {
            toggleTagliaQuantita(this);
        });
    }
    
    // Campi quantità delle taglie
    var inputsQuantita = document.querySelectorAll('input[id^="quantita_"]');
    for (var i = 0; i < inputsQuantita.length; i++) {
        inputsQuantita[i].addEventListener('input', updateStockDistribution);
        inputsQuantita[i].addEventListener('change', updateStockDistribution);
        
        // Inizialmente disabilita tutti i campi quantità
        inputsQuantita[i].disabled = true;
        inputsQuantita[i].style.backgroundColor = '#f5f5f5';
    }
    
    // Form validation
    var form = document.querySelector('form');
    if (form) {
        form.addEventListener('submit', function(e) {
            if (!validateForm()) {
                e.preventDefault();
                return false;
            }
        });
    }
    
    // Calcolo iniziale
    updateStockDistribution();
};
