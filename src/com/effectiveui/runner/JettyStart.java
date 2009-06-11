package com.effectiveui.runner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

/*
 * Based on blog post by Peter Thomas:
 * http://ptrthomas.wordpress.com/2009/01/24/how-to-start-and-stop-jetty-revisited/
 */

public class JettyStart {
	
	public static final int MONITOR_PORT = 8122;
	
	private static Server server; 
	
	public static void main(String[] args) throws Exception {
		
		String contextPath = args[0];
		String warPath = args[1];
		
		String port = "8080";
		
		if (args.length >= 3) {
			port = args[2];
		}
		
		server = new Server();  
		SocketConnector connector = new SocketConnector();  
		connector.setPort(Integer.parseInt(port));  
		server.setConnectors(new Connector[] { connector });  
		WebAppContext context = new WebAppContext();  
		context.setServer(server);  
		context.setContextPath(contextPath);  
		context.setWar(warPath);  
		server.addHandler(context);  
		Thread monitor = new MonitorThread();  
		monitor.start();  
		server.start();  
		server.join();  
	}
	
	private static class MonitorThread extends Thread {

	    private ServerSocket socket;

	    public MonitorThread() {
	        setDaemon(true);
	        setName("StopMonitor");
	        try {
	            socket = new ServerSocket(MONITOR_PORT, 1, InetAddress.getByName("127.0.0.1"));
	        } catch(Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

	    @Override
	    public void run() {
	        System.out.println("*** running jetty 'stop' thread");
	        Socket accept;
	        try {
	            accept = socket.accept();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
	            reader.readLine();
	            System.out.println("*** stopping jetty embedded server");
	            server.stop();
	            accept.close();
	            socket.close();
	        } catch(Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

	}	
	
}
