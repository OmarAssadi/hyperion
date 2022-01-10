package org.hyperion.rs2;

import org.hyperion.rs2.model.World;
import org.hyperion.rs2.task.Task;
import org.hyperion.util.BlockingExecutorService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * The 'core' class of the server which processes all the logic tasks in one
 * single logic <code>ExecutorService</code>. This service is scheduled which
 * means <code>Event</code>s are also submitted to it.
 *
 * @author Graham Edgecombe
 */
public class GameEngine implements Runnable {

    /**
     * A queue of pending tasks.
     */
    private final BlockingQueue<Task> tasks = new LinkedBlockingQueue<>();

    /**
     * The logic service.
     */
    private final ScheduledExecutorService logicService = Executors.newScheduledThreadPool(1);

    /**
     * The task service, used by <code>ParallelTask</code>s.
     */
    private final BlockingExecutorService taskService = new BlockingExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

    /**
     * The work service, generally for file I/O and other blocking operations.
     */
    private final ExecutorService workService = Executors.newSingleThreadExecutor();

    /**
     * Running flag.
     */
    private boolean running = false;

    /**
     * Thread instance.
     */
    private Thread thread;

    /**
     * Submits a new task which is processed on the logic thread as soon as
     * possible.
     *
     * @param task The task to submit.
     */
    public void pushTask(final Task task) {
        tasks.offer(task);
    }

    /**
     * Checks if this <code>GameEngine</code> is running.
     *
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Starts the <code>GameEngine</code>'s thread.
     */
    public void start() {
        if (running) {
            throw new IllegalStateException("The engine is already running.");
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Stops the <code>GameEngine</code>'s thread.
     */
    public void stop() {
        if (!running) {
            throw new IllegalStateException("The engine is already stopped.");
        }
        running = false;
        thread.interrupt();
    }

    @Override
    public void run() {
        try {
            while (running) {
                try {
                    final Task task = tasks.take();
                    submitLogic(() -> task.execute(GameEngine.this));
                } catch (final InterruptedException ignored) {
                }
            }
        } finally {
            logicService.shutdown();
            taskService.shutdown();
            workService.shutdown();
        }
    }

    /**
     * Submits a task to run in the logic service.
     *
     * @param runnable The runnable.
     */
    public void submitLogic(final Runnable runnable) {
        logicService.submit(() -> {
            try {
                runnable.run();
            } catch (final Throwable t) {
                World.getWorld().handleError(t);
            }
        });
    }

    /**
     * Schedules a task to run in the logic service.
     *
     * @param runnable The runnable.
     * @param delay    The delay.
     * @param unit     The time unit.
     * @return The <code>ScheduledFuture</code> of the scheduled logic.
     */
    public ScheduledFuture<?> scheduleLogic(final Runnable runnable, final long delay, final TimeUnit unit) {
        return logicService.schedule(() -> {
            try {
                runnable.run();
            } catch (final Throwable t) {
                World.getWorld().handleError(t);
            }
        }, delay, unit);
    }

    /**
     * Submits a task to run in the parallel task service.
     *
     * @param runnable The runnable.
     */
    public void submitTask(final Runnable runnable) {
        taskService.submit(() -> {
            try {
                runnable.run();
            } catch (final Throwable t) {
                World.getWorld().handleError(t);
            }
        });
    }

    /**
     * Submits a task to run in the work service.
     *
     * @param runnable The runnable.
     */
    public void submitWork(final Runnable runnable) {
        workService.submit(() -> {
            try {
                runnable.run();
            } catch (final Throwable t) {
                World.getWorld().handleError(t);
            }
        });
    }

    /**
     * Waits for pending parallel tasks.
     *
     * @throws ExecutionException If an error occurred during a task.
     */
    public void waitForPendingParallelTasks() throws ExecutionException {
        taskService.waitForPendingTasks();
    }

}
