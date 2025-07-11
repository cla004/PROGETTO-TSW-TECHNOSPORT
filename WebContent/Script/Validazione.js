document.addEventListener("DOMContentLoaded", function() {
    var form = document.getElementById("registrationForm");

    var nameInput = document.getElementById("name");
    var emailInput = document.getElementById("email");
    var passwordInput = document.getElementById("password");
    var confirmPasswordInput = document.getElementById("confirm-password");

    // Eventi di blur (quando si esce dal campo)
    nameInput.addEventListener("blur", function() {
        validateName();
    });

    emailInput.addEventListener("blur", function() {
        validateEmailField();
    });

    passwordInput.addEventListener("blur", function() {
        validatePassword();
        validateConfirmPassword(); // aggiorna anche se cambia la principale
    });

    confirmPasswordInput.addEventListener("blur", function() {
        validateConfirmPassword();
    });

    // Validazione al submit
    form.addEventListener("submit", function(e) {
        var valid = true;
        clearErrors();

        if (!validateName()) valid = false;
        if (!validateEmailField()) valid = false;
        if (!validatePassword()) valid = false;
        if (!validateConfirmPassword()) valid = false;

        if (!valid) {
            e.preventDefault();
        }
    });

    // Funzioni di validazione per singoli campi
    function validateName() {
        var name = nameInput.value.trim();
        if (name === "") {
            showError("nameError", "Il nome è obbligatorio");
            return false;
        }
        return true;
    }

    function validateEmailField() {
        var email = emailInput.value.trim();
        var re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!re.test(email)) {
            showError("emailError", "Inserisci un'email valida");
            return false;
        }
        return true;
    }

    function validatePassword() {
        var password = passwordInput.value;
        if (password.length < 8) {
            showError("passwordError", "La password deve contenere almeno 8 caratteri");
            return false;
        }
        return true;
    }

    function validateConfirmPassword() {
        var password = passwordInput.value;
        var confirmPassword = confirmPasswordInput.value;
        if (password !== confirmPassword) {
            showError("confirmPasswordError", "Le password non coincidono");
            return false;
        }
        return true;
    }

    // Gestione degli errori nel DOM
    function clearErrors() {
        var errors = document.getElementsByClassName("error-message");
        for (var i = 0; i < errors.length; i++) {
            errors[i].textContent = "";
        }
    }

    function showError(id, message) {
        var el = document.getElementById(id);
        if (el) {
            el.textContent = message;
        }
    }
});