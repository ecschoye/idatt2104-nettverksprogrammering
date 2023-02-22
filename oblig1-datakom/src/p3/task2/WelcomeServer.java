package p3.task2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WelcomeServer {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 1250;
        //127.0.0.1:1250
        ServerSocket serverSocket = new ServerSocket(PORTNR);
        System.out.println("Server log. Waiting now...");
        Socket socket = serverSocket.accept();
        System.out.println("Now we have contact with a client!");

        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(inputStreamReader);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        writer.println("HTTP/1.1 200 OK\n");
        writer.println("Content-Type: text/html; charset=utf-8\n\n");
        writer.println("<html><body><h1>Welcome to the server!</h1>");
        writer.println("<ul>");
        String line = reader.readLine();
        while(!line.isEmpty()) {
            writer.println("<li>"+ line + "</li>");
            line = reader.readLine();
        }
        writer.println("<li>Wow</li>");
        writer.println("</ul>");
        writer.println("</body></html>");

        reader.close();
        writer.close();
        socket.close();
        serverSocket.close();
    }
}

