package com.newrelic.codingchallenge.server;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newrelic.codingchallenge.client.SocketClient;

public class SocketLimitSemaphore {
	public static Logger logger = LoggerFactory.getLogger(SocketClient.class);
	private final ExecutorService executor;
	private final Semaphore semaphore;

	public SocketLimitSemaphore(ExecutorService executor, int limit) {
		this.executor = executor;
		this.semaphore = new Semaphore(limit);
	}

	public <T> Future<T> submit(final Callable<T> task) throws InterruptedException {

		semaphore.acquire();

		return executor.submit(() -> {
			try {
				return task.call();
			} finally {
				logger.info(
						"Acquired a permit from semaphore. We have " + semaphore.availablePermits() + " permit left!");
			}
		});

	}

	public <T> Future<T> release(final Callable<T> task) throws InterruptedException {

		return executor.submit(() -> {
			try {
				return task.call();
			} finally {
				semaphore.release();
				logger.info(
						"Relesed a permit from semaphore. We have " + semaphore.availablePermits() + " permit left!");
			}
		});

	}

}