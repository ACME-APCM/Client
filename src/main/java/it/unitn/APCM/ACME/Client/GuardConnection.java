package it.unitn.APCM.ACME.Client;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import it.unitn.APCM.ACME.Client.ClientCommon.ClientResponse;
import it.unitn.APCM.ACME.Client.ClientCommon.JSONToArray;
import it.unitn.APCM.ACME.Client.ClientCommon.Response;

public class GuardConnection {

    //Method to login
    public boolean httpRequestLogin(String url, String email, String password, User user) {
        //Create a secure connection with the guard to login
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();

        try {
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setDoOutput(true);

            // Create a JSON object for credentials
            JSONObject credentialsJson = new JSONObject();
            try {
                credentialsJson.put("email", email);
                credentialsJson.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        
            // Convert the JSON object to a string
            String jsonInputString = credentialsJson.toString();

            // Write the JSON string to the output stream
            try (OutputStream os = con.getOutputStream()) {
                os.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK
                    || con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                //Analize the response from the guard
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("success")) {
                        user.setEmail(email);
                        user.setAuthenticated(true);
                        user.setJwt(con.getHeaderField("jwt"));
                        return true;
                    }
                }
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;

    }

    //Method to request the list of all the files
    public Response httpRequestFile(String url, User user) {
        Response response = new Response();
        //Create a secure connection with the guard
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();
        //Set the jwt token required
        con.setRequestProperty("jwt", user.getJwt());

        try {
            con.setRequestMethod("GET"); //Sending request
            //Analyze response code
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //if OK analyze the response
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                response.setStatus(0);

                //Get the list of all files
                while ((inputLine = in.readLine()) != null) {
                    response.setResponse(inputLine);
                }
                in.close();
            } else if (con.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // if jwt token is invalid or expired, set the related status
                response.setStatus(2);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    //Method to open a specific file
    public Response httpRequestOpen(String url, String jwt) {
        Response res = new Response();
        ClientResponse response = new ClientResponse();
        // Create a secure connection with the Guard
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();
        //Set the jwt token required to open the file
        con.setRequestProperty("jwt", jwt);

        try {
            con.setRequestMethod("GET");
            //Analyze the response code
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                
                // if ok get the response, parse it, and the status with the realted clientResponse object
                while ((inputLine = in.readLine()) != null) {
                    response = (new JSONToArray()).convertToClientResponse(inputLine);
                    res.setStatus(0);
                    res.setResponse(response);
                }
                in.close();
            } else if(con.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED){
                // if jwt token is invalid or expired, set the related status
                res.setStatus(2);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return res;
    }

    //Method to create a new file
    public Response httpRequestCreate(String url, String jwt) {
        //Create a secure connection with the guard
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();
        Response res = new Response();
        //Set the required jwt
        con.setRequestProperty("jwt", jwt);

        try {
            con.setRequestMethod("GET");
            // Analyze the response code
            if (con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                
                //if ok; analyze the response
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("success")) {
                        res.setStatus(0);
                    }
                }
                in.close();
            } else if(con.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED){
                // if jwt token is invalid or expired, set the related status
                res.setStatus(2);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return res;
    }

    // MEthod to save a file
    public Response httpRequestSave(String url, String content, String jwt) {
        //Create a secure connection with the Guard
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();
        Response res = new Response();
        // Send the required jwt
        con.setRequestProperty("jwt", jwt);

        try {
            //Send the new text in the body
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                if (content == null) {
                    throw new NullPointerException();
                }
                byte[] input = content.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            } catch (IOException | NullPointerException e) {
                throw new RuntimeException(e);
            }

            //Analyze the reponse code
            if (con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                //if ok, analyze and set the response
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("success")) {
                        res.setStatus(0);
                    }
                }
                in.close();
            }  else if(con.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED){
                // if jwt token is invalid or expired, set the related status
                res.setStatus(2);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return res;
    }
}
