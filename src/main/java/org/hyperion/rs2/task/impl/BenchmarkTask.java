package org.hyperion.rs2.task.impl;

import org.hyperion.rs2.GameEngine;
import org.hyperion.rs2.task.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * A utility class that wraps around another, benchmarking it.
 *
 * @author Graham Edgecombe
 */
public class BenchmarkTask implements Task {

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(BenchmarkTask.class.getName());

    /**
     * Sample count.
     */
    private static final int SAMPLES = 100;

    /**
     * A list of samples.
     */
    private static final List<Long> samples = new LinkedList<>();

    /**
     * The task.
     */
    private final Task task;

    /**
     * Creates the benchmark task.
     *
     * @param task The task to wrap around.
     */
    public BenchmarkTask(final Task task) {
        this.task = task;
    }

    @Override
    public void execute(final GameEngine context) {
        final long start = System.nanoTime();
        task.execute(context);
        final long elapsed = System.nanoTime() - start;
        samples.add(elapsed);
        if (samples.size() >= SAMPLES) {
            long total = 0;
            for (final long sample : samples) {
                total += sample;
            }
            logger.info((((double) total / (double) samples.size() / 1000000D)) + " milliseconds (average over " + samples.size() + " samples)");
            samples.clear();
        }
    }

}
