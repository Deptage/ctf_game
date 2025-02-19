package put.edu.ctfgame.bank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import put.edu.ctfgame.bank.TestDataFactory;
import put.edu.ctfgame.bank.dto.AccountDTO;
import put.edu.ctfgame.bank.entity.Account;
import put.edu.ctfgame.bank.entity.BankUser;
import put.edu.ctfgame.bank.repository.AccountRepository;
import put.edu.ctfgame.bank.util.AccountNumberGenerator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testFindAll() {
        // Given
        var accounts = List.of(TestDataFactory.sampleAccount());

        // When
        when(accountRepository.findAll()).thenReturn(accounts);

        // Then
        var result = accountService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TestDataFactory.sampleAccountDTO(), result.get(0));
    }

    @Test
    void testFindAuthenticated() {
        // Given
        var accounts = List.of(TestDataFactory.sampleAccount());

        // When
        when(accountRepository.findByUser(any(BankUser.class))).thenReturn(accounts);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(TestDataFactory.sampleBankUser());

        // Then
        var result = accountService.findAuthenticated();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TestDataFactory.sampleAccountDTO(), result.get(0));
    }

    @Test
    void testCreateAccount() {
        // Given
        var account = TestDataFactory.sampleAccount();

        // When
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(TestDataFactory.sampleBankUser());

        // Then
        var result = accountService.createAccount(null);
        assertNotNull(result);
        assertEquals(TestDataFactory.sampleAccountDTO(), result);
    }
}