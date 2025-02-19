package put.edu.ctfgame.bank.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import put.edu.ctfgame.bank.TestDataFactory;
import put.edu.ctfgame.bank.repository.AccountRepository;
import put.edu.ctfgame.bank.service.AccountService;
import put.edu.ctfgame.bank.service.JwtService;
import put.edu.ctfgame.bank.service.TransactionService;
import put.edu.ctfgame.bank.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    public void testAllTransactionsByTitle_success() throws Exception {
        // Mock the TransactionService
        var transactionDTOList = List.of(TestDataFactory.sampleSummaryTransactionDTO());
        when(transactionService.findAllAuthenticatedByTitleContaining(anyString())).thenReturn(transactionDTOList);

        // Perform the GET request
        mockMvc.perform(get("/transactions/searchByTitle")
                        .header("Instance-Id", "instance-id")
                        .param("title", "Sample Title")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sourceAccountNumber").value(transactionDTOList.get(0).getSourceAccountNumber()))
                .andExpect(jsonPath("$[0].destinationAccountNumber").value(transactionDTOList.get(0).getDestinationAccountNumber()))
                .andExpect(jsonPath("$[0].amount").value(transactionDTOList.get(0).getAmount()));
    }

    // @Test
    // public void testAllTransactionsByTitle_unauthorized() throws Exception {
    //     mockMvc.perform(get("/transactions/searchByTitle")
    //                     .header("Instance-Id", "instance-id")
    //                     .contentType(MediaType.APPLICATION_JSON))
    //             .andExpect(status().isUnauthorized());
    // }

    @Test
    @WithMockUser
    public void testCreateTransaction_success() throws Exception {
        // Create a valid TransactionDTO
        var transactionDTO = TestDataFactory.sampleTransactionDTO();

        // Mock the TransactionService
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.of(TestDataFactory.sampleAccount()));
        when(transactionService.createTransaction(any())).thenReturn(transactionDTO);

        // Perform the POST request
        mockMvc.perform(post("/transactions")
                        .header("Instance-Id", "instance-id")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"sourceAccountNumber\":\"123\",\"destinationAccountNumber\":\"456\",\"amount\":200.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sourceAccountNumber").value("123"))
                .andExpect(jsonPath("$.destinationAccountNumber").value("456"))
                .andExpect(jsonPath("$.amount").value(transactionDTO.getAmount()));
    }

    // @Test
    // public void testCreateTransaction_unauthorized() throws Exception {
    //     mockMvc.perform(post("/transactions")
    //                     .header("Instance-Id", "instance-id")
    //                     .with(csrf())
    //                     .contentType("application/json"))
    //             .andExpect(status().isUnauthorized());
    // }

    @Test
    @WithMockUser
    public void testCreateTransaction_invalidRequestBody() throws Exception {
        mockMvc.perform(post("/transactions")
                        .header("Instance-Id", "instance-id")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"sourceAccountId\":1,\"destinationAccountId\":2,\"amount\":-200.0}"))
                .andExpect(status().isBadRequest());
    }
}
