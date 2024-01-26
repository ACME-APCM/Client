package it.unitn.APCM.ACME.Client;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.unitn.APCM.ACME.Client.ClientCommon.ClientResponse;
import it.unitn.APCM.ACME.Client.ClientCommon.JSONToArray;
import it.unitn.APCM.ACME.Client.ClientCommon.Response;

/**
 * The type Guard connection.
 */
public class GuardConnection {

    /**
     * Http request login boolean used to requst a login to the guard.
     *
     * @param url      the url
     * @param email    the email
     * @param password the password
     * @param user     the user
     * @return the boolean
     */
    public boolean httpRequestLogin(String url, String email, String password, User user) {
        // Create a secure connection with the guard to login
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();

        try {
            // Set the request method and the content type
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setDoOutput(true);

            // Create a JSON object for credentials
            JSONObject credentials = new JSONObject();
            try {
                credentials.put("email", email);
                credentials.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Convert the JSON object to a string
            String jsonInputString = credentials.toString();

            // Write the JSON string to the output stream
            try (OutputStream os = con.getOutputStream()) {
                os.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                return false;
            }

            // Analyze the response code
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // if OK analyze the response
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                // Analize the response from the guard
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
            return false;
        }

        return false;
    }

    /**
     * Http request file used to request the list of all the files.
     *
     * @param url  the url
     * @param user the user
     * @return the response
     */
    public Response httpRequestFile(String url, User user) {
        Response response = new Response();
        // Create a secure connection with the guard
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();
        // Set the jwt token required
        con.setRequestProperty("jwt", user.getJwt());

        try {
            con.setRequestMethod("GET"); // Sending request
            // Analyze response code
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // if OK analyze the response
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                response.setStatus(0);
                ArrayList<String> fileList = new ArrayList<>();

                // Get the list of all files
                while ((inputLine = in.readLine()) != null) {

                    // Parse the response as a JSON object
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(inputLine);

                    // Extract the "files" string
                    String filesString = jsonNode.get("files").asText();

                    // Parse the "files" string as a JSON array
                    JsonNode filesNode = objectMapper.readTree(filesString);

                    // Convert the filesNode to a List<String>
                    for (JsonNode element : filesNode) {
                        fileList.add(element.asText());
                    }
                }
                // Set the response
                response.setResponse(fileList);
                in.close();
            } else if (con.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // if jwt token is invalid or expired, set the related status
                response.setStatus(2);
            }
        } catch (IOException e) {
            // return empty response which is by default error
            return new Response();
        }

        return response;
    }

    /**
     * Http request open used to open a specific file.
     *
     * @param url the url
     * @param jwt the jwt
     * @return the response
     */
    public Response httpRequestOpen(String url, String jwt) {
        Response res = new Response();
        ClientResponse response = new ClientResponse();
        // Create a secure connection with the Guard
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();
        // Set the jwt token required to open the file
        con.setRequestProperty("jwt", jwt);

        try {
            con.setRequestMethod("GET");
            // Analyze the response code
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                // if OK get the response, parse it, and set the status with the realted
                // clientResponse object
                while ((inputLine = in.readLine()) != null) {
                    response = (new JSONToArray()).convertToClientResponse(inputLine);
                    res.setStatus(0);
                    res.setResponse(response);
                }
                in.close();
            } else if (con.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // if jwt token is invalid or expired, set the related status
                res.setStatus(2);
            }
        } catch (IOException e) {
            // return empty response which is by default error
            return new Response();
        }

        return res;
    }

    /**
     * Http request create used to create a new file.
     *
     * @param url the url
     * @param jwt the jwt
     * @return the response
     */
    public Response httpRequestCreate(String url, String jwt) {
        Response res = new Response();

        // Create a secure connection with the guard
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();
        // Set the required jwt
        con.setRequestProperty("jwt", jwt);

        try {
            con.setRequestMethod("GET");
            // Analyze the response code
            if (con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                // if CREATED analyze the response
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("success")) {
                        res.setStatus(0);
                    }
                }
                in.close();
            } else if (con.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // if jwt token is invalid or expired, set the related status
                res.setStatus(2);
            }
        } catch (IOException e) {
            // return empty response which is by default error
            return new Response();
        }

        return res;
    }

    /**
     * Http request save used to save a file.
     *
     * @param url     the url
     * @param content the content
     * @param jwt     the jwt
     * @return the response
     */
    public Response httpRequestSave(String url, String content, String jwt) {
        Response res = new Response();

        // Create a secure connection with the Guard
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();
        // Send the required jwt
        con.setRequestProperty("jwt", jwt);

        try {
            // Send the new text in the body
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            con.setDoOutput(true);
            // Write the new text in the output stream
            try (OutputStream os = con.getOutputStream()) {
                if (content == null) {
                    throw new NullPointerException();
                }
                byte[] input = content.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            } catch (IOException | NullPointerException e) {
                // return empty response which is by default error
                return new Response();
            }

            // Analyze the reponse code
            if (con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                // if CREATED analyze and set the response
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("success")) {
                        res.setStatus(0);
                    }
                }
                in.close();
            } else if (con.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // if jwt token is invalid or expired, set the related status
                res.setStatus(2);
            }
        } catch (IOException e) {
            // return empty response which is by default error
            return new Response();
        }

        return res;
    }
}
