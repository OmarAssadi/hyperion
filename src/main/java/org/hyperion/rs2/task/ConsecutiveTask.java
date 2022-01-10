package org.hyperion.rs2.task;

import org.hyperion.rs2.GameEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A task which executes a group of tasks in a guaranteed sequence.
 *
 * @author Graham Edgecombe
 */
public class ConsecutiveTask implements Task {

    /**
     * The tasks.
     */
    private final Collection<Task> tasks;

    /**
     * Creates the consecutive task.
     *
     * @param tasks The child tasks to execute.
     */
    public ConsecutiveTask(final Task... tasks) {
        final List<Task> taskList = new ArrayList<>(Arrays.asList(tasks));
        this.tasks = Collections.unmodifiableCollection(taskList);
    }

    @Override
    public void execute(final GameEngine context) {
        for (final Task task : tasks) {
            task.execute(context);
        }
    }

}
