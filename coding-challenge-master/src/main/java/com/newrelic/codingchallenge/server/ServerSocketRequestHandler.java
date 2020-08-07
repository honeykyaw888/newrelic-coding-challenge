package com.newrelic.codingchallenge.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class ServerSocketRequestHandler extends SocketServer {
	private SocketLimitSemaphore connection;
	private ExecutorService executorService = Executors.newCachedThreadPool();
	private ServerSocketChannel socket;
	private boolean channelOpen;
	protected BlockingQueue<String> queue = null;

	public ServerSocketRequestHandler(int port, String hostName, boolean blockingMode, int connectionCount,
			BlockingQueue<String> queue) {
		super(port, hostName, blockingMode);
		connection = new SocketLimitSemaphore(executorService, connectionCount);
		this.queue = queue;

	}

	public void openSocket() {
		try {
			socket = setupServerSocket();

			while (true) {
				// logger.info("Server is waiting for new connection and buffer select...");
				selector.select();// Selects a set of keys whose corresponding channels are ready for I/O
				Set<SelectionKey> selectionKeys = selector.selectedKeys();// token representing the registration of a
																			// SelectableChannel with a Selector
				Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
				while (selectionKeyIterator.hasNext()) {
					SelectionKey selectionKey = selectionKeyIterator.next();
					if (selectionKey.isAcceptable()) {// Tests whether this key's channel is ready to accept a new
						initializeClient(socket);
					} else if (selectionKey.isReadable()) {// Tests whether this key's channel is ready for reading
						if (channelOpen)
							streamData(selectionKey);
						else
							return;
					}
					selectionKeyIterator.remove();
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

	}

	protected void initializeClient(ServerSocketChannel socket)
			throws IOException, ClosedChannelException, InterruptedException, ExecutionException {
		SocketChannel clientChannel = processIncomingConnection(socket);
		if (clientChannel == null)
			return;
		// Adjusts this channel's blocking mode to false
		clientChannel.configureBlocking(blockingMode);
		// Operation-set bit for read operations
		clientChannel.register(selector, SelectionKey.OP_READ);
		logger.info("Connection Accepted: " + clientChannel.getRemoteAddress() + "\n");
	}

	public SocketChannel processIncomingConnection(ServerSocketChannel socket)
			throws InterruptedException, IOException, ExecutionException {
		Future<SocketChannel> future = connection.submit(() -> {

			return socket.accept();
		});
		SocketChannel channel = future.get();
		channelOpen = true;
		return channel;
	}

	protected void streamData(SelectionKey theKey) throws IOException, InterruptedException, ExecutionException {
		SocketChannel clientChannel = (SocketChannel) theKey.channel();

		ByteBuffer readBuffer = ByteBuffer.allocate(1000);
		clientChannel.read(readBuffer);
		String result = new String(readBuffer.array()).trim();
		String[] lines = result.split("\\r?\\n");

		for (String input : lines) {
			// logger.debug("Message received: " + input);
			queue.add(input);
			if (input.equals("terminate")) {
				logger.info("client info:" + clientChannel.getRemoteAddress());
				Future<Boolean> task = connection.release(() -> {
					if (clientChannel.isOpen()) {
						clientChannel.close();
						return true;
					}
					return false;
				});
				channelOpen = false;
				logger.info("It's time to close connection as we got last command 'terminate'");
				endTime = System.currentTimeMillis();
				long totalTime = endTime - startTime;
				logger.info("time taken - " + totalTime);

				break;
			}
		}

	}
}
