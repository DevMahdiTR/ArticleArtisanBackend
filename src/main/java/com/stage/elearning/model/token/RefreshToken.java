package com.stage.elearning.model.token;


import com.stage.elearning.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken {

    @SequenceGenerator(
            name = "refresh_token_sequence",
            sequenceName = "refresh_token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "refresh_token_sequence"
    )
    @Column(name = "id" , unique = true , nullable = false)
    private long id;

    @Column(name = "issued_at", nullable = false)
    private Date issuedAt;

    @Column(name = "expires_at", nullable = false)
    private  Date expiresAt;

    @Column(name = "expired",nullable = false)
    private boolean expired;

    @Column(name = "revoked",nullable = false)
    private boolean  revoked;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    private UserEntity userEntity;


}
