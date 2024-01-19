package it.unitn.APCM.ACME.Client.ClientCommon;

public class ClientResponse {
    String path;
    boolean auth;
    boolean w_mode;
    String text;

    /*
     * SETTER
     */

    public void set_path(String path) {this.path = path;}

    public void set_auth(boolean auth) {this.auth = auth;}

    public void set_text(String text) {this.text = text;}

    public void set_w_mode(boolean w_mode) {this.w_mode = w_mode;}

    /*
     * GETTER
     */

    public String get_path() {return this.path;}

    public String get_text() {return this.text;}

    public boolean get_auth() {return this.auth;}

    public boolean get_w_mode() {return this.w_mode;}
}