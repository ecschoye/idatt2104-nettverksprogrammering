package task1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {
    static DatagramSocket socket;
    boolean isRunning;

    public Server() throws SocketException {
        socket = new DatagramSocket(1250);
    }

    public void run() throws IOException {
        isRunning = true;
        System.out.println("Server is running...");

        while (isRunning) {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String expression = new String(packet.getData(), 0, packet.getLength());

            expression = expression.trim();
            System.out.println("Received expression: " + expression);

            // Perform calculation
            String[] expressionParts = expression.split(" ");
            int number1 = Integer.parseInt(expressionParts[0]);
            int number2 = Integer.parseInt(expressionParts[2]);
            String operator = expressionParts[1];
            int result = 0;
            switch (operator) {
                case "+" -> result = number1 + number2;
                case "-" -> result = number1 - number2;
                case "*" -> result = number1 * number2;
                case "/" -> result = number1 / number2;
            }
            System.out.println("Result: " + result);
            buf = Integer.toString(result).getBytes();
            port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, packet.getAddress(), port);
            socket.send(packet);
        }
        socket.close();
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
