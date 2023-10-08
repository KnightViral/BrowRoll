package sample.services;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Base64;
import java.util.Collection;

public class HttpHelper {

    private final static String AUTHORIZATION = "Authorization";
    private final static int TIMEOUT_MILLIS = 10000;


    public static Header generateAuthorizationHeader(String login, String password) {
        return new BasicHeader(AUTHORIZATION, "Basic " + new String(Base64.getEncoder().encode(String.format("%s:%s", login, password).getBytes())));
    }

    public static Header createHeader(String name, String value) {
        return new BasicHeader(name, value);
    }


    public static boolean responseCheck(HttpResponse response, int status) {
        return response != null && response.getStatusLine().getStatusCode() == status;
    }


    private static RequestConfig createConfig(int timeoutMillis) {
        return RequestConfig.custom()
                .setConnectTimeout(timeoutMillis)
                .setConnectionRequestTimeout(timeoutMillis)
                .setSocketTimeout(timeoutMillis)
                .build();
    }

    public static HttpResponse executeGetRequest(String url, Collection<Header> headers)
            throws IOException {
        url = url.replaceAll(" ", "%20");
        try {
            HttpUriRequest request = new HttpGet(url);

            if (headers != null) {
                headers.forEach(request::addHeader);
            }
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            httpClientBuilder.setDefaultRequestConfig(createConfig(TIMEOUT_MILLIS));
            HttpClient httpClient = httpClientBuilder.build();
            return httpClient.execute(request);
        } catch (ConnectTimeoutException | SocketTimeoutException ex) {
            System.out.println("Http get request timed out");
            throw new IOException();
        } catch (Exception ex) {
            System.out.println("Http get request error");
            return null;
        }
    }

    public static HttpResponse executePostRequest(String url, String entity, Collection<Header> headers) {
        try {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(entity, "UTF-8");
            if (headers != null) {
                headers.forEach(request::addHeader);
            }
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            httpClientBuilder.setDefaultRequestConfig(createConfig(TIMEOUT_MILLIS));
            HttpClient httpClient = httpClientBuilder.build();
            request.setEntity(params);
            return httpClient.execute(request);
        } catch (Exception ex) {
            System.out.println("Http post request error");
            return null;
        }
    }
}