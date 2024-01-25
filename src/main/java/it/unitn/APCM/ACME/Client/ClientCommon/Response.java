package it.unitn.APCM.ACME.Client.ClientCommon;

//Class used to return a response for GuardConnection class to Editor dial
public class Response {
    int status; // Status related to the response => 0: OK, 1: INTERNAL_ERROR, 2: UNAUTHORIZED
    Object response; // other response info which can be string or clientResponse in our case

    public Response() {
        status = 1;
        response = null;
    }

    public Response(int status, Object response) {
        this.status = status;
        this.response = response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public Object getResponse() {
        return response;
    }
}
