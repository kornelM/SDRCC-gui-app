package com.myapp.guiapp.communication.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class VideoServerRestClient {

    private static final int SERVER_PORT = 60001;
    private static final String SLASH = "/";
    private static final String COLON = ":";
    private static final String HTTP = "http";

    @Value("${gui.video.videoServerApp.address}")
    private String serverAddress;

    private RestTemplate rest;
    private HttpHeaders headers;
    private HttpStatus status;

    public VideoServerRestClient() {
        this.rest = new RestTemplate();
        this.headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
    }

    public String get(String uri) {
        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
        ResponseEntity<String> responseEntity = rest.exchange(serverAddress + uri, HttpMethod.GET, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public String post(String uri, String json) {
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        ResponseEntity<String> responseEntity = rest.exchange(serverAddress + uri, HttpMethod.POST, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public void put(String uri, String value) {
        HttpEntity<String> requestEntity = new HttpEntity<>(value, headers);
        ResponseEntity<String> responseEntity = rest.exchange(getServerUrl() + uri, HttpMethod.PUT, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    private String getServerUrl() {
        return HTTP + COLON + SLASH + SLASH + serverAddress + COLON + SERVER_PORT;
    }
}
