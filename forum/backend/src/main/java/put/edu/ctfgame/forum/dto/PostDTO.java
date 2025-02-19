package put.edu.ctfgame.forum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.forum.entity.Post;


import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PostDTO {

    private Long id;

    @NotNull(message = "Title cannot be null")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotNull(message = "Content cannot be null")
    @NotBlank(message = "Content cannot be blank")
    private String content;

    private PostAuthorDTO author;

    private List<CommentDTO> comments;

    public static PostDTO from(Post post) {
        return PostDTO.builder()
            .id(post.getId())
            .title(post.getTitle())
            .content(post.getContent())
            .author(PostAuthorDTO.from(post.getAuthor()))
            .comments(post.getComments().stream().map(CommentDTO::from).collect(Collectors.toList()))
            .build();
    }
}
