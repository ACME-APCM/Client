package it.unitn.APCM.ACME.Client;

public class User {

    private String email;
    private boolean authenticated;
    private String jwt;

    public User() {
        this.email = "";
        this.authenticated = false;
        this.jwt = "";
    }

    public String getEmail() {
        return email;
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

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
