package p3.task1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 1250;
        ServerSocket serverSocket = new ServerSocket(PORTNR);
        System.out.println("Server log. Waiting now...");

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Now we have contact with a client!");
                new Thread(new SocketServer(socket)).start();
            } catch (IOException e) {
                System.out.println("An error occurred while accepting a connection: " + e.getMessage());
            }
        }


    }
}

