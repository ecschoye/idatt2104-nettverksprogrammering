package task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 1250;

        Scanner readerFromCommandLine = new Scanner(System.in);
        System.out.print("Enter the name of the machine where the server program is running: ");
        String serverMachine = readerFromCommandLine.nextLine();

        Socket connection = new Socket(serverMachine, PORTNR);
        System.out.println("The connection is now established.");

        InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(readConnection);
        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

        String introduction1 = reader.readLine();
        String introduction2 = reader.readLine();
        System.out.println(introduction1 + "\n" + introduction2);
        while (true) {
            System.out.println("Enter operator (+, -): ");
            String operator = readerFromCommandLine.nextLine();
            while (!operator.equals("+") && !operator.equals("-")) {
                System.out.println("Operator is not valid. Try again.");
                System.out.println("Enter operator (+, -): ");
                operator = readerFromCommandLine.nextLine();
            }
            writer.println(operator);

            System.out.println(reader.readLine());

            System.out.println("Enter number 1: ");
            String number1 = readerFromCommandLine.nextLine();
            writer.println(number1);
            System.out.println(reader.readLine());

            System.out.println("Enter number 2: ");
            String number2 = readerFromCommandLine.nextLine();
            writer.println(number2);
            System.out.println(reader.readLine());

            System.out.println("---");
            System.out.println(reader.readLine());

            System.out.println("Do you want to continue? (y/n): ");
            String response = readerFromCommandLine.nextLine();
            writer.println(response);
            if (response.equalsIgnoreCase("n")) {
                break;
            }
        }

        reader.close();
        writer.close();
        connection.close();
        readerFromCommandLine.close();
    }
}
