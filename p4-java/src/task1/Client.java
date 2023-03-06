package task1;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner readerFromCommandLine = new Scanner(System.in);
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getLocalHost();
        byte[] buf = null;

        while (true) {
            System.out.println("Enter math expression: ");
            System.out.println("Example: 2 + 2, 2 - 2, 2 * 2, 2 / 2");
            System.out.println("Enter 'n' to exit");
            buf = new byte[256];
            String expression = readerFromCommandLine.nextLine();
            System.out.println("Expression: " + expression);

            if (expression.equalsIgnoreCase("n")) {
                break;
            }

            String[] tokens = expression.split(" ");
            if (tokens.length != 3 || !tokens[0].matches("-?\\d+") || !tokens[2].matches("-?\\d+")) {
                System.out.println("Invalid expression format. Please try again.");
                continue;
            }

            buf = expression.getBytes();
            DatagramPacket sendingPacket = new DatagramPacket(buf, buf.length, address, 1250);
            socket.send(sendingPacket);

            buf = new byte[256];
            DatagramPacket receivingPacket = new DatagramPacket(buf, buf.length);
            socket.receive(receivingPacket);
            String received = new String(buf, 0, buf.length);
            System.out.println("Result: " + received + "\n");

        }
        socket.close();
    }
}
