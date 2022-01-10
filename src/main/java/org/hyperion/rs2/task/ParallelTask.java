package org.hyperion.rs2.task;

import org.hyperion.rs2.GameEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A task which can execute multiple child tasks simultaneously.
 *
 * @author Graham Edgecombe
 */
public class ParallelTask implements Task {

    /**
     * The child tasks.
     */
    private final Collection<Task> tasks;

    /**
     * Creates the parallel task.
     *
     * @param tasks The child tasks.
     */
    public ParallelTask(final Task... tasks) {
        final List<Task> taskList = new ArrayList<>(Arrays.asList(tasks));
        this.tasks = Collections.unmodifiableCollection(taskList);
    }

    @Override
    public void execute(final GameEngine context) {
        for (final Task task : tasks) {
            context.submitTask(() -> task.execute(context));
        }
        try {
            context.waitForPendingParallelTasks();
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
