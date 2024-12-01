package com.example.bugfixhub.entity.friend;

import com.example.bugfixhub.entity.BaseEntity;
import com.example.bugfixhub.entity.user.User;
import com.example.bugfixhub.enums.FriendStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "friend")
public class Friend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendStatus status;

    @Setter
    @ManyToOne
    @JoinColumn(name = "follow_id")
    private User follower;  // follow 받은 사용자


    @Setter
    @ManyToOne
    @JoinColumn(name = "following_id")
    private User following; // follow 요청한 사용자

    public Friend(User follower, User following, FriendStatus status) {
        this.follower = follower;
        this.following = following;
        this.status = status;

    }

    public Friend() {

    }
}
