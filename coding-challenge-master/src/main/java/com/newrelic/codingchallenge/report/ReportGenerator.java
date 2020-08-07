package com.newrelic.codingchallenge.report;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportGenerator implements Runnable {
	public static Logger logger = LoggerFactory.getLogger(ReportGenerator.class);

	private Set<String> msg;
	AtomicInteger counter = null;

	public ReportGenerator(Set<String> m, AtomicInteger counter) {
		this.msg = m;
		this.counter = counter;
	}

	private long total;

	// Received 50 unique numbers, 2 duplicates. Unique total: 567231
	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		try {
			int uniqueCount = msg.size();
			long incomingCount = counter.getAndSet(0);
			total += uniqueCount;
			long duplicates = incomingCount - uniqueCount;
			Set<String> copySet = new HashSet(msg);
			msg.removeAll(msg);
			writeToFile(copySet);
			logger.info("Received " + uniqueCount + " unique numbers, " + duplicates + ". Unique total: " + total);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeToFile(Set<String> set) {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("numbers.log"))) {
			for (String str : set)
				writer.write(str + System.lineSeparator());
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

}