package me.leegiseok.project.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(nullable = false, unique = true)
    private  String username;

    @Column(nullable = false)
    private  String password;

    @Column(nullable = false)
    private  String role;

    @Column(nullable = false)
    private  String nickname;

    public  String getRollKey() {
        return this.role;
    }

}
