package put.edu.ctfgame.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import put.edu.ctfgame.bank.TestDataFactory;
import put.edu.ctfgame.bank.dto.TransactionDTO;
import put.edu.ctfgame.bank.entity.Account;
import put.edu.ctfgame.bank.entity.Transaction;
import put.edu.ctfgame.bank.exception.InsufficientFundsException;
import put.edu.ctfgame.bank.exception.InvalidAccountException;
import put.edu.ctfgame.bank.repository.AccountRepository;
import put.edu.ctfgame.bank.repository.SusyTransactionRepository;
import put.edu.ctfgame.bank.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private UserService userService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SusyTransactionRepository susyTransactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account sourceAccount;
    private Account destinationAccount;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        sourceAccount = TestDataFactory.sampleAccount();
        destinationAccount = TestDataFactory.sampleAccount2();
        transactionDTO = TestDataFactory.sampleTransactionDTO();
    }

    @Test
    void testFindAll() {
        // Given
        var transactions = List.of(TestDataFactory.sampleTransaction(), TestDataFactory.sampleTransaction());

        // When
        when(transactionRepository.findAll()).thenReturn(transactions);

        // Then
        var result = transactionService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindAllAuthenticatedByTitleContaining() {
        // Given
        var username = "user";
        var transactions = List.of(TestDataFactory.sampleSummaryTransactionDTO(), TestDataFactory.sampleSummaryTransactionDTO());

        // When
        when(userService.findAuthenticated()).thenReturn(TestDataFactory.sampleBankUserDTO());
        when(susyTransactionRepository.findByUsernameAndTitleContaining(anyString(), anyString())).thenReturn(transactions);

        // Then
        var result = transactionService.findAllAuthenticatedByTitleContaining("Sample Title");
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(transactions.get(0), result.get(0));
    }

    @Test
    void testCreateTransaction_Success() {
        // When
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("456")).thenReturn(Optional.of(destinationAccount));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(TestDataFactory.sampleTransaction());
        when(accountRepository.getAccountBalance("123")).thenReturn(sourceAccount.getBalance()-100.0f);
        when(accountRepository.getAccountBalance("456")).thenReturn(destinationAccount.getBalance()+100.0f);
        // Then
        var result = transactionService.createTransaction(transactionDTO);
        assertNotNull(result);
        assertEquals(900.0f, sourceAccount.getBalance());
        assertEquals(600.0f, destinationAccount.getBalance());
    }

    @Test
    void testCreateTransaction_InsufficientFunds() {
        // Given
        transactionDTO.setAmount(2000.0f);

        // When
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("456")).thenReturn(Optional.of(destinationAccount));

        // Then
        assertThrows(InsufficientFundsException.class, () -> transactionService.createTransaction(transactionDTO));
    }

    @Test
    void testCreateTransaction_InvalidSourceAccount() {
        // Then
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.empty());

        // Then
        assertThrows(InvalidAccountException.class, () -> transactionService.createTransaction(transactionDTO));
    }

    @Test
    void testCreateTransaction_InvalidDestinationAccount() {
        // When
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("456")).thenReturn(Optional.empty());

        // Then
        assertThrows(InvalidAccountException.class, () -> transactionService.createTransaction(transactionDTO));
    }
}