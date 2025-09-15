// ------------------------------------------------------
// Validazione/formattazione campo scadenza MM/YY
// Logica: regole "Amazon-style" + messaggi di errore
// ------------------------------------------------------

// Prendo il campo input della scadenza (deve esistere nell'HTML con id="scadenza")
var campoScadenza = document.getElementById('scadenza');

// Prendo il contenitore dove mostrare eventuali errori (deve esistere con id="errorScadenza")
var errorBox = document.getElementById('errorScadenza');

// Provo a prendere il form: quello con id formCheckout
var form = document.getElementById('formCheckout');

// Costante: quanti anni avanti è accettabile (configurabile)
var ANNI_AVANTI = 10;

// -----------------------------
// Gestore evento "input" (ogni volta che l'utente digita o incolla)
// -----------------------------
campoScadenza.addEventListener('input', function () {
    // Leggo il valore attuale del campo (testo così com'è inserito dall'utente)
    var raw = campoScadenza.value;

    // Creo una stringa vuota che conterrà solo le cifre (pulizia input)
    var numeri = "";

    // Scorro ogni carattere di `raw`
    for (var i = 0; i < raw.length; i++) {
        // Ottengo il singolo carattere alla posizione i
        var ch = raw.charAt(i);

        // Se il carattere è una cifra tra '0' e '9' lo accodo a numeri
        // (in questo modo elimino spazi, slash, lettere, simboli inseriti manualmente)
        if (ch >= '0' && ch <= '9') {
            numeri += ch;
        }
    }

    // Limito a massimo 4 cifre totali (2 per mese + 2 per anno). Se l'utente incolla di più, tronco e ignoro i numeri successivi.
    if (numeri.length > 4) {
        numeri = numeri.substring(0, 4);
    }

    // Preparo due array temporanei:
    // - meseDigits per le cifre del mese (0..2 elementi)
    // - annoDigits per le cifre dell'anno (0..2 elementi)
    var meseDigits = [];
    var annoDigits = [];

    // idx sarà l'indice in `numeri` da cui iniziare a leggere le cifre per l'anno
    var idx = 0;

    // Se non ci sono cifre, non faccio nulla
    if (numeri.length === 0) {
        // campo vuoto
    } else {
        // Primo carattere esiste: lo prendiamo
        var primo = numeri.charAt(0);

        // CASO: primo carattere '0'
        // Es: l'utente digita "0" -> aspettiamo un secondo numero per formare 0X (es 05)
        if (primo === '0') {
            // Metto '0' come prima cifra del mese
            meseDigits.push('0');

            // Se esiste un secondo carattere, lo considero secondo digit del mese
            if (numeri.length >= 2) {
                meseDigits.push(numeri.charAt(1));
                // Ho consumato due caratteri per il mese
                idx = 2;
            } else {
                // Ho consumato solo il primo carattere
                idx = 1;
            }

        // CASO: primo carattere '1'
        // Es: se il secondo è 0/1/2 -> mese 10/11/12; se il secondo è 3..9 -> vogliamo "01/X"
        } else if (primo === '1') {
            if (numeri.length >= 2) {
                var secondo = numeri.charAt(1);

                // Se secondo è '0' o '1' o '2' -> mese valido 10/11/12
                if (secondo === '0' || secondo === '1' || secondo === '2') {
                    meseDigits.push('1');
                    meseDigits.push(secondo);
                    idx = 2; // consumati due caratteri per il mese
                } else {
                    // Se secondo è > '2' (es '3'..'9'), NON vogliamo 13..19
                    // Applichiamo la regola: mese -> "01" e consideriamo il secondo come primo digit dell'anno
                    meseDigits.push('0');
                    meseDigits.push('1');
                    // sposto il secondo carattere come primo digit dell'anno
                    annoDigits.push(secondo);
                    // abbiamo consumato 2 caratteri in input numeri, quindi idx = 2
                    idx = 2;
                }
            } else {
                // C'è solo '1' digitato per ora: mostro '1' e aspetto
                meseDigits.push('1');
                idx = 1;
            }

        // CASO: primo carattere '2'..'9'
        // Es: l'utente digita '5' => vogliamo trasformare in "05/" (0 davanti + slash)
        } else {
            // Metto '0' e il digit come mese (es '5' -> '0','5')
            meseDigits.push('0');
            meseDigits.push(primo);
            // Ho consumato solo il primo carattere come input per il mese: l'anno parte dal secondo carattere
            idx = 1;
        }
    }

    // Ora riempio annoDigits con le cifre rimanenti (max 2)
    for (var j = idx; j < numeri.length; j++) {
        if (annoDigits.length < 2) {
            // aggiungo la cifra all'array annoDigits
            annoDigits.push(numeri.charAt(j));
        } else {
            // già due cifre nell'anno, ignoro il resto
            break;
        }
    }

    // Costruisco la stringa da mostrare nell'input (display)
    var display = "";

    // Se non ho ancora il mese completo, mostro quello che c'è (o niente)
    if (meseDigits.length === 0) {
        display = "";
    } else if (meseDigits.length === 1) {
        // L'utente ha digitato 1 cifra del mese (es '1' o '0'), la mostro così com'è
        display = meseDigits[0];
    } else {
        // Mese completo (due cifre), lo concateno
        var meseStr = "" + meseDigits[0] + meseDigits[1];

        // Se ci sono cifre dell'anno, mostro "MM/YY-parziale" o "MM/YY"
        if (annoDigits.length > 0) {
            // Anno parziale o completo (potrebbe essere 1 o 2 cifre)
            display = meseStr + "/" + ("" + annoDigits.join(''));
        } else {
            // Mese completo ma anno vuoto -> mostro la slash per indicare prossima parte
            display = meseStr + "/";
        }
    }

    // Aggiorno il campo con la stringa costruita
    campoScadenza.value = display;

    // Se ho mese completo e due cifre di anno (4 cifre totali), eseguo la validazione completa
    if ((meseDigits.length === 2) && (annoDigits.length === 2)) {
        // validaScadenzaCompleta imposterà errorBox se c'è errore
        if (!validaScadenzaCompleta(meseDigits.join('') , annoDigits.join(''))) {
            // se false => erroBox è già stato impostato dalla funzione
        } else {
            // se true => tutto ok, pulisco eventuali messaggi precedenti
            errorBox.innerHTML = "";
        }
    } else {
        // Se la data è ancora parziale non mostro messaggi di "scaduta" o "troppo lontana".
        // Pulisco messaggi precedenti per non confondere l'utente mentre scrive.
        errorBox.innerHTML = "";
    }
});

