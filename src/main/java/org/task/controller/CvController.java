package org.task.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.task.cache.JobStatus;
import org.task.cache.JobsCache;
import org.task.dto.JobInfo;
import org.task.service.FileProcessingService;

/**
 * Controller for handling CVs.
 */
@RestController
public class CvController {

    private final JobsCache jobsCache;
    private final FileProcessingService fileProcessingService;

    public CvController(JobsCache jobsCache, FileProcessingService fileProcessingService) {
        this.jobsCache = jobsCache;
        this.fileProcessingService = fileProcessingService;
    }

    /**
     * @param file to be processed.
     * @return job id assotiated with the request.
     */
    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String submit(@RequestParam("file") MultipartFile file) throws IOException {
        return fileProcessingService.submitFile(file.getBytes());
    }

    /**
     * @param file to be processed in bytes.
     * @return job id associated with the request.
     */
    //NOTE: This doesn't work if we send DOC files, but works for PDF
    //might be bug in extraction service
    @PostMapping(value = "/submit/bytes", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String submitBytes(@RequestBody byte[] file) {
        return fileProcessingService.submitFile(file);
    }

    @GetMapping("/retrieve/{processId}")
    public Object retrieve(@PathVariable String processId) {
        Optional<JobInfo> job = jobsCache.getJob(String.valueOf(processId));
        return job.map(jobInfo -> {
            switch (jobInfo.getStatus()) {
            case PROGRESS:
                return JobStatus.PROGRESS.name();
            case FINISHED_FAILURE:
                return JobStatus.FINISHED_FAILURE.name();
            case FINISHED_SUCCESS:
                //if job is finished we always have the result in that case
                return jobInfo.getResult().get();
            default:
                return null;
            }
        }).orElse(String.format("No job with processId = %s exist", processId));
    }


}
