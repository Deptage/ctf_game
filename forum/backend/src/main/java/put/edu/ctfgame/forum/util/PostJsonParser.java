package put.edu.ctfgame.forum.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import put.edu.ctfgame.forum.dto.CommentDTO;
import put.edu.ctfgame.forum.dto.ForumUserDTO;
import put.edu.ctfgame.forum.dto.PostDTO;
import put.edu.ctfgame.forum.entity.Comment;
import put.edu.ctfgame.forum.entity.ForumUser;
import put.edu.ctfgame.forum.entity.Post;
import put.edu.ctfgame.forum.entity.Role;
import put.edu.ctfgame.forum.repository.PostRepository;
import put.edu.ctfgame.forum.repository.RoleRepository;
import put.edu.ctfgame.forum.repository.UserRepository;

import java.awt.desktop.AppReopenedEvent;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
@AllArgsConstructor
public class PostJsonParser {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void parsePosts(String json) throws Exception {
        // Deserialize JSON into a list of PostDTO
        ClassPathResource resource = new ClassPathResource(json);
        ObjectMapper objectMapper = new ObjectMapper();
        List<PostJson> postJsonList;
        try (InputStream inputStream = resource.getInputStream()) {
            postJsonList = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<PostJson>>() {}
            );
        }
        List<String> existingUsernames = List.of("user", "admin");
        Set<String> usernames = postJsonList.stream()
                .flatMap(postJson -> Stream.concat(
                        Stream.of(postJson.getAuthor().getUsername()),
                        postJson.getComments().stream().map(commentJson -> commentJson.getAuthor().getUsername())
                ).filter(username -> !existingUsernames.contains(username)))
                .collect(Collectors.toSet());
        // Fetch ForumUser entities from the database
        Role userRole = roleRepository.findByName("USER").orElseThrow();
        List<ForumUser> users = usernames.stream().map(username ->
                ForumUser.builder()
                        .username(username)
                        .roles(Set.of(userRole))
                        .password("ihaveneverseenamorecomplicatedandunobtainablepassword")
                        .build()
            ).toList();
        userRepository.saveAll(users);
        var savedUsers = userRepository.findAll();

        // Convert PostDTOs to Post entities
        var posts = postJsonList.stream().map(dto -> {
            Post post = Post.fromJson(dto);
            post.setAuthor(savedUsers.stream().filter(user -> user.getUsername().equals(dto.getAuthor().getUsername())).findFirst().orElseThrow());
            List<Comment> comments = dto.getComments().stream()
                    .map(commentDTO -> {
                        var comment = Comment.fromJson(commentDTO, post);
                        var attemptedAuthor = commentDTO.getAuthor().getUsername();
                        comment.setAuthor(savedUsers.stream().filter(user -> user.getUsername().equals(commentDTO.getAuthor().getUsername())).findFirst().orElseThrow());
                        return comment;
                    }).toList();
            post.setComments(comments);
            return post;
        }).toList();
        postRepository.saveAll(posts);
    }

    @Data
    public static class PostJson {
        private Long id;
        private String title;
        private String content;

        @JsonProperty("author")
        private ForumUserJson author;

        @JsonProperty("comments")
        private List<CommentJson> comments;
    }

    @Data
    public static class ForumUserJson {
        private String username;
    }

    @Data
    public static class CommentJson {
        private String content;

        @JsonProperty("author")
        private ForumUserJson author;
    }
}
