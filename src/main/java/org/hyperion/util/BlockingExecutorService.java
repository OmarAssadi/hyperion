package org.hyperion.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * An <code>ExecutorService</code> that waits for all its events to finish
 * executing.
 *
 * @author Graham Edgecombe
 */
public class BlockingExecutorService implements ExecutorService {

    /**
     * The service backing this service.
     */
    private final ExecutorService service;

    /**
     * A list of pending tasks.
     */
    private final BlockingQueue<Future<?>> pendingTasks = new LinkedBlockingQueue<>();

    /**
     * Creates the executor service.
     *
     * @param service The service backing this service.
     */
    public BlockingExecutorService(final ExecutorService service) {
        this.service = service;
    }

    /**
     * Waits for pending tasks to complete.
     *
     * @throws ExecutionException if an error in a task occurred.
     */
    public void waitForPendingTasks() throws ExecutionException {
        while (pendingTasks.size() > 0) {
            if (isShutdown()) {
                return;
            }
            try {
                pendingTasks.take().get();
            } catch (final InterruptedException ignored) {
            }
        }
    }

    /**
     * Gets the number of pending tasks.
     *
     * @return The number of pending tasks.
     */
    public int getPendingTaskAmount() {
        return pendingTasks.size();
    }

    @Override
    public void shutdown() {
        service.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return service.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return service.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return service.isTerminated();
    }

    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        return service.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        final Future<T> future = service.submit(task);
        pendingTasks.add(future);
        return future;
    }

    @Override
    public <T> Future<T> submit(final Runnable task, final T result) {
        final Future<T> future = service.submit(task, result);
        pendingTasks.add(future);
        return future;
    }

    @Override
    public Future<?> submit(final Runnable task) {
        final Future<?> future = service.submit(task);
        pendingTasks.add(future);
        return future;
    }

    @Override
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
        final List<Future<T>> futures = service.invokeAll(tasks);
        pendingTasks.addAll(futures);
        return futures;
    }

    @Override
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException {
        final List<Future<T>> futures = service.invokeAll(tasks, timeout, unit);
        pendingTasks.addAll(futures);
        return futures;
    }

    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return service.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return service.invokeAny(tasks, timeout, unit);
    }

    @Override
    public void execute(final Runnable command) {
        service.execute(command);
    }

}

