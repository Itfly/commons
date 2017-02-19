package com.itfly.commons.thread;

/**
 * Created by zhoufeiyu on 06/01/2017.
 */
public abstract class CancellablePeriodThread extends CancellableThread {

    public CancellablePeriodThread() {
        super();
    }

    public CancellablePeriodThread(Runnable runnable) {
        super(runnable);
    }

    @Override
    public final void run() {
        while (!super.cancelled) {
            try {
                doWork();
            } catch (Exception e) {
                LOG.error("", e);
            }

            try {
                Thread.sleep(getPeriod());
            } catch (InterruptedException e) {
            }
        }
    }

    protected abstract long getPeriod();

    protected abstract long doWork();
}
