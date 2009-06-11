package com.effectiveui.runner;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class JettyStop {
	public static void main(String[] args) throws Exception {
	    Socket s = new Socket(InetAddress.getByName("127.0.0.1"), JettyStart.MONITOR_PORT);
	    OutputStream out = s.getOutputStream();
	    System.out.println("*** sending jetty stop request");
	    out.write(("\r\n").getBytes());
	    out.flush();
	    s.close();
	}
}
