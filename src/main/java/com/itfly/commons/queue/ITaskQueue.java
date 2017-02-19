package com.itfly.commons.queue;

/**
 * Created by zhoufeiyu on 19/02/2017.
 */
public interface ITaskQueue<T> {

    boolean start();

    boolean stop();

    void feed(T task);
}
