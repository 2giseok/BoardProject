package me.leegiseok.project.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor
public class RefreshToken {
    @Id
    private  Long userId;

    @Column(nullable = false, unique = true)
    private  String token;



    public RefreshToken update(String newToken) {
        this.token=  newToken;
        return this;
    }
}
