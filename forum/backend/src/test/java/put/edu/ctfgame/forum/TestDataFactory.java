package put.edu.ctfgame.forum;

import put.edu.ctfgame.forum.dto.ForumUserDTO;
import put.edu.ctfgame.forum.dto.PostAuthorDTO;
import put.edu.ctfgame.forum.entity.Comment;
import put.edu.ctfgame.forum.entity.ForumUser;
import put.edu.ctfgame.forum.entity.Post;
import put.edu.ctfgame.forum.entity.Role;

import java.util.List;

public class TestDataFactory {
    public static ForumUser sampleForumUser() {
        return ForumUser.builder()
              .id(1L)
              .username("user")
              .password("password")
              .build();
    }

    public static ForumUserDTO sampleForumUserDTO() {
        return ForumUserDTO.from(sampleForumUser());
    }

    public static PostAuthorDTO sampleSimpleForumUserDTO() {
        return PostAuthorDTO.from(sampleForumUser());
    }

    public static Role sampleRole() {
        return Role.builder()
              .id(1L)
              .name("USER")
              .build();
    }

    public static Comment sampleComment() {
        return Comment.builder()
              .id(1L)
              .author(sampleForumUser())
              .content("content")
              .build();
    }

    public static Post samplePost() {
        return Post.builder()
                .id(1L)
                .author(sampleForumUser())
                .title("title")
                .comments(List.of(sampleComment(), sampleComment()))
                .content("content")
                .build();
    }

    public static Post samplePostId68() {
        return Post.builder()
                .id(68L)
                .author(sampleForumUser())
                .title("title")
                .comments(List.of(sampleComment(), sampleComment()))
                .content("content")
                .build();
    }
}
