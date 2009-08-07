package org.hyperion.rs2.net.ondemand;

import java.util.concurrent.BlockingQueue;

/**
 * <p>A class which waits for ondemand requests to queue up and then processes
 * them.</p>
 * @author Graham
 *
 */
public class OnDemandWorker implements Runnable {
	
	/**
	 * The array of request queues.
	 */
	private BlockingQueue<OnDemandRequest>[] queues;

	/**
	 * Creates the ondemand worker.
	 * @param queues The array of request queues.
	 */
	public OnDemandWorker(BlockingQueue<OnDemandRequest>[] queues) {
		this.queues = queues;
	}

	@Override
	public void run() {
		while(true) {
			for(BlockingQueue<OnDemandRequest> activeQueue : queues) {
				OnDemandRequest request;
				while((request = activeQueue.poll()) != null) {
					request.service();
				}
			}
			synchronized(OnDemandPool.getOnDemandPool()) {
				try {
					OnDemandPool.getOnDemandPool().wait();
				} catch(InterruptedException e) {
					continue;
				}
			}
		}
	}

}
