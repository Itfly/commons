package com.itfly.commons.thread;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhoufeiyu on 05/01/2017.
 */
public class CancellableThread extends Thread {

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    protected volatile boolean cancelled = false;

    public CancellableThread() {
        super();
        setName();
    }

    public CancellableThread(Runnable runnable) {
        super(runnable);
        setName();
    }

    public boolean cancel() {
        return cancel(true);
    }

    public boolean cancel(boolean wait) {
        State state = super.getState();
        if (state == State.NEW) {
            throw new IllegalStateException("Thread is not start");
        } else if (state == State.TERMINATED) {
            // thread has been terminated
            LOG.warn("Thread " + getName() + " has been terminated");
            return false;
        }

        cancelled = true;
        this.interrupt();
        if (wait) {
            try {
                super.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }

    protected void setName() {
        String threadName = getClass().getSimpleName();
        if (StringUtils.isNotBlank(threadName)) {
            super.setName(threadName);
        }
    }
}
