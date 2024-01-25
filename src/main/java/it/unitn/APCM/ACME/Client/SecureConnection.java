package it.unitn.APCM.ACME.Client;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

//Class used to create a secure connection (mTLS v1.3) with the Guard
public class SecureConnection {
	private final static String guard_url = String.format("https://%s/api/v1/", System.getenv("GUARD_URL"));
	private final static String kstore_pw = System.getenv("KEYSTORE_PASSWORD");
	private final static String k_pw = System.getenv("KEY_PASSWORD");
	private static SSLContext sc = null;
	// Connection create at each new SecureConnection()
	private HttpsURLConnection secure_con;

	public SecureConnection(String url) {
		//System.setProperty("javax.net.debug", "all");
		String request_url = guard_url + url;

		if (sc == null) {
			try {
				if (guard_url == null || kstore_pw == null || k_pw == null) {
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

				// Instantiate SecureContext
				sc = SSLContext.getInstance("TLSv1.3");
				sc.init(new KeyManager[]{x509km}, new TrustManager[]{x509tm}, new java.security.SecureRandom());
			} catch (IOException | UnrecoverableKeyException | CertificateException | NoSuchAlgorithmException |
					 KeyStoreException | KeyManagementException e) {
				throw new RuntimeException(e);
			}
		}

		try	{
			URL httpsURL = new URL(request_url);
			HttpURLConnection con = (HttpURLConnection) httpsURL.openConnection();
			if (con instanceof HttpsURLConnection) {
				this.secure_con = (HttpsURLConnection) con;
				this.secure_con.setSSLSocketFactory(sc.getSocketFactory());
				this.secure_con.setRequestProperty("User-Agent", "ACME-Client");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected HttpsURLConnection getSecure_con() {
		return secure_con;
	}
}
