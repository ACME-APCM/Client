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
        String request_url = guard_url + url;
        URL httpsURL;

        try {
            httpsURL = new URL(request_url);

            if (kstore_pw == null || k_pw == null) {
                throw new NullPointerException();
            }

            // KeyManagerFactory
            KeyStore ks = KeyStore.getInstance("JKS");
            InputStream kstoreStream = ClassLoader.getSystemClassLoader().getResourceAsStream("Client_keystore.jks");
            ks.load(kstoreStream, kstore_pw.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
            kmf.init(ks, k_pw.toCharArray());
            X509ExtendedKeyManager x509km = null;
            for (KeyManager keyManager : kmf.getKeyManagers()) {
                if (keyManager instanceof X509ExtendedKeyManager) {
                    x509km = (X509ExtendedKeyManager) keyManager;
                    break;
                }
            }
            if (x509km == null) throw new NullPointerException();

            // TrustManagerFactory
            KeyStore ts = KeyStore.getInstance("JKS");
            InputStream tstoreStream = ClassLoader.getSystemClassLoader().getResourceAsStream("Client_truststore.jks");
            ts.load(tstoreStream, null);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
            tmf.init(ts);
            X509ExtendedTrustManager x509tm = null;
            for (TrustManager trustManager : tmf.getTrustManagers()) {
                if (trustManager instanceof X509ExtendedTrustManager) {
                    x509tm = (X509ExtendedTrustManager) trustManager;
                    break;
                }
            }
            if (x509tm == null) throw new NullPointerException();

            SSLContext sc = SSLContext.getInstance("TLSv1.3");
            sc.init(new KeyManager[]{x509km}, new TrustManager[]{x509tm}, new java.security.SecureRandom());

            HttpURLConnection con = (HttpURLConnection) httpsURL.openConnection();
            if (con instanceof HttpsURLConnection) {
                ((HttpsURLConnection)con).setSSLSocketFactory(sc.getSocketFactory());
            }


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
        } catch (IOException | UnrecoverableKeyException | CertificateException | NoSuchAlgorithmException |
				 KeyStoreException | KeyManagementException e) {
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
