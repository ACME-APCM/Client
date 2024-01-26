package it.unitn.APCM.ACME.Client.ClientCommon;

/**
 * The type Response.
 */
//Class used to return a response for GuardConnection class to Editor dial
public class Response {
    /**
     * The Status.
     */
    int status; // Status related to the response => 0: OK, 1: INTERNAL_ERROR, 2: UNAUTHORIZED
    /**
     * The Response.
     */
    Object response; // other response info which can be string or clientResponse in our case

    /**
     * Instantiates a new Response.
     */
    public Response() {
        status = 1;
        response = null;
    }

    /**
     * Instantiates a new Response.
     *
     * @param status   the status
     * @param response the response
     */
    public Response(int status, Object response) {
        this.status = status;
        this.response = response;
    }

    /**
     * Sets response.
     *
     * @param response the response
     */
    public void setResponse(Object response) {
        this.response = response;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Gets response.
     *
     * @return the response
     */
    public Object getResponse() {
        return response;
    }
}
