package it.unitn.APCM.ACME.Client.ClientCommon;

/**
 * The type Client response that represent the response when a file is opened.
 */
public class ClientResponse {
    /**
     * The Path of the file.
     */
    String path; 
    /**
     * The Auth (read permission on the file).
     */
    boolean auth; 
    /**
     * The W mode (Used to disable the save button if user has no right to write on the file).
     */
    boolean w_mode; 
    /**
     * The Text of the response.
     */
    String text;

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
     * Sets text fo the response
     *
     * @param text the text
     */
    public void set_text(String text) {this.text = text;}

    /**
     * Sets the write mode.
     *
     * @param w_mode the write mode
     */
    public void set_w_mode(boolean w_mode) {this.w_mode = w_mode;}

    /**
     * Gets path.
     *
     * @return the path
     */
    public String get_path() {return this.path;}

    /**
     * Gets text fo the response.
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
     * Gets the write mode.
     *
     * @return the write mode
     */
    public boolean get_w_mode() {return this.w_mode;}
}