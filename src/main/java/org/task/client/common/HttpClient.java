package org.task.client.common;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpClient {

    private final RestTemplate restTemplate;

    public HttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> sendPost(MultiValueMap<String, Object> body, HttpHeaders headers, String serverUrl) {
        HttpEntity<MultiValueMap<String, Object>> requestEntity
            = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(serverUrl, requestEntity, String.class);
    }


}