// -----------------------------
// Funzione che valida mese e anno COMPLETI (stringhe, 2 cifre ciascuno)
// Restituisce true se valido, false se errore (e imposta errorBox)
// -----------------------------
function validaScadenzaCompleta(meseStr, annoStr) {
    // Pulisco eventuali messaggi
    errorBox.innerHTML = "";

    // Converto le stringhe in numeri interi
    var mese = parseInt(meseStr, 10);
    var anno = parseInt(annoStr, 10);

    // Prendo anno corrente (ultime due cifre)
    var fullYear = new Date().getFullYear();            // es: 2025
    var annoCorrente = parseInt((fullYear + "").slice(-2), 10); // "2025".slice(-2) => "25" -> 25
    var meseCorrente = new Date().getMonth() + 1;       // getMonth() parte da 0 -> +1 per avere 1..12
    var annoMax = annoCorrente + ANNI_AVANTI;          // es 25 + 10 = 35 (limite superiore)

    // Controllo: mese deve essere un numero tra 1 e 12
    if (isNaN(mese) || mese < 1 || mese > 12) {
        errorBox.innerHTML = "Il mese non è valido.";
        return false;
    }

    // Controllo: anno deve essere un numero (due cifre) - se NaN segnalo errore
    if (isNaN(anno)) {
        errorBox.innerHTML = "Inserisci una data valida.";
        return false;
    }

    // Controllo: anno non troppo lontano nel futuro
    if (anno > annoMax) {
        errorBox.innerHTML = "Questa data sembra troppo lontana.";
        return false;
    }

    // Controllo: anno già passato
    if (anno < annoCorrente) {
        errorBox.innerHTML = "La carta è già scaduta.";
        return false;
    }

    // Controllo: se siamo nell'anno corrente, il mese non può essere minore del mese corrente
    if (anno === annoCorrente && mese < meseCorrente) {
        errorBox.innerHTML = "La carta è già scaduta.";
        return false;
    }

    // Se arrivo qui, la data è valida
    return true;
}

// -----------------------------
// Evento "blur" (quando l'utente esce dal campo)
// Mostra errore se campo vuoto o incompleto
// -----------------------------
campoScadenza.addEventListener('blur', function () {
    // Leggo il valore corrente
    var val = campoScadenza.value;

    // Se vuoto -> messaggio
    if (!val || val.trim() === "") {
        errorBox.innerHTML = "Inserisci una data valida.";
        return;
    }

    // Estraggo solo le cifre per controllare se ci sono almeno 4
    var numeri = "";
    for (var i2 = 0; i2 < val.length; i2++) {
        var ch2 = val.charAt(i2);
        if (ch2 >= '0' && ch2 <= '9') numeri += ch2;
    }

    // Se meno di 4 -> incompleto -> messaggio
    if (numeri.length < 4) {
        errorBox.innerHTML = "Inserisci una data valida.";
        return;
    }

    // Se ci sono 4 cifre, eseguo la validazione completa
    var meseStr = numeri.substring(0,2);
    var annoStr = numeri.substring(2,4);
    validaScadenzaCompleta(meseStr, annoStr);
});

