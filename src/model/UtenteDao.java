import model;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Utente;

public class UtenteDAO {

    public void inserisciUtente(Utente utente) {
        String sql = "INSERT INTO utenti (nome, email, password,cognome) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getEmail());
            stmt.setString(3, utente.getPassword());
            stmt.setString(4, utente.getCognome());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Utente cercaUtenteById(int id) {
        String sql = "SELECT * FROM utenti WHERE id = ?";
        Utente utente = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                utente = new Utente();
                utente.setId(rs.getInt("id"));
                utente.setNome(rs.getString("nome"));
                utente.setEmail(rs.getString("email"));
                utente.setPassword(rs.getString("password"));
                utente.setCognome(rs.getString("cognome"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return utente;
    }

    public List<Utente> listaUtenti() {
        String sql = "SELECT * FROM utenti";
        List<Utente> lista = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Utente utente = new Utente();
                utente.setId(rs.getInt("id"));
                utente.setNome(rs.getString("nome"));
                utente.setEmail(rs.getString("email"));
                utente.setPassword(rs.getString("password"));
                utente.setCognome(rs.getString("cognome"));
                lista.add(utente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void aggiornaUtente(Utente utente) {
        String sql = "UPDATE utenti SET nome = ?, email = ?, password = ?,cognoeme = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getEmail());
            stmt.setString(3, utente.getPassword());
            stmt.setInt(4, utente.getId());
            stmt.setInt(5, utente.getCognome);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminaUtente(int id) {
        String sql = "DELETE FROM utenti WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean emailEsiste(String email) {
        String sql = "SELECT 1 FROM utenti WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean passwordEsiste(String password) {
        String sql = "SELECT 1 FROM utenti WHERE password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}