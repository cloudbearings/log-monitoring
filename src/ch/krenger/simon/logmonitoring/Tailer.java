package ch.krenger.simon.logmonitoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import ch.krenger.simon.logmonitoring.util.SMTPMailer;

/**
 * Class to "tail" a log file and send an e-mail on match
 * 
 * @author simon
 * 
 */
public class Tailer {

	/**
	 * Constant to hold the subject line
	 */
	private static String SUBJECT_LINE = "Log supervision: Match found";

	/**
	 * Filename
	 */
	private String fname = null;

	/**
	 * Thread that is used for the tailing
	 */
	private Thread t = null;

	/**
	 * Regex pattern used to match each line
	 */
	private Pattern p = null;

	/**
	 * SMTP Mailer class to send e-mail
	 */
	private SMTPMailer s = null;

	/**
	 * String containing the sender of the e-mail
	 */
	private String mailsender = null;

	/**
	 * String containing the recipient of the e-mail
	 */
	private String mailreceiver = null;

	/**
	 * Constructor for the class
	 * 
	 * @param filename
	 *            The file to be monitored
	 * @param regex
	 *            Every new line in the file is matched to this regex
	 * @param mailer
	 *            SMTPMailer to use when sending e-mails
	 * @param sender
	 *            Sender e-mail address
	 * @param recipient
	 *            Recipient e-mail address
	 */
	public Tailer(String filename, String regex, SMTPMailer mailer,
			String sender, String recipient) {

		this.fname = filename;
		this.p = Pattern.compile(regex);
		this.s = mailer;

		mailsender = sender;
		mailreceiver = recipient;
	}

	/**
	 * Method to start tailing and send an e-mail on each match
	 */
	public void startTailing() {
		t = new Thread() {
			public void run() {
				try {
					BufferedReader input = new BufferedReader(new FileReader(
							fname));
					String currentline = null;
					input.skip(new File(fname).length());
					while (true) {
						if ((currentline = input.readLine()) != null) {
							// System.out.println("New line: " + currentline);
							if (p.matcher(currentline).matches()) {
								System.out
										.println("LINE MATCH: " + currentline);
								try {
									if (s.send(currentline, SUBJECT_LINE,
											mailsender, mailreceiver)) {
										System.out
												.println("SMTP send() returned TRUE, done");
									}
								} catch (IOException ex) {
									System.err
											.println("Failed to send e-mail: "
													+ ex.getMessage());
									ex.printStackTrace();
								}
							}
							continue;
						}
						try {
							Thread.sleep(1000L);
						} catch (InterruptedException x) {
							Thread.currentThread().interrupt();
							break;
						}
					}
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
}