package rsoi.lab2.entity;

import rsoi.lab2.model.TokenType;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tokens")
public class Token {

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
}
