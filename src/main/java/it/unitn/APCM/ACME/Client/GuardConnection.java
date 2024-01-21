package it.unitn.APCM.ACME.Client;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;

import it.unitn.APCM.ACME.Client.ClientCommon.ClientResponse;
import it.unitn.APCM.ACME.Client.ClientCommon.JSONToArray;



public class GuardConnection {
    public String httpRequest(String url) {
        String response = null;
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();

        try {
            con.setRequestMethod("GET");
            int code = con.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response = inputLine;
                }
                in.close();
            }
        } catch (IOException e) {
			throw new RuntimeException(e);
		}

		return response;
    }

    public ClientResponse httpRequestOpen(String url) {
        ClientResponse response = new ClientResponse();
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();

        try {
            con.setRequestMethod("GET");
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response = (new JSONToArray()).convertToClientResponse(inputLine);
                }
                in.close();
            } else {
                response = null;
            }
        } catch (IOException e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }

        return response;
    }

    public String http_request_saveFile(String url, String content) {
        String response = "error";
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();

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
                    response = inputLine;
                }
                in.close();
            }
        } catch (IOException e) {
			throw new RuntimeException(e);
		}

		return response;
    }
}
