package com.example.springbackoffice.entity;

import com.example.springbackoffice.dto.PostRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
@Entity
@Getter
@Setter
@Table(name = "post")
@NoArgsConstructor
public class Post extends Timestamped { // 상속받아서 createdAt, modifiedAt column 가져옴
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 3000)
    private String content;

    @Column(nullable = false)
    private int post_like_count;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;


    @OneToMany( mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user;
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
    }

    public void updatePost(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }
}