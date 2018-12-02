package rsoi.lab2.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idUser;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "secondName")
    private String secondName;
    @Column(name = "birthDate")
    private String birthDate;
    @Column(name = "password")
    private String password;
    @Column(name = "login")
    private String login;
    @Column
    private UUID uid;

    public User() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
