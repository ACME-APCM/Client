package it.unitn.APCM.ACME.Client;

public class User {

    private String email;
    private String password;
    private boolean authenticated;
    private String jwt;

    public User() {
        this.email = "";
        this.password = "";
        this.authenticated = false;
        this.jwt = "";
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getJwt() {
        return jwt;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
