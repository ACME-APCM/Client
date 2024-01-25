package it.unitn.APCM.ACME.Client.ClientCommon;

public class Response {
    int status;
    Object response;

    public Response(){
        status = 1; //0: OK, 1: INTERNAL:ERROR, 2: UNAUTHORIZED
        response = null;
    }

    public Response(int status, Object response){
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
