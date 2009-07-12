package com.grahamedgecombe.rs2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.firewall.ConnectionThrottleFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.grahamedgecombe.rs2.model.World;

public class Server {
	
	private static final int PORT = 43594;
	
	public static void main(String[] args) {
		try {
			new Server().bind(PORT).start();
		} catch(IOException e) {
			logger.severe("Error while starting server : " + e.getMessage());
		}
	}
	
	private static final Logger logger = Logger.getLogger(Server.class.getName());
	private final IoAcceptor acceptor = new NioSocketAcceptor();
	private final GameEngine engine = new GameEngine();
	
	public Server() {
		logger.info("Starting...");
		World.getWorld().init(engine);
		acceptor.setHandler(new ConnectionHandler());
		acceptor.getFilterChain().addFirst("throttleFilter", new ConnectionThrottleFilter());
	}
	
	public Server bind(int port) throws IOException {
		logger.info("Binding to port : " + port);
		acceptor.bind(new InetSocketAddress(port));
		return this;
	}
	
	public void start() {
		engine.start();
		logger.info("Ready");
	}

}
