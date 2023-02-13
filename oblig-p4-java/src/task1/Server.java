package task1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(1250);
        byte[] buf = new byte[256];
        DatagramPacket receivePacket = null;
        DatagramPacket responsePacket = null;
        while (true) {
            buf = new byte[256];

            receivePacket = new DatagramPacket(buf, buf.length);
            // Receive expression
            socket.receive(receivePacket);
            String expression = new String(receivePacket.getData(), 0, receivePacket.getLength());
            expression = expression.trim();
            System.out.println("Received expression: " + expression);

            if (expression.equalsIgnoreCase("n")) {
                break;
            }

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
            buf = Integer.toString(result).getBytes();
            int port = receivePacket.getPort();
            responsePacket = new DatagramPacket(buf, buf.length, receivePacket.getAddress(), port);
            socket.send(responsePacket);


        }
        socket.close();
    }
}
