package com.example.megalab.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "news")
public class News {
    @Id
    @SequenceGenerator(
            name = "news_sequence",
            sequenceName = "news_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy=GenerationType.SEQUENCE,
            generator = "news_sequence")
    @Column(name = "news_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

    @Column(name = "rubric")
    @Enumerated(EnumType.STRING)
    private Rubric rubric;

    @Column(name = "header")
    private String header;

    @Column(name = "content",
            columnDefinition = "text")
    private String content;

    @Column(name = "description")
    private String description;

    @Column(name = "picture")
    private byte[] picture;

    @ManyToMany
    @JoinTable(name="user_likes",
            joinColumns=  @JoinColumn(name="news_id", referencedColumnName="news_id"),
            inverseJoinColumns= @JoinColumn(name="user_id", referencedColumnName="user_id") )
    private Set<User> users = new HashSet<>();

    public News() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", user=" + user +
                ", rubric=" + rubric +
                ", header='" + header + '\'' +
                ", content='" + content + '\'' +
                ", description='" + description + '\'' +
                ", picture=" + Arrays.toString(picture) +
                ", users=" + users +
                '}';
    }
}
