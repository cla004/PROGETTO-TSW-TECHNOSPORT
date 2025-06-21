<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrazione - Maglie Calcio Online</title>
    <link rel="stylesheet" href="styles/Registrazione.css">
</head>
<body>
    <div class="background-container">
        <div class="form-container">
            <form class="registration-form" action="#" method="POST">
                <h2>Registrati</h2>

                <label for="name">Nome</label>
                <input type="text" id="name" name="name" required>

                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>

                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>

                <label for="confirm-password">Conferma Password</label>
                <input type="password" id="confirm-password" name="confirm-password" required>

                <button type="submit">Registrati</button>
            </form>
        </div>
    </div>
</body>
</html>