// -----------------------------
// Submit: blocco l'invio del form se la data non è valida
// -----------------------------
if (form) {
    form.addEventListener('submit', function (e) {
        // Estraggo le cifre attuali dal campo
        var rawv = campoScadenza.value;
        var numeri = "";
        for (var k = 0; k < rawv.length; k++) {
            var ch3 = rawv.charAt(k);
            if (ch3 >= '0' && ch3 <= '9') numeri += ch3;
        }

        // Se meno di 4 cifre -> messaggio e blocco submit
        if (numeri.length < 4) {
            errorBox.innerHTML = "Inserisci una data valida.";
            e.preventDefault();
            return;
        }

        // Altrimenti validiamo mese+anno completi. Se non valido blocchiamo submit.
        var meseStr = numeri.substring(0,2);
        var annoStr = numeri.substring(2,4);
        

        // Se tutto ok, il form viene inviato normalmente
    });
}


// Formattazione automatica mentre si digita
campoScadenza.addEventListener('input', function() {
    var value = campoScadenza.value;
    var numeri = "";

    for (var i = 0; i < value.length; i++) {
        var c = value.charAt(i);
        if (c >= '0' && c <= '9') {
            numeri += c;
        }
    }

    if (numeri.length > 4) {
        numeri = numeri.substring(0, 4);
    }

    if (numeri.length === 1) {
        var primo = parseInt(numeri, 10);
        if (primo > 1) {
            campoScadenza.value = "0" + primo + "/";
        } else {
            campoScadenza.value = numeri;
        }
        return;
    }

	if (numeri.length >= 2) {
	    var mese = numeri.substring(0, 2);
	    var anno = numeri.substring(2, 4);

	    var meseInt = parseInt(mese, 10);
	    if (meseInt > 12) {
	        mese = "01"; // fallback di sicurezza
	    } else if (meseInt < 10 && mese.length === 1) {
	        mese = "0" + meseInt; // solo se mese ha una sola cifra
	    }

	    var annoStr = "";
	    if (anno.length > 0) {
	        annoStr = anno; // uso le cifre come sono
	    }

	    if (annoStr.length > 0) {
	        campoScadenza.value = mese + "/" + annoStr;
	    } else {
	        campoScadenza.value = mese + "/";
	    }
	}
});

// Validazione quando l’utente lascia il campo
campoScadenza.addEventListener('blur', function() {
    validaCarta(meseStr, annoStr);
});





// VALIDAZIONE NUMERO CARTA

// Prendo l'input e il contenitore per gli errori
var campoCarta = document.getElementById('numeroCarta');
var errorCarta = document.getElementById('errorCarta');

// Funzione di validazione della carta
function validaCarta() {
    var value = campoCarta.value;//otteniamo ciò che l'utente inserisce
    var soloNumeri = "";
    for (var i = 0; i < value.length; i++) {
        var c = value.charAt(i);
        if (c >= '0' && c <= '9') soloNumeri += c;
    }

    if (soloNumeri.length === 0) {
        errorCarta.innerHTML = "Inserisci un numero di carta valido.";
        return false;
    } else if (soloNumeri.length < 16) {
        errorCarta.innerHTML = "Il numero della carta deve avere 16 cifre.";
        return false;
    } else {
        errorCarta.innerHTML = "";
        return true;
    }
}

// Validazione quando l’utente lascia il campo
campoCarta.addEventListener('blur', validaCarta);

// Listener input per formattazione in blocchi da 4
campoCarta.addEventListener('input', function() {
    var value = campoCarta.value;// campoCarta
    var soloNumeri = "";
    for (var i = 0; i < value.length; i++) {
        var c = value.charAt(i);
        if (c >= '0' && c <= '9') soloNumeri += c;
    }
    if (soloNumeri.length > 16) soloNumeri = soloNumeri.substring(0,16);

    var formatted = "";
    var count = 0;
    for (var i = 0; i < soloNumeri.length; i++) {
        if (count === 4) {
            formatted += " ";
            count = 0;
        }
        formatted += soloNumeri.charAt(i);
        count++;
    }
    campoCarta.value = formatted;

    // Aggiorna messaggi live
    validaCarta();
});



// Blocca submit se carta non valida
form.addEventListener('submit', function(e) {
    if (!validaCarta() || !validaScadenzaCompleta(meseStr, annoStr)) {
	     
        e.preventDefault(); // blocca invio
		
    }
});

