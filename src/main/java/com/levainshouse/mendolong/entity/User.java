package com.levainshouse.mendolong.entity;

import com.levainshouse.mendolong.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class User {

    @Column(name = "user_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user", cascade = ALL)
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    @Builder.Default
    private List<Item> items = new ArrayList<>();

    @Column(name = "username", nullable = false, unique = true)
    private final String username;

    @Column(name = "kakao_talk_chatting_url", nullable = false,
            columnDefinition = "VARCHAR(255) CHECK (kakao_talk_chatting_url LIKE 'https://open.kakao.com/%')")
    private final String kakaoTalkChattingUrl;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private final UserRole role;
}
