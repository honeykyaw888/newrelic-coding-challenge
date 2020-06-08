package com.icegroup.bondprices.handler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icegroup.bondprices.handler.PriceUpdater;

import junit.framework.TestCase;

@RunWith(JUnit4.class)
public class PriceUpdaterTest extends TestCase {
	private PriceUpdater priceUpdater;
	private static final Logger LOG = LoggerFactory.getLogger("PriceUpdaterTest");
	private String filePath = "./src/files/CUSIP.txt";
	private final int interval = 500;
	private ExecutorService executor;
	private boolean shouldIRun = true;
	private PriceAppender priceAppender;
	private String[] CUSIP = { "02039D02", "20DM2000", "F9200102", "01010012", "0NN90012" };

	@Before
	public void setUp() throws Exception {

		priceUpdater = new PriceUpdater(filePath, interval);
		priceAppender = new PriceAppender();
		LOG.info("startup - starting writing thread");

	}

	@After
	public void tearDown() throws Exception {
		LOG.info("Closing file connection");
		shouldIRun = false;
	}

	public class PriceAppender implements Runnable {

		public void run() {
			LOG.info("Starting to append price");
			appendData(filePath, shouldIRun, interval);
		}

		private void appendData(String filePath, boolean shouldIRun, int runEveryNSeconds) {
			FileWriter fileWritter;

			try {
				while (shouldIRun) {
					int randomNum = ThreadLocalRandom.current().nextInt(1, 10);
					Thread.sleep(runEveryNSeconds);
					fileWritter = new FileWriter(filePath, true);
					BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
					bufferWritter.write("\n" + CUSIP[randomNum % (CUSIP.length - 1)]);
					for (int i = 0; i < randomNum; i++) {
						double data = ThreadLocalRandom.current().nextDouble(1, 200);
						bufferWritter.write("\n" + Math.round(data * 100.0) / 100.0);
					}
					bufferWritter.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Test
	public void readerTest() throws InterruptedException {
		LOG.info("readerTest");
		executor = Executors.newFixedThreadPool(4);
		executor.execute(priceUpdater);
		executor.execute(priceAppender);

		Thread.sleep(3600 * 2l);// letting it run for 2 minutes
	}

}
