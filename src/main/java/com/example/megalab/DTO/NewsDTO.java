package com.example.megalab.DTO;

import com.example.megalab.entity.Rubric;
import com.example.megalab.entity.User;


public class NewsDTO {

    private Long id;

    private UserDTO userDTO = new UserDTO();

    private Rubric rubric;

    private String header;

    private String content;

    private String description;

    private byte[] picture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return userDTO;
    }

    public void setUser(User user) {
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setLogin(user.getLogin());
    }

    public Rubric getRubric() {
        return rubric;
    }

    public void setRubric(Rubric rubric) {
        this.rubric = rubric;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
