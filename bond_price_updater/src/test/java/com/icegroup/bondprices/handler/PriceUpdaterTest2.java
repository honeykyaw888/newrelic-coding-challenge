package com.icegroup.bondprices.handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

@RunWith(JUnit4.class)
public class PriceUpdaterTest2 extends TestCase {
	private PriceUpdater priceUpdater;
	private static final Logger LOG = LoggerFactory.getLogger("PriceUpdaterTest");
	private String filePath = "./src/files/CUSIP.txt";
	private final int interval = 500;

	@Before
	public void setUp() throws Exception {

		priceUpdater = new PriceUpdater(filePath, interval);
		LOG.info("startup - starting writing thread");

	}

	@Test
	public void testPrintLine() {
		String bondName1 = "S282909M";
		String bondName2 = "39010294";
		priceUpdater.printLine(bondName1);
	}

}
