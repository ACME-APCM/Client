package it.unitn.APCM.ACME.Client;

public class User {

    private String email;
    private boolean authenticated;
    private String jwt;
    private boolean valid_session;

    public User() {
        this.email = "";
        this.authenticated = false;
        this.jwt = "";
        this.valid_session = false;
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

    public boolean isValid_session() {
        return valid_session;
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

    public void setValid_session(boolean valid_session) {
        this.valid_session = valid_session;
    }
}
