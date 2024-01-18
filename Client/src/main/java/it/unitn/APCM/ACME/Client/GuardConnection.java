package it.unitn.APCM.ACME.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import it.unitn.APCM.ACME.Client.ClientCommon.ClientResponse;
import it.unitn.APCM.ACME.Client.ClientCommon.JSONToArray;

public class GuardConnection {

    private final static String guard_url = "http://localhost:8090/api/v1/";

    public ArrayList<String> http_request(String url) {
        ArrayList<String> response = new ArrayList<>();
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
                    response.add(inputLine);
                    //System.out.println(inputLine);
                }
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public ClientResponse http_requestOpen(String url) {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public ArrayList<String> http_request_saveFile(String url, String content) {
        ArrayList<String> response = new ArrayList<>();
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
                byte[] input = content.getBytes("utf-8");
                os.write(input, 0, input.length);           
            }

            if (con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.add(inputLine);
                    //System.out.println(inputLine);
                }
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public ArrayList<String> http_request_newFile(String url, String content) {
        ArrayList<String> response = new ArrayList<>();
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
                byte[] input = content.getBytes("utf-8");
                os.write(input, 0, input.length);           
            }

            if (con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.add(inputLine);
                    //System.out.println(inputLine);
                }
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}


