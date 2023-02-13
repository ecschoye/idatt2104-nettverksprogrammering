package task0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTjener {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 1250;
        ServerSocket server = new ServerSocket(PORTNR);
        System.out.println("Server log. Waiting now...");
        Socket connection = server.accept();
        System.out.println("Now we have contact with a client!");

        InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(readConnection);
        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

        writer.println("Hi, you have contact with the server side!");
        writer.println("Here you can write!");
        while (true) {
            System.out.println("In while loop");
            String operator = reader.readLine();
            writer.println("Operator: " + operator);

            String number1 = reader.readLine();
            writer.println("Number 1: " + number1);

            String number2 = reader.readLine();
            writer.println("Number 2: " + number2);

            int number1Int = Integer.parseInt(number1);
            int number2Int = Integer.parseInt(number2);
            int answer = 0;

            if (operator.equals("+")) {
                answer = number1Int + number2Int;
            } else if (operator.equals("-")) {
                answer = number1Int - number2Int;
            } else {
                writer.println("Operator is not valid");
            }

            writer.println("Answer: " + answer);

            if (reader.readLine().equals("n")) {
                System.out.println("The client has terminated the connection");
                break;
            }
        }
        reader.close();
        writer.close();
        connection.close();
        server.close();
    }
}
