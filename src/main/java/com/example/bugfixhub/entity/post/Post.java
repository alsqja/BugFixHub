package com.example.bugfixhub.entity.post;

import com.example.bugfixhub.entity.BaseEntity;
import com.example.bugfixhub.entity.comment.Comment;
import com.example.bugfixhub.entity.like.PostLike;
import com.example.bugfixhub.entity.user.User;
import com.example.bugfixhub.enums.PostType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "post")
@SQLDelete(sql = "UPDATE post SET deleted = true WHERE id = ?")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(nullable = false)
    private String contents;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostType type;

    @Setter
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PostLike> likes = new ArrayList<>();

    public Post(String title, String contents, PostType type) {
        this.title = title;
        this.contents = contents;
        this.type = type;
    }

    public Post() {

    }

    public void updateDelete(boolean deleted) {
        this.deleted = deleted;
    }

    public void update(String title, String contents, PostType type) {
        this.title = title == null ? this.title : title;
        this.contents = contents == null ? this.contents : contents;
        this.type = type == null ? this.type : type;
    }
}
