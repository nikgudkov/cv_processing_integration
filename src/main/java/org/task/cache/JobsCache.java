package org.task.cache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import org.task.dto.JobInfo;

@Component
public class JobsCache {

    // <JOB_ID , JOB_DATA> mapping
    //TODO need to set TTL to prevent memory leak
    private final Map<String, JobInfo> jobs = new ConcurrentHashMap<>();

    public Optional<JobInfo> getJob(String jobId) {
        return Optional.ofNullable(jobs.get(jobId));
    }

    public void putJob(String jobId, JobInfo jobInfo) {
        jobs.put(jobId, jobInfo);
    }

}
