package put.edu.ctfgame.bank.repository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import put.edu.ctfgame.bank.dto.SummaryTransactionDTO;
import put.edu.ctfgame.bank.dto.TransactionDTO;
import put.edu.ctfgame.bank.entity.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@AllArgsConstructor
@NoArgsConstructor
public class SusyTransactionRepository {

    @Value("${spring.datasource.url}")
    private String DB_URL;

    @Value("${spring.datasource.username}")
    private String DB_USERNAME;

    @Value("${spring.datasource.password}")
    private String DB_PASSWORD;

    public List<SummaryTransactionDTO> findByUsernameAndTitleContaining(String text, String username) {
        String query =
                "SELECT t.title, " +
                        "CONCAT('$', TO_CHAR(t.amount, 'FM999999999.00')) AS amount, " +
                        "TO_CHAR(t.created_at, 'YYYY-MM-DD HH24:MI:SS') AS timestamp, " +
                        "sa.account_number AS source_account_number, da.account_number AS destination_account_number " +
                        "FROM transaction t " +
                        "INNER JOIN account sa ON t.source_account_id = sa.account_id " +
                        "INNER JOIN account da ON t.destination_account_id = da.account_id " +
                        "INNER JOIN bank_user sau ON sa.user_id = sau.user_id " +
                        "INNER JOIN bank_user dau ON da.user_id = dau.user_id " +
                        "WHERE (sau.username = '" + username + "' OR dau.username = '" + username + "') " +
                        "AND t.title LIKE '%" + text + "%'";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            List<SummaryTransactionDTO> transactions = new ArrayList<>();
            while (rs.next()) {
                var transaction = new SummaryTransactionDTO();
                transaction.setCreatedAt(rs.getString("timestamp"));
                transaction.setTitle(rs.getString("title"));
                transaction.setAmount(rs.getString("amount"));
                transaction.setSourceAccountNumber(rs.getString("source_account_number"));
                transaction.setDestinationAccountNumber(rs.getString("destination_account_number"));
                transactions.add(transaction);
            }
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
