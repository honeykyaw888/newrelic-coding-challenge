package com.newrelic.codingchallenge.utils;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newrelic.codingchallenge.Main;

public class MessageProcessor implements Runnable {

	protected BlockingQueue<String> queue = null;
	protected Set<String> msg = null;
	protected AtomicInteger counter = null;
	public static Logger logger = LoggerFactory.getLogger(Main.class);

	public MessageProcessor(BlockingQueue<String> queue, Set<String> msg, AtomicInteger counter) {
		this.queue = queue;
		this.msg = msg;
		this.counter = counter;
	}

	public void run() {
		try {

			logger.info("Running in message processor");
			while (true) {
				msg.add(queue.take());
				counter.incrementAndGet();
			}

		} catch (InterruptedException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}
}