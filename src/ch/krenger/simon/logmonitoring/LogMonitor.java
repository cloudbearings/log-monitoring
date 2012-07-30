/**
 * 
 */
package ch.krenger.simon.logmonitoring;

import ch.krenger.simon.logmonitoring.util.GetOpt;
import ch.krenger.simon.logmonitoring.util.SMTPMailer;

/**
 * Program to monitor a log file and send an e-mail when a specified regex is
 * matched. The program takes multiple arguments to specify the monitored log
 * file, the used SMTP server, the sender and recipient of the e-mail and the
 * regex that is used to check all lines.
 * 
 * @author simon
 * @date 30.07.2012
 * 
 */
public class LogMonitor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		GetOpt go = new GetOpt(args, "um:i:s:r:h:p:f:");

		/**
		 * String to contain the file to be monitored
		 */
		String filename = null;
		
		/**
		 * String to contain the regex
		 */
		String regex = null;

		/**
		 * SMTP server
		 */
		String mailhost = null;
		
		/**
		 * SMTP daemon port
		 */
		int mailport = 25;
		
		/**
		 * Sender of the e-mail
		 */
		String mailsender = null;
		
		/**
		 * Recipient of the e-mail
		 */
		String mailrecipient = null;

		int ch = -1;
		while ((ch = go.getopt()) != GetOpt.optEOF) {
			switch (ch) {
			case 'i':
				// Filename (input)
				filename = go.optArgGet();
				break;
			case 's':
				// Sender
				mailsender = go.optArgGet();
				break;
			case 'r':
				// Recipient
				mailrecipient = go.optArgGet();
				break;
			case 'm':
				// Regex to match
				regex = go.optArgGet();
				break;
			case 'h':
				// Host
				mailhost = go.optArgGet();
				break;
			case 'p':
				// Mail port
				mailport = Integer.parseInt(go.optArgGet());
				break;
			case 'u':
				usage();
				return;
			default:
				System.err.println("Unknown switch: '" + ch + "', ignoring");
				usage();
				break;
			}
		}

		if (filename == null) {
			System.err
					.println("Input file was not set (-i switch), aborting...");
			usage();
			return;
		}

		if (regex == null) {
			System.err
					.println("Regex to match as not set (-m switch), aborting...");
			usage();
			return;
		}

		if (mailhost == null) {
			System.err.println("SMTP server not set (-h switch), aborting...");
			usage();
			return;
		}

		if (mailsender == null) {
			System.err
					.println("Sender for E-Mail not set (-s switch), aborting...");
			usage();
			return;
		}

		if (mailrecipient == null) {
			System.err.println("Recipient not set (-r switch), aborting...");
			usage();
			return;
		}

		SMTPMailer sm = new SMTPMailer(mailhost, mailport);

		Tailer t = new Tailer(filename, regex, sm, mailsender, mailrecipient);
		t.startTailing();
		System.out.println("Program started");
	}

	public static void usage() {
		System.out
				.println("JAVA LOG MONITORING by Simon Krenger");
		System.out.println();
		System.out
				.println("Usage: -i <file> -m <regex> -h <server> -s <sender> -r <recipient> [-p <port> -u]");
		System.out.println("  -i\t Input file (required)");
		System.out
				.println("  -m\t Each new line of the file will be compared to this regex (required)");
		System.out.println("  -h\t SMTP mail server to use (required)");
		System.out.println("  -s\t Sender e-mail address (required)");
		System.out.println("  -r\t Recipient e-mail address (required)");
		System.out.println("  -p\t SMTP port to usage (optional)");
		System.out.println("  -u\t Prints this usage message");
		System.out.println();
	}
}
