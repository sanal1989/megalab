package com.example.megalab.DTO;

import com.example.megalab.entity.Comment;
import com.example.megalab.entity.User;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommentDTO {

    private Long id;

    private UserDTO userDTO = new UserDTO();

    private String comment;

    private List<CommentDTO> commentForComment = new ArrayList<>();

    public CommentDTO() {
    }

    public CommentDTO(Long id, UserDTO userDTO, String comment, List<CommentDTO> commentForComment) {
        this.id = id;
        this.userDTO = userDTO;
        this.comment = comment;
        this.commentForComment = commentForComment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(User user) {
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setLogin(user.getLogin());
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<CommentDTO> getCommentForComment() {
        return commentForComment;
    }

    public void setCommentForComment(List<CommentDTO> commentForComment) {
        this.commentForComment = commentForComment;
    }
}
