package org.task.client.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public class ExecutorFactory {

    /**
     * Creates a thread pool that reuses a fixed number of threads operating off a shared queue.
     *
     * @param corePoolSize the number of threads in the pool.
     * @param maximumPoolSize capacity of the underlying work queue to hold tasks before they are executed.
     * @param keepAliveTime when the number of threads is greater than the core, this is the maximum time that excess idle threads
     *                      will wait for new tasks before terminating. <b>In milliseconds.</b>
     */
    public ExecutorService newFixedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(maximumPoolSize);
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue);
    }

}
