package org.task.service;

import org.springframework.http.ResponseEntity;

/**
 * General interface for file processing classes. Could be internal or remote.
 */
//TODO for simplification it returns ResponseEntity, ideally it should be generic of <T>
public interface FileProcessor {

    /**
     * Send file for processing and get job id associated to it.
     * @param file to be processed.
     *
     * @return parsed data.
     */
    ResponseEntity<String> processFile(byte[] file);

}
