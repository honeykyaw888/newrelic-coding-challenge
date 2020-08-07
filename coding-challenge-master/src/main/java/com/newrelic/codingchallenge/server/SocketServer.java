package com.newrelic.codingchallenge.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketServer {
	protected Selector selector;
	protected int port;
	protected String hostName;
	protected boolean blockingMode;
	public static Logger logger = LoggerFactory.getLogger(SocketServer.class);
	long startTime = 0;
	long endTime = 0;

	public SocketServer(int port, String hostName, boolean blockingMode) {
		this.port = port;
		this.hostName = hostName;
		this.blockingMode = blockingMode;
	}

	protected ServerSocketChannel setupServerSocket() throws IOException, ClosedChannelException {
		selector = Selector.open();
		ServerSocketChannel socket = ServerSocketChannel.open();
		InetSocketAddress serverAddress = new InetSocketAddress(this.hostName, this.port);
		socket.bind(serverAddress);
		socket.configureBlocking(blockingMode);// Adjusts this channel's blocking mode.
		SelectionKey selectKy = socket.register(selector, socket.validOps(), null);
		return socket;
	}

}
