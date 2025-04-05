package it.andrea.start.models.user;

import java.time.LocalDateTime;

import it.andrea.start.constants.TokenType;
import it.andrea.start.models.FirstBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "token_user", indexes = { @Index(name = "IDX_USER_ID", columnList = "user_id"), @Index(name = "IDX_TOKEN_USER", columnList = "id, user_id, token_type") })
public class TokenUser extends FirstBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOKEN_SEQ")
    @SequenceGenerator(sequenceName = "token_seq", initialValue = 1, allocationSize = 1, name = "TOKEN_SEQ")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, name = "token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime tokenExpire;

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

    public TokenType getTokenType() {
	return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
	this.tokenType = tokenType;
    }

    public LocalDateTime getTokenExpire() {
	return tokenExpire;
    }

    public void setTokenExpire(LocalDateTime tokenExpire) {
	this.tokenExpire = tokenExpire;
    }

}
