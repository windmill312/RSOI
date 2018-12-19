package rsoi.lab2.entity;

import rsoi.lab2.model.TokenType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tokens"/*,
        uniqueConstraints=
        @UniqueConstraint(columnNames={"serviceUuid", "userId", "tokenType"})*/)
public class Token implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private UUID serviceUuid;

    @Column
    private String value;

    @Enumerated(EnumType.STRING)
    @Column
    private TokenType tokenType;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
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
}
