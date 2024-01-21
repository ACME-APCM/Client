package it.unitn.APCM.ACME.Client;

import java.lang.System;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.net.HttpURLConnection;
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.CertificateException;

import it.unitn.APCM.ACME.Client.ClientCommon.ClientResponse;
import it.unitn.APCM.ACME.Client.ClientCommon.JSONToArray;

public class GuardConnection {

    private final static String guard_url = "https://localhost:8090/api/v1/";
    private final static String kstore_pw = System.getenv("KEYSTORE_PASSWORD");
    private final static String k_pw = System.getenv("KEY_PASSWORD");

    public String httpRequest(String url) {
        //System.setProperty("javax.net.debug", "all");
        String response = "";
        HttpsURLConnection con = (new SecureConnection(url)).getSecure_con();

        try {
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "ACME");
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

    public ClientResponse httpRequestOpen(String url) {
        ClientResponse response = new ClientResponse();
        String request_url = guard_url + url;
        URL obj;

        try {
            obj = new URL(request_url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            if (con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
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
            e.printStackTrace();
        }

        return response;
    }

    public String http_request_saveFile(String url, String content) {
        String response = "error";
        String request_url = guard_url + url;
        URL obj;

        try {
            obj = new URL(request_url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = content.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
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
            e.printStackTrace();
        }

        return response;
    }
}
