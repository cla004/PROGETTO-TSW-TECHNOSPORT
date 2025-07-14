document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("loginForm");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");

    emailInput.addEventListener("blur", function () {
        validateEmailField();
        checkEmailExists();
    });

    passwordInput.addEventListener("blur", function () {
        validatePasswordField();
        checkPasswordCorrect();
    });

    form.addEventListener("submit", function (e) {
        clearErrors();
        let valid = true;

        if (!validateEmailField()) valid = false;
        if (!validatePasswordField()) valid = false;

        if (!valid) {
            e.preventDefault(); // blocca invio solo se validazione locale fallisce
        }
    });

    function validateEmailField() {
        const email = emailInput.value.trim();
        if (email === "") {
            showError("emailError", "Email obbligatoria");
            return false;
        }
        clearError("emailError");
        return true;
    }

    function validatePasswordField() {
        const password = passwordInput.value;
        if (password === "") {
            showError("passwordError", "Password obbligatoria");
            return false;
        }
        clearError("passwordError");
        return true;
    }

    function checkEmailExists() {
        const email = emailInput.value.trim();
        if (email === "") return;

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "login", true);
        xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                try {
                    const response = JSON.parse(xhr.responseText);
                    if (!response.valido) {
                        showError("emailError", "Email non registrata");
                    } else {
                        clearError("emailError");
                    }
                } catch (err) {
                    console.error("Errore nel parsing JSON (email):", err);
                }
            }
        };

        xhr.send("email=" + encodeURIComponent(email));
    }

    function checkPasswordCorrect() {
        const email = emailInput.value.trim();
        const password = passwordInput.value;
        if (email === "" || password === "") return;

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "login", true);
        xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                try {
                    const response = JSON.parse(xhr.responseText);
                    if (!response.valido) {
                        showError("passwordError", "Password errata");
                    } else {
                        clearError("passwordError");
                    }
                } catch (err) {
                    console.error("Errore nel parsing JSON (password):", err);
                }
            }
        };

        xhr.send(
            "email=" + encodeURIComponent(email) +
            "&password=" + encodeURIComponent(password)
        );
    }

    function showError(id, message) {
        const el = document.getElementById(id);
        if (el) el.textContent = message;
    }

    function clearError(id) {
        const el = document.getElementById(id);
        if (el) el.textContent = "";
    }

    function clearErrors() {
        clearError("emailError");
        clearError("passwordError");
    }
});
