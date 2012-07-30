package ch.krenger.simon.logmonitoring.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class is used to send an e-mail via a SMTP server. Shameless copy from
 * http://www.java-forums.org/java-net/7247-sending-mail-using-sockets.html
 * 
 * @since 04.02.2010
 * @author krens1
 */
public class SMTPMailer {

	/**
	 * Variable containing the SMTP server address
	 */
	InetAddress mailHost;

	/**
	 * Localhost
	 */
	InetAddress localhost;

	/**
	 * Port of the SMTP daemon
	 */
	int mailPort;

	BufferedReader in;
	PrintWriter out;

	/**
	 * This is the constructor for the SMTPMailer class.
	 * 
	 * @param host
	 *            This is the server that will be contacted to send e-mails.
	 */
	public SMTPMailer(String host, int port) {
		try {
			mailHost = InetAddress.getByName(host);
			localhost = InetAddress.getLocalHost();
			mailPort = port;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to send an e-mail via a SMTP server.
	 * 
	 * @param msgText
	 *            This is the message text which represents the contents of this
	 *            message.
	 * @param subject
	 *            The subject used to send the e-mail.
	 * @param from
	 *            This is the sender of the e-mail
	 * @param to
	 *            The recipient of the message
	 * @return Returns TRUE if the e-mail could be sent and FALSE if there was
	 *         an error.
	 * @throws IOException
	 */
	public boolean send(String msgText, String subject, String from, String to)
			throws IOException {
		Socket smtpPipe;
		InputStream inn;
		OutputStream outt;

		smtpPipe = new Socket(mailHost, mailPort);

		inn = smtpPipe.getInputStream();
		outt = smtpPipe.getOutputStream();
		in = new BufferedReader(new InputStreamReader(inn));
		out = new PrintWriter(new OutputStreamWriter(outt), true);
		if (inn == null || outt == null) {
			System.out.println("Failed to open streams to socket.");
			return false;
		}
		String initialID = in.readLine();
		System.out.println(initialID);
		System.out.println("HELO " + localhost.getHostName());
		out.println("HELO " + localhost.getHostName());
		String welcome = in.readLine();
		System.out.println(welcome);
		System.out.println("MAIL From:<" + from + ">");
		out.println("MAIL From:<" + from + ">");
		String senderOK = in.readLine();
		System.out.println(senderOK);
		System.out.println("RCPT TO:<" + to + ">");
		out.println("RCPT TO:<" + to + ">");
		String recipientOK = in.readLine();
		System.out.println(recipientOK);
		System.out.println("DATA");
		out.println("DATA");
		out.println("From: <" + from + ">\nTo: <" + to + ">\nSubject: "
				+ subject + "\n\n" + msgText);
		System.out.println(".");
		out.println(".");
		String acceptedOK = in.readLine();
		System.out.println(acceptedOK);
		System.out.println("QUIT");
		out.println("QUIT");
		return true;
	}
}
