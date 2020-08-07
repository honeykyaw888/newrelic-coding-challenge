package com.newrelic.codingchallenge;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newrelic.codingchallenge.report.ReportGenerator;
import com.newrelic.codingchallenge.server.ServerSocketRequestHandler;
import com.newrelic.codingchallenge.utils.MessageProcessor;
import com.newrelic.codingchallenge.utils.PropertiesReader;

public class Main {
	public static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws IOException {

		logger.info("Starting up server ....");

		int qSize = PropertiesReader.readPropertyInInteger("queue.size");
		BlockingQueue<String> queue = new ArrayBlockingQueue<String>(qSize == 0 ? 200000 : qSize);
		Set<String> myConcurrentSet = ConcurrentHashMap.newKeySet();
		AtomicInteger incomingMsgCounter = new AtomicInteger(0);

		/* TO READ MESSAGES SENT TO BLOCKINGQUEUE */
		initBackendMessageProcessor(queue, myConcurrentSet, incomingMsgCounter);

		/* A SCHEDULE THREAD TO POPULATE STATUS REPORT */
		scheduleReportingTask(myConcurrentSet, incomingMsgCounter);

		/*
		 * OPEN A SERVER SOCKET TO LISTEN ON INCOMING CLIENT CONNECTIONS AND TRANSFER
		 * MESSAGES TO BLOCKINGQUEUE
		 */
		initSocketServer(queue);
	}

	private static void initSocketServer(BlockingQueue<String> queue) throws IOException {
		int port = PropertiesReader.readPropertyInInteger("server.port");
		int connectionCount = PropertiesReader.readPropertyInInteger("server.connection.count");
		ServerSocketRequestHandler server = new ServerSocketRequestHandler(port == 0 ? 4000 : port, "localhost", false,
				connectionCount == 0 ? 5 : connectionCount, queue);
		server.openSocket();
		Runtime.getRuntime().addShutdownHook(Thread.currentThread());
	}

	private static void scheduleReportingTask(Set<String> myConcurrentSet, AtomicInteger incomingMsgCounter) {
		ReportGenerator report = new ReportGenerator(myConcurrentSet, incomingMsgCounter);
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(report, 10, 10, TimeUnit.SECONDS);
	}

	private static void initBackendMessageProcessor(BlockingQueue<String> queue, Set<String> myConcurrentSet,
			AtomicInteger incomingMsgCounter) {
		MessageProcessor processor = new MessageProcessor(queue, myConcurrentSet, incomingMsgCounter);
		Thread th = new Thread(processor);
		th.setName("Message-Processor");
		th.start();
	}

}