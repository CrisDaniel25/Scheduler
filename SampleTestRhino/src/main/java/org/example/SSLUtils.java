package org.example;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;

public class SSLUtils {

    public static void disableSSLVerification() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCertificates = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }
        };

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCertificates, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Skip hostname verification
        HostnameVerifier allHostsValid = (hostname, session) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}
