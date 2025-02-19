package put.edu.ctfgame.messenger.databaseInit;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import put.edu.ctfgame.messenger.DTO.UserDTO;
import put.edu.ctfgame.messenger.entity.Conversation;
import put.edu.ctfgame.messenger.entity.Message;
import put.edu.ctfgame.messenger.entity.User;
import put.edu.ctfgame.messenger.enums.ConversationType;
import put.edu.ctfgame.messenger.repository.UserRepository;
import put.edu.ctfgame.messenger.repository.MessageRepository;
import put.edu.ctfgame.messenger.repository.ConversationRepository;
import put.edu.ctfgame.messenger.service.AuthService;
import put.edu.ctfgame.messenger.service.MessageService;
import put.edu.ctfgame.messenger.util.DialogParser;

@Slf4j
@Configuration
@AllArgsConstructor
public class DataLoader {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;
    private final AuthService authService;
    private final DialogParser dialogParser;

    private final String ANTIQUA_USERNAME = "antiquascarlet1697";
    private final String ROBERT_USERNAME = "Robert Carlson";
    private final String MADELEINE_USERNAME = "Madeleine Currey";
    private final String CAROLINE_USERNAME = "Caroline Reapersky";
    private final String JAKE_USERNAME = "Jake Pawel";

    @Bean
    CommandLineRunner initDatabase(MessageRepository messageRepository, UserRepository userRepository, ConversationRepository conversationRepository) {
        return args -> {
            List<String> usernames = Arrays.asList("antiquascarlet1697", "Robert Carlson", "Madeleine Currey", "Caroline Reapersky", "Jake Pawel");

            var userDTOs = usernames.stream()
                    .map(username -> UserDTO.builder()
                            .username(username)
                            .password(username.equals("antiquascarlet1697") ? "password123" : "areallydifficultpasswordtoguessthatsliterallytimpossibletohack")
                            .isBot(!username.equals("antiquascarlet1697"))
                            .build())
                    .toList();
            authService.signUpBulk(userDTOs);

            var users = userRepository.findAll();
            List<Conversation> conversations = List.of(
                    Conversation.builder()
                            .user1(users.stream().filter(u -> u.getUsername().equals(ANTIQUA_USERNAME)).findFirst().orElseThrow())
                            .user2(users.stream().filter(u -> u.getUsername().equals(ROBERT_USERNAME)).findFirst().orElseThrow())
                            .type(ConversationType.SCRIPTED)
                            .build(),
                    Conversation.builder()
                            .user1(users.stream().filter(u -> u.getUsername().equals(ANTIQUA_USERNAME)).findFirst().orElseThrow())
                            .user2(users.stream().filter(u -> u.getUsername().equals(MADELEINE_USERNAME)).findFirst().orElseThrow())
                            .type(ConversationType.SCRIPTED)
                            .build(),
                    Conversation.builder()
                            .user1(users.stream().filter(u -> u.getUsername().equals(ANTIQUA_USERNAME)).findFirst().orElseThrow())
                            .user2(users.stream().filter(u -> u.getUsername().equals(CAROLINE_USERNAME)).findFirst().orElseThrow())
                            .type(ConversationType.SCRIPTED)
                            .build(),
                    Conversation.builder()
                            .user1(users.stream().filter(u -> u.getUsername().equals(ROBERT_USERNAME)).findFirst().orElseThrow())
                            .user2(users.stream().filter(u -> u.getUsername().equals(JAKE_USERNAME)).findFirst().orElseThrow())
                            .type(ConversationType.SCRIPTED)
                            .build()
            );

            conversationRepository.saveAll(conversations);

            dialogParser.parseMessageSequenceFromJson("robert_carlson.json", ANTIQUA_USERNAME, ROBERT_USERNAME);
            dialogParser.parseMessageSequenceFromJson("madeleine_currey.json", ANTIQUA_USERNAME, MADELEINE_USERNAME);
            dialogParser.parseMessageSequenceFromJson("caroline_reapersky.json", ANTIQUA_USERNAME, CAROLINE_USERNAME);
            dialogParser.parseMessageSequenceFromJson("dialog_robert_carlson_jake_pawel.json", ROBERT_USERNAME, JAKE_USERNAME);

        };
    }
}
