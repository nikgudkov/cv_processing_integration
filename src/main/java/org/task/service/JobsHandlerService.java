package org.task.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import org.task.cache.JobStatus;
import org.task.cache.JobsCache;
import org.task.dto.JobInfo;

/**
 * Executes the jobs asynchronously.
 */
@Component
public class JobsHandlerService {

    //TODO should be more appropriate message
    private final String JOB_FAILURE_MESSAGE = "Couldn't complete the request.";
    private final int RANDOM_STRING_LENGTH = 10;

    private final JobsCache jobsCache;

    public JobsHandlerService(JobsCache jobsCache) {
        this.jobsCache = jobsCache;
    }

    //TODo note: this class could be generalized to receive Supplier<T>, but in that case we need to redesign callback

    /**
     * Asynchronously submits the task.
     * @param task to be executed;
     * @return id of the created job.
     */
    public String submitJob(Supplier<ResponseEntity> task, ExecutorService jobsExecutor) {
        String jobId = generateJobId();
        //NOTE: the future object could be used to track existing jobs and cancel them manually or stop jobs creation with same parameters
        CompletableFuture.supplyAsync(task, jobsExecutor).thenApply(
            (responseEntity) -> {
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    updateJob(jobId, JobInfo.of(JobStatus.FINISHED_SUCCESS, responseEntity.getBody()));
                } else if (responseEntity.getStatusCode().isError()) {
                    updateJob(jobId, JobInfo.of(JobStatus.FINISHED_FAILURE, responseEntity.getBody()));
                }
                return responseEntity.getBody();
            }
        ).exceptionally((e) -> {
            updateJob(jobId, JobInfo.of(JobStatus.FINISHED_FAILURE, JOB_FAILURE_MESSAGE));
            return null;
        });
        saveNewJob(jobId);
        return jobId;
    }

    private void updateJob(String jobId, JobInfo jobInfo) {
        jobsCache.putJob(jobId, jobInfo);
    }

    private void saveNewJob(String jobId) {
        jobsCache.putJob(jobId, JobInfo.of(JobStatus.PROGRESS, null));
    }

    private String generateJobId() {
        return RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
    }


}
