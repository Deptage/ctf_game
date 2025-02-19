package put.edu.ctfgame.bank.repository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import put.edu.ctfgame.bank.entity.BankUser;

import java.sql.*;
import java.util.Optional;

@Repository
@AllArgsConstructor
@NoArgsConstructor
public class SusyUserRepository {

    @Value("${spring.datasource.url}")
    private String DB_URL;

    @Value("${spring.datasource.username}")
    private String DB_USERNAME;

    @Value("${spring.datasource.password}")
    private String DB_PASSWORD;

    public Optional<BankUser> findByCredentials(String username, String password) {
        String query = "SELECT * FROM bank_user WHERE username = '" + username + "' AND password = '" + password + "'";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                var user = new BankUser();
                user.setId(rs.getString("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Boolean existsByUsername(String username) {
        String query = "SELECT * FROM bank_user WHERE username = '" + username + "'";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Return false in case of an exception
    }

}