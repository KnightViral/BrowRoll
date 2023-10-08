package sample.services;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class DonationAlertsConnection {
    private static final int CLIENT_ID = 11554;
    private static final String REDIRECT_URL = "http://localhost:5001";
    private static final String OAUTH_URL = "https://www.donationalerts.com/oauth/authorize";

    public static final String BROWSER_LINK = "https://www.donationalerts.com/oauth/authorize?client_id=11554&redirect_uri=http://localhost:5001&response_type=token&scope=oauth-user-show%20oauth-donation-subscribe";
    private static final String USER_URL = "https://www.donationalerts.com/api/v1/user/oauth";
    private static final String SUBSCRIBE_URL = "https://www.donationalerts.com/api/v1/centrifuge/subscribe";
    private static final String CENTRIFUGO_CONNECTION_ENDPOINT = "wss://centrifugo.donationalerts.com/connection/websocket";
    private static final String CHANNEL = "$alerts:donation_";

    private final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint();

    private String bearerToken;
    private String socketConnectionToken;
    private int userId = 0;
    private String socketClientId;
    private String channelLink;
    private String channelToken;

    public void addMessageHandler(WebsocketClientEndpoint.MessageHandler handler) {
        clientEndPoint.addMessageHandler(handler);
    }

    public boolean connect(String urlString) {
        try {

            bearerToken = getBearerToken(urlString);
            if (!updateSocketToken()) {
                return false;
            }
            channelLink = CHANNEL + userId;
            WebsocketClientEndpoint.MessageHandler socketClientMessageHandler = this::handleSocketClientRequest;
            clientEndPoint.connect(getURI());
            callForSocketClientId(clientEndPoint, socketClientMessageHandler);
            Thread.sleep(2000);
            clientEndPoint.removeMessageHandlers(socketClientMessageHandler);
            if (!updateChannelToken()) {
                return false;
            }
            clientEndPoint.addMessageHandler(this::handleMessage);
            clientEndPoint.sendMessage(String.format("{\"params\": {\"channel\": \"%s\",\"token\": \"%s\"},\"method\": 1,\"id\": %s}", channelLink, channelToken, userId));
            return true;
        } catch (InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getBearerToken(String urlString) {
        Map<String, String> query_pairs = splitQuery(urlString);
        if (query_pairs.containsKey("access_token")) {
            return query_pairs.get("access_token");
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void handleSocketClientRequest(String message) {
        try {
            System.out.println(message);
            socketClientId = new JSONObject(message).getJSONObject("result").getString("client");
        } catch (Exception e) {
            System.out.println("Failed to parse message.");
        }
    }

    public void handleMessage(String message) {
        try {
            System.out.println(message);
            JSONObject json = new JSONObject(message);
            JSONObject data = json.getJSONObject("result").getJSONObject("data").getJSONObject("data");
            System.out.printf("%s\n%s : %s\n%s%n",
                    data.getString("username"),
                    data.getDouble("amount"),
                    data.getString("currency"),
                    data.getString("message"));
        } catch (Exception e) {
            System.out.println("Failed to parse message.");
        }
    }

    private boolean updateSocketToken() {
        if (bearerToken == null) {
            return false;
        }
        try {
            HttpResponse response =
                    HttpHelper.executeGetRequest(USER_URL,
                            Collections.singletonList(HttpHelper.createHeader("Authorization", "Bearer " + bearerToken)));
            if (HttpHelper.responseCheck(response, HttpStatus.SC_OK)) {
                JSONObject responseJsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
                socketConnectionToken = responseJsonObject.getJSONObject("data").getString("socket_connection_token");
                userId = responseJsonObject.getJSONObject("data").getInt("id");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void callForSocketClientId(WebsocketClientEndpoint clientEndPoint, WebsocketClientEndpoint.MessageHandler messageHandler) {
        clientEndPoint.addMessageHandler(messageHandler);
        clientEndPoint.sendMessage(String.format("{\"params\": {\"token\": \"%s\"},\"id\": %s}", socketConnectionToken, userId));
    }

    private Boolean updateChannelToken() {
        if (channelLink == null || socketClientId == null) {
            return false;
        }
        try {
            HttpResponse response = HttpHelper.executePostRequest(SUBSCRIBE_URL,
                    String.format("{\"channels\":[\"%s\"], \"client\":\"%s\"}", channelLink, socketClientId),
                    Arrays.asList(HttpHelper.createHeader("Authorization", "Bearer " + bearerToken),
                            HttpHelper.createHeader("Content-Type", "application/json")));
            if (HttpHelper.responseCheck(response, HttpStatus.SC_OK)) {
                JSONObject responseJsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
                for (Object channel : responseJsonObject.getJSONArray("channels")) {
                    JSONObject channelJson = (JSONObject) channel;
                    if (channelJson.getString("channel").equals(channelLink)) {
                        channelToken = channelJson.getString("token");
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Map<String, String> splitQuery(String url) {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        try {
            String query = new URL(url).getRef();
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8.toString()),
                        URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8.toString()));
            }
        } catch (MalformedURLException | UnsupportedEncodingException | NullPointerException e) {
            e.printStackTrace();
        }
        return query_pairs;
    }

    private URI getURI() {
        try {
            return new URI(CENTRIFUGO_CONNECTION_ENDPOINT);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
