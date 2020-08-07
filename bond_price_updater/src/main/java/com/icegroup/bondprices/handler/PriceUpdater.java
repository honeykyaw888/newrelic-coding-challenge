package com.icegroup.bondprices.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PriceUpdater implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger("PriceUpdater");
	private boolean debug = true;

	private int runEveryNSeconds = 1000;// wait for 1 second to read again from file if there is no new lines
	private long lastKnownPosition = 0; // last line read in file
	private boolean shouldIRun = true; // true at first and will become false when the program is asked to shutdown
	private File priceFile = null; // file to read from
	private static int counter = 0; // number of lines read since the program started
	private RandomAccessFile readWriteFileAccess;
	private Map<String, String> priceData = new HashMap(); // to hold price data
	private String lastBondName;

	public PriceUpdater(String myFile, int myInterval) {
		priceFile = new File(myFile);
		this.lastKnownPosition = myFile.length();// this will avoid loading the whole file and we are only
													// interested about the latest prices
		this.runEveryNSeconds = myInterval;
		try {
			this.readWriteFileAccess = new RandomAccessFile(priceFile, "r");
		} catch (FileNotFoundException e) {
			LOG.error("Price file cannot be found!");
		}
	}

	public RandomAccessFile getReadWriteFileAccess() {
		return readWriteFileAccess;
	}

	protected void printLine(String message) {
		if (isNumeric(message)) { // we have a price
			if (lastBondName != null)
				priceData.put(lastBondName, message);
		} else if (message.length() == 8) { // we have a CUSIP
			lastBondName = message;
		}
		if (priceData.isEmpty())
			return;
		StringBuilder logLine = new StringBuilder();
		logLine.append("Format[Symbol: price] ");
		/* assuming we need to print all CUSIP and price pair at every update */
		for (Map.Entry<String, String> entry : priceData.entrySet()) {
			logLine.append(entry.getKey() + ":" + entry.getValue() + "\t");
		}
		LOG.info(logLine.toString());
	}

	public void stopRunning() {
		shouldIRun = false;
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		double d = 0.1;
		try {
			d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return d % 1 != 0;
	}

	public void run() {
		LOG.info("Tailing file");
		try {
			while (shouldIRun) {

				Thread.sleep(runEveryNSeconds);
				long fileLength = priceFile.length();
				if (fileLength > lastKnownPosition) {

					readWriteFileAccess.seek(lastKnownPosition);
					String line = null;
					while ((line = readWriteFileAccess.readLine()) != null) {
						this.printLine(line);
						counter++;
					}
					lastKnownPosition = readWriteFileAccess.getFilePointer();

				} else {
					if (debug)
						LOG.debug("Couldn't found new line after line # " + (lastKnownPosition + counter));
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			stopRunning();
		} finally {
			try {
				if (readWriteFileAccess != null)
					readWriteFileAccess.close();
			} catch (IOException e) {
				LOG.error(e.getMessage());
			}
		}
		if (debug)
			this.printLine("Exit the program...");
	}

}