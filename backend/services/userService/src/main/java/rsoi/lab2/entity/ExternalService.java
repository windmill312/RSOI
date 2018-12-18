package rsoi.lab2.entity;

import org.apache.commons.lang.RandomStringUtils;

import javax.persistence.*;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "services")
public class ExternalService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 300)
    private String name;

    @Column(length = 2048)
    private String secretKey;

    @Column(length = 60)
    private UUID uuid;

    public ExternalService() {

    }

    public ExternalService(String name) {
        this.name = name;
        int length = 15;
        String generatedString = RandomStringUtils.random(length, true, true);
        this.secretKey = generatedString;
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
