package put.edu.ctfgame.bank.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import put.edu.ctfgame.bank.dto.AccountDTO;
import put.edu.ctfgame.bank.service.AccountService;
import put.edu.ctfgame.bank.service.JwtService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<List<AccountDTO>> authenticatedAccount() {
        return ResponseEntity.ok(accountService.findAuthenticated());
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestParam(required = false) Float initBalance) {
        //if user has more than 3 accounts, he can't create new one
        if (accountService.findAuthenticated().size() >= 3) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(accountService.createAccount(initBalance));
    }

    @GetMapping("/checkBalance")
    public ResponseEntity<HashMap<String,String>> checkAccountBalance(HttpServletRequest request) {
        HashMap<String,String> response = new HashMap<>();
        var token = request.getHeader("Authorization").substring(7);
        var username = jwtService.extractUsername(token);
        if (!Objects.equals(username, "hacker")){
            response.put("status", "This user is not authorized to get the flag");
            return ResponseEntity.badRequest().body(response);
        }

        if(accountService.getMaxAccountBalance() < 1000){
            response.put("status", "You are not rich enough to get the flag");
            return ResponseEntity.ok(response);
        }

        response.put("status", "TGHM{sig3egv_tr4ns4cti0n_f4ult}");
        return ResponseEntity.ok(response);
    }
}
