<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Member Login</title>
    <link rel="stylesheet" href="styles/LoginCss.css">
</head>
<body>
    <div class="login-container">
        <h1>Member Login</h1>
        
        <form action="login" method="post">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>
            </div>
            
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <button type="submit" class="login-button">LOGIN</button>
        </form>
        
        <div class="forgot-link">
            <a href="forgot-password">Forgot Username / Password?</a>
        </div>
    </div>
</body>
</html>