package webprogramming;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * A simple Server that communicated via HTTP over port 8090
 * 
 * compile command :: javac Server.java
 *     run command :: java Server.java 8090
 * 
 * @author michaelpermyashkin
 */
public class Server {
	
	private static String[] allowed_dirs = {"/dir1/", "/dir2/"};

	/**
	 * Creates socket on :8090 and accepts client connections to open communication
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		int portNumber = Integer.parseInt(args[0]);		
		Server server = new Server();
		server.run(portNumber);
		
	}
	
	
	/**
	 * Creates the socket and accepts client connections over the specified port
	 * 
	 * - Prints a welcome message and waits for client to communicate
	 * - When client sends message, we process the request and respond accordingly
	 * 
	 * @param portNumber - Port number the server will be listening on
	 */
	private void run(int portNumber) throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(portNumber);
				Socket clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
			
			// Send welcome message and instructions to client
			showWelcomeMessage(out);
			
			// Wait for input and then process request
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine); // displays the request on server
				String[] params = inputLine.split(" "); // splits the request 
				String filePath = params[1];	// extracts the file path from the HTTP request

				processRequestPath(filePath, out); // helper function to handle processing of file path
			}
		}
	}


	/**
	 * Helper function to process the clients file request.
	 * 
	 * Response codes
	 * ---------------
	 * dir1/
	 * 		HTTP 200 if file exists
	 * 		HTTP 404 if file not found
	 * dir2/
	 * 		HTTP 200 if file exists
	 * 		HTTP 404 if file not found
	 * blocked/
	 * 		HTTP 403 forbidden directory
	 * other/
	 * 		HTTP 403 forbidden 
	 * 
	 * @param filePath - The file path the client is requesting
	 * @param out - buffer writer the server can write a response to the client
	 */
	private void processRequestPath(String filePath, PrintWriter out) throws FileNotFoundException {
		// If filePath is in any of the allowed paths we check for the file
		if (Arrays.stream(allowed_dirs).parallel().anyMatch(filePath::contains)) {
			File file = new File("." + filePath);
			
			// if file exists, read/write with HTTP 200 - else HTTP 404
			if (file.exists()) {
				Scanner sc = new Scanner(file);
				String contents = "";
				while (sc.hasNext()) {
					contents += sc.nextLine() + "\n";
				}
				sc.close();
				out.println("\r\nHTTP/1.1 200 OK \r\n"+contents+"\r\n\r\n");
			} else {
				out.println("\r\nHTTP/1.1 404 FileNotFound\r\n\r\n");
			}
		} else {
			// The requested file is either to the blocked directory or beyond the local file structure
			out.println("\r\nHTTP/1.1 403 Forbidden\r\n\r\n");
		}
	}


	/**
	 * Sends welcome message and usage instructions to client
	 * 
	 * @param out - PrintWriter for client socket connection
	 */
	private void showWelcomeMessage(PrintWriter out) {
		// Display welcome message to client
		out.println("\r\nWelcome to Michael Permyashkin's Simple Server");
		out.println("----------------------------------------------");
		out.println("Directory Structure");
		out.println("/dir1/");
		out.println("     file1.txt");
		out.println("     file2.txt");
		out.println("/dir2/");
		out.println("     file1.txt");
		out.println("     file2.txt");
		out.println("/blocked/");
		out.println("     file1.txt");
		out.println("     file2.txt\r\n");
		out.println("\r\nExample request: /dir1/file1.txt");
		out.println("----------------------------------------------\r\n");
		
	}
}

