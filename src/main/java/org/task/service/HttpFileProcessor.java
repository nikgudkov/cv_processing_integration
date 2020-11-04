package org.task.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.task.client.common.HttpClient;
import org.task.dto.IntegrationServiceProperties;
import org.task.exception.FileSaveException;

/**
 *
 * Service for sendings files for parsing over http.
 */
@Service
public class HttpFileProcessor implements FileProcessor {

    private final String UPLOADED_FILE_PARAMETER = "uploaded_file";
    private final String ACCOUNT_PARAMETER = "account";
    private final String USERNAME_PARAMETER = "username";
    private final String PASSWORD_PARAMETER = "password";
    private final int RANDOM_STRING_LENGTH = 10;

    private final String ENDPOINT = "/extract.do";

    private final IntegrationServiceProperties integrationServiceProperties;
    private final HttpClient httpClient;

    public HttpFileProcessor(IntegrationServiceProperties integrationServiceProperties, HttpClient httpClient) {
        this.integrationServiceProperties = integrationServiceProperties;
        this.httpClient = httpClient;
    }

    public ResponseEntity<String> processFile(byte[] file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Path tempFile;
        try {
            String randomName = RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
            tempFile = Files.createTempFile(randomName, "");
            Files.write(tempFile, file);
        } catch (IOException e) {
            throw new FileSaveException("Uploaded file wasn't saved", e);
        }

        body.add(UPLOADED_FILE_PARAMETER, new FileSystemResource(tempFile.toFile()));
        body.add(ACCOUNT_PARAMETER, integrationServiceProperties.getAccount());
        body.add(USERNAME_PARAMETER, integrationServiceProperties.getUsername());
        body.add(PASSWORD_PARAMETER, integrationServiceProperties.getPassword());

        return httpClient.sendPost(body, headers, integrationServiceProperties.getServerUrl() + ENDPOINT);
    }


}
