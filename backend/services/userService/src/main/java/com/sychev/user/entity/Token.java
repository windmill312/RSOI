package com.sychev.user.entity;

import com.sychev.user.model.TokenType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tokens")
public class Token implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private UUID serviceUuid;

    @Column
    private String value;

    @Column
    private Long dttmCreate;

    @Enumerated(EnumType.STRING)
    @Column
    private TokenType tokenType;

    @ManyToOne
    @JoinColumn(name = "uuid", nullable = false)
    private User user;

    public Token() {
    }


    public UUID getServiceUuid() {
        return serviceUuid;
    }

    public void setServiceUuid(UUID serviceUuid) {
        this.serviceUuid = serviceUuid;
    }

    public Enum getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getDttmCreate() {
        return dttmCreate;
    }

    public void setDttmCreate(Long dttmCreate) {
        this.dttmCreate = dttmCreate;
    }
}
