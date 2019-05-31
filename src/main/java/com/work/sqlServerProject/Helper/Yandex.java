package com.work.sqlServerProject.Helper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Created by a.shcherbakov on 07.05.2019.
 */
public class Yandex {
    public static String news() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    /*
     * end of the fix
     */

        URL url = new URL("https://www.yandex.ru/");
        //URLConnection con = url.openConnection();
        Connection connection = Jsoup.connect(String.valueOf(url));
        Document document = connection.get();
        System.out.println(document.title());
        Element element = document.getElementById("wd-_topnews");
        Element news = element.getElementById("tabnews_newsc");
        Elements elements = news.getElementsByTag("a");
        StringBuilder res = new StringBuilder();
        for (Element e : elements){
            res.append(e);
            //System.out.println(e.text());
        }
        Connection connection1 = Jsoup.connect("https://yandex.ru/pogoda/moscow");
        Document pogoda = connection1.get();
        Elements elements1 = pogoda.getElementsByClass("temp fact__temp fact__temp_size_s");/*pogoda.getElementsByClass("fact__temp-wrap");*/
        res.append("<br><p>Погода в Москве:</p>"+elements1);
        return res.toString();
    }
}
