package org.task.service;

import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class FileProcessingService {

    private final FileProcessor fileProcessor;
    private final JobsHandlerService jobsHandlerService;
    private final ExecutorService extractionServiceExecutor;

    public FileProcessingService(@Qualifier("extractionServiceExecutor") ExecutorService extractionServiceExecutor, FileProcessor fileProcessor,
        JobsHandlerService jobsHandlerService) {
        this.extractionServiceExecutor = extractionServiceExecutor;
        this.fileProcessor = fileProcessor;
        this.jobsHandlerService = jobsHandlerService;
    }

    /**
     * Process the file in a job.
     * @param file to be parsed in bytes.
     * @return id of the associated job.
     */
    public String submitFile(byte[] file) {
        return jobsHandlerService.submitJob(() -> fileProcessor.processFile(file), extractionServiceExecutor);
    }

}
