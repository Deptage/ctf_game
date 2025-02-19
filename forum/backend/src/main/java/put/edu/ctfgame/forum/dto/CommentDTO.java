package put.edu.ctfgame.forum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.forum.entity.Comment;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommentDTO {

    @NotNull(message = "Content cannot be null")
    @NotBlank(message = "Content cannot be blank")
    private String content;

    @NotNull(message = "Author cannot be null")
    private CommentAuthorDTO author;

    public static CommentDTO from(Comment comment) {
        return CommentDTO.builder()
            .content(comment.getContent())
            .author(CommentAuthorDTO.from(comment.getAuthor()))
            .build();
    }
}
