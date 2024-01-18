package it.unitn.APCM.ACME.Client;

public class User {

    private String email;
    private String password;
    
    public User (){
        this.email = "";
        this.password = "";
    }

    public String getEmail() {
        return email;
    }

    public String getPassword(){
        return password;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
