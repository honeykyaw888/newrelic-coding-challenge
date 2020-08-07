package com.newrelic.codingchallenge.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketClient {
	public static Logger logger = LoggerFactory.getLogger(SocketClient.class);

	public int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		InetSocketAddress address = new InetSocketAddress("localhost", 4000);
		SocketChannel clientSocketChannel = SocketChannel.open(address);

		logger.info("Connecting to Server on port 8000...");

		Random rn = new Random();

		// ByteBuffer NEWLINE = ByteBuffer.wrap("\n".getBytes());

		for (int i = 0; i < 100000000; i++) {
			String msg = String.format("%09d", rn.nextInt(1000000000));
			byte[] message = new String(msg + System.lineSeparator()).getBytes();

			ByteBuffer buffer = ByteBuffer.wrap(message);
			clientSocketChannel.write(buffer);
			buffer.clear();
			// NEWLINE.clear();
		}
		byte[] message = new String("terminate" + System.lineSeparator()).getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(message);
		clientSocketChannel.write(buffer);
		buffer.clear();
		clientSocketChannel.close();
	}

}
