package rsoi.lab2.model;

import java.sql.Timestamp;
import java.util.UUID;

public class UserInfo {

    private int idUser;

    private String firstName;

    private String secondName;

    private String birthDate;

    private String login;

    private String password;

    private UUID uid;

    private UUID token;

    private UUID refreshToken;

    private Timestamp dttmCurrentToken;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public UUID getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(UUID refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Timestamp getDttmCurrentToken() {
        return dttmCurrentToken;
    }

    public void setDttmCurrentToken(Timestamp dttmCurrentToken) {
        this.dttmCurrentToken = dttmCurrentToken;
    }
}
