package webprogramming;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Simple client that communicated with a server over HTTP. The client requests
 * files and is given an HTTP response with the file content or an error
 * message.
 * 
 * compile command :: javac Client.java 
 * run command :: java Client.java 127.0.0.1 8090
 * 
 * @author michaelpermyashkin
 */
public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		String host = args[0];
		int portNumber = Integer.parseInt(args[1]);

		Client client = new Client();
		client.run(host, portNumber);
	}

	/**
	 * Runner method for the client. - connects to socket by specifying host and
	 * port - enters path to file they would like to see and awaits response
	 * 
	 * @param host       - localhost or 127.0.0.1
	 * @param portNumber - :8090
	 */
	private void run(String host, int portNumber) throws UnknownHostException, IOException {
		try (Socket kkSocket = new Socket(host, portNumber);
				PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));) {

			Scanner stdIn = new Scanner(System.in); // scanner to get client input

			// Read response from the server and print to client console
			String fromServer;
			while ((fromServer = in.readLine()) != null) {
				System.out.println(fromServer); // prints HTTP response code
				// If additional lines were written to socket, condition passes and we print to console
				while(in.ready())
					System.out.println(in.readLine()); // Prints remaining lines from response
				
				// if client typed input into console, read and write to socket
				if (stdIn.hasNext()) {
					String fromUser = stdIn.nextLine();
					out.println("GET " + fromUser + " HTTP/1.1");
				}
			}

			stdIn.close(); // close scanner
		}
	}
}
