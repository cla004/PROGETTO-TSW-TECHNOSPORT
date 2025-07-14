document.addEventListener("DOMContentLoaded", function() {
    // Prendo il form e i campi input dal DOM
    var form = document.getElementById("registrationForm");
    var nameInput = document.getElementById("name");
    var emailInput = document.getElementById("email");
    var passwordInput = document.getElementById("password");
    var confirmPasswordInput = document.getElementById("confirm-password");
    var successMessage = document.getElementById("successMessage");

    // Quando esco dal campo "nome" eseguo la validazione
    nameInput.addEventListener("blur", function() {
        validateName();
    });

    // Quando esco dal campo "email" valido e controllo con AJAX se esiste già
    emailInput.addEventListener("blur", function() {
        validateEmailField();
        checkEmailExists();
    });

    // Quando esco dal campo "password" valido, controllo la conferma password e controllo con AJAX
    passwordInput.addEventListener("blur", function() {
        validatePassword();
        validateConfirmPassword();
        checkPasswordExists();
    });

    // Quando esco dal campo "conferma password" valido che corrisponda
    confirmPasswordInput.addEventListener("blur", function() {
        validateConfirmPassword();
    });

    // Alla submit del form valido tutti i campi
    form.addEventListener("submit", function(e) {
        // Pulisco eventuali messaggi di errore precedenti
        clearErrors();
        successMessage.style.display = "none";

        var valid = true;
        if (!validateName()) valid = false;
        if (!validateEmailField()) valid = false;
        if (!validatePassword()) valid = false;
        if (!validateConfirmPassword()) valid = false;

        // Se qualche validazione fallisce, blocco l'invio del form
        if (!valid) {
            e.preventDefault();
            return;
        }


    });

    // Funzione che valida che il nome non sia vuoto
    function validateName() {
        var name = nameInput.value.trim();
        if (name === "") {
            showError("nameError", "Il nome è obbligatorio");
            return false;
        }
        clearError("nameError");
        return true;
    }

    // Funzione che valida che la mail abbia un formato valido
    function validateEmailField() {
        var email = emailInput.value.trim();
        // Regex semplice per verificare un formato base di email
        var re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!re.test(email)) {
            showError("emailError", "Inserisci un'email valida");
            return false;
        }
        clearError("emailError");
        return true;
    }

    // Funzione che valida la password
    function validatePassword() {
        var password = passwordInput.value;
        // Controllo lunghezza minima 8 e presenza almeno un carattere speciale
        var specialCharRegex = /[!@#$%^&*(),.?":{}|<>]/;
        if (password.length < 8) {
            showError("passwordError", "La password deve contenere almeno 8 caratteri");
            return false;
        } else if (!specialCharRegex.test(password)) {
            showError("passwordError", "La password deve contenere almeno un carattere speciale");
            return false;
        }
        clearError("passwordError");
        return true;
    }

    // Funzione che verifica che password e conferma password coincidano
    function validateConfirmPassword() {
        var password = passwordInput.value;
        var confirmPassword = confirmPasswordInput.value;
        if (password !== confirmPassword) {
            showError("confirmPasswordError", "Le password non coincidono");
            return false;
        }
        clearError("confirmPasswordError");
        return true;
    }

    // Funzione AJAX per controllare se l'email è già usata nel DB
    function checkEmailExists() {
        var email = emailInput.value.trim();
        if (email === "") return;

        var xhr = new XMLHttpRequest();
        // Uso GET, con parametri codificati per sicurezza
        xhr.open("GET", "controlloEmail?email=" + encodeURIComponent(email), true);
        xhr.onreadystatechange = function() {
            // Quando la richiesta è completata
            if (xhr.readyState === 4 && xhr.status === 200) {
                var response = xhr.responseText;
                // Se la risposta è "true" significa che email esiste già
                if (response === "true") {
                    showError("emailError", "Email già utilizzata");
                } else {
                    clearError("emailError");
                }
            }
        };
        xhr.send();
    }

    // Funzione AJAX per controllare se la password è già usata (opzionale)
    function checkPasswordExists() {
        var password = passwordInput.value;
        if (password === "") return;

        var xhr = new XMLHttpRequest();
        xhr.open("GET", "controlloPassword?password=" + encodeURIComponent(password), true);
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                var response = xhr.responseText;
                if (response === "true") {
                    showError("passwordError", "Password già utilizzata");
                } else {
                    clearError("passwordError");
                }
            }
        };
        xhr.send();
    }

    // Mostra il messaggio di errore nel campo specificato
    function showError(id, message) {
        var el = document.getElementById(id);
        if (el) {
            el.textContent = message;
        }
    }

    // Rimuove il messaggio di errore dal campo specificato
    function clearError(id) {
        var el = document.getElementById(id);
        if (el) {
            el.textContent = "";
        }
    }

    // Pulisce tutti i messaggi di errore
    function clearErrors() {
        clearError("nameError");
        clearError("emailError");
        clearError("passwordError");
        clearError("confirmPasswordError");
    }
});
