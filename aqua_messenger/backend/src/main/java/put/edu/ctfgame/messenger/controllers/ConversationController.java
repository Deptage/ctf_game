package put.edu.ctfgame.messenger.controllers;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import put.edu.ctfgame.messenger.DTO.*;
import put.edu.ctfgame.messenger.service.MessageService;
import put.edu.ctfgame.messenger.service.ConversationService;
import put.edu.ctfgame.messenger.entity.Conversation;
import put.edu.ctfgame.messenger.entity.Message;
import put.edu.ctfgame.messenger.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/conversation")
@RequiredArgsConstructor
public class ConversationController {

    private final MessageService messageService;
    private final ConversationService conversationService;
    private final UserService userService;

    @Value("${FRONTEND_PORT:3000}")
    private String frontendPort;

    @GetMapping
    public ResponseEntity<List<ConversationDTO>> getConversationsList() {
        var username = userService.findAuthenticated().getUsername();
        var conversations = conversationService.findConversationsByUser(username).stream().map(conv ->  ConversationDTO.from(conv, username)).toList();
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/{conversationId}/info")
    public ResponseEntity<ConversationTypeInfo> getConversationInfo(@PathVariable Long conversationId) {
        return ResponseEntity.ok(conversationService.getConversationInfo(conversationId));
    }

    @GetMapping("/{conversationId}/sentMessages")
    public ResponseEntity<List<MessageDTO>> getSentMessages(@PathVariable Long conversationId) {
        return ResponseEntity.ok(messageService.getSentMessages(conversationId));
    }

    @GetMapping("/{conversationId}/nextMessages")
    public ResponseEntity<Set<MessagePreviewDTO>> getNextMessages(@PathVariable Long conversationId) {
        Set<MessagePreviewDTO> messages = conversationService.getNextMessagesPreviews(conversationId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{conversationId}/sendNew")
    public ResponseEntity<MessageDTO> sendNew(@PathVariable Long conversationId, @RequestBody MessageToAddDTO messageRequest) {
        return ResponseEntity.ok(messageService.sendNew(conversationId, messageRequest));
    }

    @PostMapping("/{conversationId}/sendExisting/{messageId}")
    public ResponseEntity<List<MessageDTO>> sendExisting(@PathVariable Long conversationId, @PathVariable Long messageId) {
        return ResponseEntity.ok(messageService.sendExisting(conversationId, messageId));
    }
}