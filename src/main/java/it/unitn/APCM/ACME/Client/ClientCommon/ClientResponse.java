package it.unitn.APCM.ACME.Client.ClientCommon;

/**
 * The type Client response.
 */
//Class that represent the response when a file is opened
public class ClientResponse {
    /**
     * The Path.
     */
    String path; //path of the file
    /**
     * The Auth.
     */
    boolean auth; // read permission on the file
    /**
     * The W mode.
     */
    boolean w_mode; //Used to disable the save button if user has no right to write on the file
    /**
     * The Text.
     */
    String text; //decrypted content of the file

    /*
     * SETTER
     */

    /**
     * Sets path.
     *
     * @param path the path
     */
    public void set_path(String path) {this.path = path;}

    /**
     * Sets auth.
     *
     * @param auth the auth
     */
    public void set_auth(boolean auth) {this.auth = auth;}

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void set_text(String text) {this.text = text;}

    /**
     * Sets w mode.
     *
     * @param w_mode the w mode
     */
    public void set_w_mode(boolean w_mode) {this.w_mode = w_mode;}

    /*
     * GETTER
     */

    /**
     * Gets path.
     *
     * @return the path
     */
    public String get_path() {return this.path;}

    /**
     * Gets text.
     *
     * @return the text
     */
    public String get_text() {return this.text;}

    /**
     * Gets auth.
     *
     * @return the auth
     */
    public boolean get_auth() {return this.auth;}

    /**
     * Gets w mode.
     *
     * @return the w mode
     */
    public boolean get_w_mode() {return this.w_mode;}
}