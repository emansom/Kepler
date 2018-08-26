package org.alexdev.kepler.util.schedule;

import java.util.concurrent.Future;

public abstract class FutureRunnable implements Runnable {
    private Future<?> future;

    public Future<?> getFuture() {
        return future;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }
}