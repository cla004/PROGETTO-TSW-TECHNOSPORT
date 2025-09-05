// FUNZIONE: Gestisce la selezione della taglia per un prodotto
// PARAMETRI:
// - prodottoId: ID del prodotto per cui stiamo selezionando la taglia
// - tagliaId: ID della taglia selezionata (es: 1=S, 2=M, 3=L, 4=XL)
// - button: il bottone HTML che è stato cliccato
function selezionaTaglia(prodottoId, tagliaId, button) {
    
    // PASSO 1: Cerco tutti i bottoni delle taglie per questo specifico prodotto
    // Uso querySelectorAll per trovare tutti gli elementi con classe 'taglia-btn' 
    // che hanno attributo data-prodotto uguale al prodottoId corrente
    var buttons = document.querySelectorAll('.taglia-btn[data-prodotto="' + prodottoId + '"]');
    
    // PASSO 2: Rimuovo la classe 'selected' da tutti i bottoni di questo prodotto
    // Questo fa sì che solo una taglia alla volta possa essere selezionata
    for (var i = 0; i < buttons.length; i++) {
        buttons[i].classList.remove('selected');
    }
    
    // PASSO 3: Aggiungo la classe 'selected' al bottone che è stato cliccato
    // Questo fa cambiare il colore del bottone per mostrare che è selezionato
    button.classList.add('selected');
    
    // PASSO 4: Salvo l'ID della taglia in un campo nascosto del form
    // Questo campo verrà inviato al server quando l'utente clicca "Aggiungi al carrello"
    document.getElementById('taglia-' + prodottoId).value = tagliaId;
    
    // PASSO 5: Attivo il bottone "Aggiungi al carrello" per questo prodotto
    // Prima della selezione della taglia il bottone era disabilitato
    document.getElementById('add-btn-' + prodottoId).disabled = false;
}

// FUNZIONE: Aumenta la quantità di un prodotto di 1
// PARAMETRI:
// - prodottoId: ID del prodotto per cui aumentare la quantità
function aumentaQuantita(prodottoId) {
    
    // PASSO 1: Trovo il campo input della quantità per questo prodotto
    var input = document.getElementById('quantita-' + prodottoId);
    
    // PASSO 2: Prendo il valore attuale, lo converto in numero e aggiungo 1
    // parseInt() converte la stringa in numero intero
    input.value = parseInt(input.value) + 1;
}

// FUNZIONE: Diminuisce la quantità di un prodotto di 1 (ma non scende mai sotto 1)
// PARAMETRI:
// - prodottoId: ID del prodotto per cui diminuire la quantità
function diminuisciQuantita(prodottoId) {
    
    // PASSO 1: Trovo il campo input della quantità per questo prodotto
    var input = document.getElementById('quantita-' + prodottoId);
    
    // PASSO 2: Controllo che il valore sia maggiore di 1 prima di diminuire
    // Non permettiamo mai di andare sotto 1 pezzo
    if (parseInt(input.value) > 1) {
        
        // PASSO 3: Se maggiore di 1, diminuisco di 1
        input.value = parseInt(input.value) - 1;
    }
    // Se il valore è 1 o meno, non faccio niente (rimane 1)
}

// FUNZIONE: Controlla che l'utente abbia selezionato una taglia prima di inviare il form
// PARAMETRI:
// - prodottoId: ID del prodotto da controllare
// RITORNA: true se tutto ok, false se manca la taglia (blocca l'invio del form)
function controllaForm(prodottoId) {
    
    // PASSO 1: Prendo il valore del campo nascosto che contiene l'ID della taglia
    var taglia = document.getElementById('taglia-' + prodottoId).value;
    
    // PASSO 2: Controllo se il campo è vuoto (nessuna taglia selezionata)
    if (!taglia) {
        
        // PASSO 3: Creo un messaggio di errore elegante nella pagina
        var errorDiv = document.getElementById('error-' + prodottoId);
        if (!errorDiv) {
            // Se non esiste, creo il div per il messaggio di errore
            errorDiv = document.createElement('div');
            errorDiv.id = 'error-' + prodottoId;
            errorDiv.style.color = 'red';
            errorDiv.style.fontSize = '14px';
            errorDiv.style.marginTop = '5px';
            errorDiv.style.fontWeight = 'bold';
            
            // Inserisco il div dopo il selettore delle taglie
            var tagliaSelector = document.querySelector('#taglia-selector-' + prodottoId);
            if (tagliaSelector) {
                tagliaSelector.appendChild(errorDiv);
            }
        }
        
        // PASSO 4: Mostro il messaggio di errore
        errorDiv.textContent = '⚠️ Seleziona una taglia prima di continuare!';
        
        // PASSO 5: Ritorno false per bloccare l'invio del form
        return false;
    } else {
        
        // PASSO 6: Se la taglia è selezionata, rimuovo eventuali messaggi di errore
        var errorDiv = document.getElementById('error-' + prodottoId);
        if (errorDiv) {
            errorDiv.textContent = '';
        }
    }
    
    // PASSO 5: Se la taglia è stata selezionata, permetto l'invio del form
    return true;
}
