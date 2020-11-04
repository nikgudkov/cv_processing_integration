package org.task.dto;

import java.util.Optional;

import org.task.cache.JobStatus;
import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class JobInfo {

    @NonNull
    JobStatus status;
    Object result;

    public Optional<Object> getResult() {
        return Optional.ofNullable(result);
    }

}
