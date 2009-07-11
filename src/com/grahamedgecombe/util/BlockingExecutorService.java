package com.grahamedgecombe.util;

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

public class BlockingExecutorService implements ExecutorService {
	
	private ExecutorService service;
	private BlockingQueue<Future<?>> pendingTasks = new LinkedBlockingQueue<Future<?>>();
	
	public BlockingExecutorService(ExecutorService service) {
		this.service = service;
	}
	
	public void waitForPendingTasks() throws ExecutionException {
		while(pendingTasks.size() > 0) {
			if(isShutdown()) {
				return;
			}
			try {
				pendingTasks.take().get();
			} catch(InterruptedException e) {
				continue;
			}
		}
	}

	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return service.awaitTermination(timeout, unit);
	}

	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		List<Future<T>> futures = service.invokeAll(tasks);
		for(Future<?> future : futures) {
			pendingTasks.add(future);
		}
		return futures;
	}

	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
		List<Future<T>> futures = service.invokeAll(tasks, timeout, unit);
		for(Future<?> future : futures) {
			pendingTasks.add(future);
		}
		return futures;
	}

	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return service.invokeAny(tasks);
	}

	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return service.invokeAny(tasks, timeout, unit);
	}

	public boolean isShutdown() {
		return service.isShutdown();
	}

	public boolean isTerminated() {
		return service.isTerminated();
	}

	public void shutdown() {
		service.shutdown();
	}

	public List<Runnable> shutdownNow() {
		return service.shutdownNow();
	}

	public <T> Future<T> submit(Callable<T> task) {
		Future<T> future = service.submit(task);
		pendingTasks.add(future);
		return future;
	}

	public Future<?> submit(Runnable task) {
		Future<?> future = service.submit(task);
		pendingTasks.add(future);
		return future;
	}

	public <T> Future<T> submit(Runnable task, T result) {
		Future<T> future = service.submit(task, result);
		pendingTasks.add(future);
		return future;
	}

	public void execute(Runnable command) {
		service.execute(command);
	}

}

