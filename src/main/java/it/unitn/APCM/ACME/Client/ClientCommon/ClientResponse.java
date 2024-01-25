package it.unitn.APCM.ACME.Client.ClientCommon;

//Class that represent the response when a file is opened
public class ClientResponse {
    String path; //path of the file     
    boolean auth; // read permission on the file
    boolean w_mode; //Used to disable the save button if user has no right to write on the file
    String text; //decrypted content of the file

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