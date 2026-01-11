package com.raf.gaminggui.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ApiClient {

    private static final String USER_SERVICE_URL = "http://localhost:8084/users";
    private static final String SESSION_SERVICE_URL = "http://localhost:8084/sessions";
    private static final String NOTIFICATION_SERVICE_URL = "http://localhost:8084/notifications";

    private static String token;
    private static Gson gson;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    public static void setToken(String newToken) {
        token = newToken;
    }

    public static String getToken() {
        return token;
    }

    public static String get(String service, String endpoint) throws IOException {
        String url = getServiceUrl(service) + endpoint;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            if (token != null) {
                request.setHeader("Authorization", "Bearer " + token);
            }

            try (CloseableHttpResponse response = client.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    public static String post(String service, String endpoint, Object body) throws IOException {
        String url = getServiceUrl(service) + endpoint;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            request.setHeader("Content-Type", "application/json");
            if (token != null) {
                request.setHeader("Authorization", "Bearer " + token);
            }

            if (body != null) {
                StringEntity entity = new StringEntity(gson.toJson(body), "UTF-8");
                request.setEntity(entity);
            }

            try (CloseableHttpResponse response = client.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    public static String put(String service, String endpoint, Object body) throws IOException {
        String url = getServiceUrl(service) + endpoint;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(url);
            request.setHeader("Content-Type", "application/json");
            if (token != null) {
                request.setHeader("Authorization", "Bearer " + token);
            }

            if (body != null) {
                StringEntity entity = new StringEntity(gson.toJson(body), "UTF-8");
                request.setEntity(entity);
            }

            try (CloseableHttpResponse response = client.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    public static void delete(String service, String endpoint) throws IOException {
        String url = getServiceUrl(service) + endpoint;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(url);
            if (token != null) {
                request.setHeader("Authorization", "Bearer " + token);
            }

            client.execute(request);
        }
    }

    public static Gson getGson() {
        return gson;
    }

    private static String getServiceUrl(String service) {
        switch (service) {
            case "user": return USER_SERVICE_URL;
            case "session": return SESSION_SERVICE_URL;
            case "notification": return NOTIFICATION_SERVICE_URL;
            default: throw new IllegalArgumentException("Unknown service: " + service);
        }
    }
}