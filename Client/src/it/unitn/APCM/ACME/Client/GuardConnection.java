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

    public ArrayList<String> http_request2(String url) {
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
                
                Object o = con.getContent();
                //System.out.println(o.toString());
                ClientResponse cl =  new ClientResponse();
                
                /*JSONObject obj = new JSONObject(o);
                String pageName = obj.getJSONObject("pageInfo").getString("pageName");
        
                System.out.println(pageName);
        
                JSONArray arr = obj.getJSONArray("posts");
                for (int i = 0; i < arr.length(); i++) {
                    String post_id = arr.getJSONObject(i).getString("post_id");
                    System.out.println(post_id);
                }*/

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
}


