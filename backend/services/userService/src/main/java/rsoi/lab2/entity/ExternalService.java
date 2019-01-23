package rsoi.lab2.entity;

import org.apache.commons.lang.RandomStringUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "service")
    private Set<ServiceKey> keys;

    public ExternalService() {

    }

    public ExternalService(String name) {
        this.name = name;
        int length = 15;
        this.secretKey = RandomStringUtils.random(length, true, true);
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

    public Set<ServiceKey> getKeys() {
        return keys;
    }

    public void setKeys(Set<ServiceKey> keys) {
        this.keys = keys;
    }
}
