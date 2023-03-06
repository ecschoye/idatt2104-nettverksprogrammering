package task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Runnable{
    private final Socket connection;

    public SocketServer(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(readConnection);
            PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

            writer.println("Hi, you have contact with the server side!");
            writer.println("Here you can write!");
            while (true) {
                String operator = reader.readLine();
                writer.println("Operator: " + operator);

                String number1 = reader.readLine();
                writer.println("Number 1: " + number1);

                String number2 = reader.readLine();
                writer.println("Number 2: " + number2);

                int result;

                int number1Int = Integer.parseInt(number1);
                int number2Int = Integer.parseInt(number2);

                if (operator.equals("+")) {
                    result = number1Int + number2Int;
                    writer.println("Answer: " + result);
                } else {
                    result = number1Int - number2Int;
                    writer.println("Answer: " + result);
                }

                if (reader.readLine().equals("n")) {
                    System.out.println("The client has terminated the connection");
                    break;
                }
            }
            reader.close();
            writer.close();
            //connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
