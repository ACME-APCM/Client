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

    public boolean httpRequestLogin(String url, String email, String password, User user) {

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

    public Response httpRequestFile(String url, User user) {
        Response response = new Response();
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();

        con.setRequestProperty("jwt", user.getJwt());

        try {
            con.setRequestMethod("GET"); //Sending request
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                response.setStatus(0);

                while ((inputLine = in.readLine()) != null) {
                    response.setResponse(inputLine);
                }
                in.close();
            } else if (con.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                response.setStatus(2);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    public Response httpRequestOpen(String url, String jwt) {
        Response res = new Response();
        ClientResponse response = new ClientResponse();
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();

        con.setRequestProperty("jwt", jwt);

        try {
            con.setRequestMethod("GET");
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                
                while ((inputLine = in.readLine()) != null) {
                    response = (new JSONToArray()).convertToClientResponse(inputLine);
                    res.setStatus(0);
                    res.setResponse(response);
                }
                in.close();
            } else if(con.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED){
                res.setStatus(2);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return res;
    }

    public Response httpRequestCreate(String url, String jwt) {

        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();
        Response res = new Response();
        con.setRequestProperty("jwt", jwt);

        try {
            con.setRequestMethod("GET");
            if (con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("success")) {
                        res.setStatus(0);
                    }
                }
                in.close();
            } else if(con.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED){
                res.setStatus(2);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return res;
    }

    public Response httpRequestSave(String url, String content, String jwt) {

        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();
        Response res = new Response();
        con.setRequestProperty("jwt", jwt);

        try {
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

            if (con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("success")) {
                        res.setStatus(0);
                    }
                }
                in.close();
            }  else if(con.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED){
                res.setStatus(2);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return res;
    }
}
