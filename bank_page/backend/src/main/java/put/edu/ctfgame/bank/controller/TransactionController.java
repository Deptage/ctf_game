package put.edu.ctfgame.bank.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import put.edu.ctfgame.bank.dto.AccountDTO;
import put.edu.ctfgame.bank.dto.SummaryTransactionDTO;
import put.edu.ctfgame.bank.dto.TransactionDTO;
import put.edu.ctfgame.bank.entity.Account;
import put.edu.ctfgame.bank.service.AccountService;
import put.edu.ctfgame.bank.service.TransactionService;
import put.edu.ctfgame.bank.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final AccountService accountService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<String>> currentUserAllTransactions() {
        return ResponseEntity.ok(transactionService.findAllAuthenticated());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/searchByTitle")
    public ResponseEntity<List<SummaryTransactionDTO>> allTransactionsByTitle(@Valid @RequestParam String title) {
        return ResponseEntity.ok(transactionService.findAllAuthenticatedByTitleContaining(title));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        var sourceAccountNumber = transactionDTO.getSourceAccountNumber();
        var destinationAccountNumber = transactionDTO.getDestinationAccountNumber();
        //if dest account number is owned by the hacker and source account number is not, return error
        var user = userService.findByUsername("hacker");
        List<String> userAccountNumbers = accountService.getAccountNumbersByUser(user);
        if(userAccountNumbers.contains(destinationAccountNumber) && !userAccountNumbers.contains(sourceAccountNumber)) {
            throw new AccessDeniedException("User is temporarily blocked and can't accept transactions");
        }
        return ResponseEntity.ok(transactionService.createTransaction(transactionDTO));
    }
}