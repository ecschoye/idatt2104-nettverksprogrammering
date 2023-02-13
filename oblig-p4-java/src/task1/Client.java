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
            String expression = readerFromCommandLine.nextLine();
            buf = expression.getBytes();
            DatagramPacket sendingPacket = new DatagramPacket(buf, buf.length, address, 1250);
            socket.send(sendingPacket);

            if (expression.equalsIgnoreCase("n")) {
                break;
            }

            DatagramPacket receivingPacket = new DatagramPacket(buf, buf.length);
            socket.receive(receivingPacket);
            String received = new String(buf, 0, buf.length);
            System.out.println("Result: " + received);

        }
        socket.close();
    }
}
