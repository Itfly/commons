package com.itfly.commons.queue;

import com.itfly.commons.thread.CancellableThread;
import com.itfly.commons.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

/**
 * Created by zhoufeiyu on 19/02/2017.
 */
public abstract class TaskQueue<T> implements ITaskQueue<T> {

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    public static final int DEFAULT_WORKER_COUNT = 10;
    public static final int DEFAULT_CAPACITY = 0;  // 0 present queue size is unlimited

    protected BlockingQueue<T> queue;

    protected int workerCount = DEFAULT_WORKER_COUNT;
    protected int capacity = DEFAULT_CAPACITY;

    /**
     * if retry enabled, consume failed message will requeue to the queue
     */
    protected boolean retryEnabled = true;

    protected List<WorkerThread> threads;

    /*********************************************
     *
     *  API
     *
     ********************************************/
    protected abstract void consume(T task);


    @Override
    public void feed(T task) {
        queue.add(task);
    }

    @Override
    public boolean start() {
        if (threads != null) {
            LOG.warn("Threads have already started.");
            return false;
        }

        LOG.info("Creating workerThreads, worker Count : " + workerCount);
        threads = new ArrayList<>(workerCount);

        IntStream.range(0, workerCount).forEach(i -> {
            WorkerThread thread = new WorkerThread(i + 1);
            thread.start();
            threads.add(thread);
            LOG.info(thread.getName() + " started");
        });

        return true;
    }

    @Override
    public boolean stop() {
        if (threads == null) {
            LOG.warn("threads == null while stop() called");
            return false;
        }

        threads.forEach(thread -> {
            thread.cancel(false);
            LOG.info("Cancelling " + thread.getName());
        });
        threads = null;

        return true;
    }

    /*********************************************
     *
     *  INIT
     *
     ********************************************/

    protected void init() {
        if (capacity <= 0) {
            queue = new LinkedBlockingQueue<>();
        } else {
            queue = new ArrayBlockingQueue<>(capacity);
        }
    }

    /*********************************************
     *
     *  Inner Class
     *
     ********************************************/

    protected class WorkerThread extends CancellableThread {
        private final int id;

        public WorkerThread(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (!cancelled) {
                try {
                    T task = queue.take();
                    try {
                        consume(task);
                    } catch (Exception e) {
                        LOG.error("Consume task error.", e);
                        if (retryEnabled) {
                            boolean refeed = queue.offer(task);
                            LOG.error("Re-feed task to the queue: " + (refeed ? "succeed" : "failed"));
                        }
                    }
                } catch (Exception ee) {
                    if (cancelled) {
                        break;
                    }
                    LOG.error("", ee);
                    ThreadUtils.sleepQuietly(1000L);
                }
            }
        }

        @Override
        protected void setName() {
            String threadName = WorkerThread.this.getClass().getSimpleName() + "-worker-" + id;
            super.setName(threadName);
        }
    }

    /*********************************************
     *
     *  Getter & Setter
     *
     ********************************************/

    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isRetryEnabled() {
        return retryEnabled;
    }

    public void setRetryEnabled(boolean retryEnabled) {
        this.retryEnabled = retryEnabled;
    }
}
