package com.example.megalab.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "comment")
//@NamedEntityGraph(name = "Comment.comment",
//attributeNodes = @NamedAttributeNode("commentForComment"))
public class Comment {

    @Id
    @SequenceGenerator(
            name = "comment_sequence",
            sequenceName = "comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy=GenerationType.SEQUENCE,
            generator = "comment_sequence")
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="news_id")
    @JsonIgnore
    private News news;

    @Column(name = "comment")
    private String comment;

    @OneToMany
    @JoinColumn(name = "comments_id")
    private List<Comment> commentForComment;

    public Comment() {
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

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Comment> getCommentForComment() {
        return commentForComment;
    }

    public void setCommentForComment(List<Comment> commentForComment) {
        this.commentForComment = commentForComment;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", user=" + user +
                ", news=" + news +
                ", comment='" + comment + '\'' +
//                ", commentForComment=" + commentForComment +
                '}';
    }
}
