package rsoi.lab2.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "service_keys")
public class ServiceKey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private UUID value;

    @ManyToOne
    @JoinColumn(name = "userUuid", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "serviceUuid", nullable = false)
    private ExternalService service;

    public ServiceKey() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getValue() {
        return value;
    }

    public void setValue(UUID value) {
        this.value = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ExternalService getService() {
        return service;
    }

    public void setService(ExternalService service) {
        this.service = service;
    }
}
