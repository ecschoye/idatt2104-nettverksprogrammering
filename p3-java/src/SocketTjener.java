import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTjener {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 1250;
        ServerSocket tjener = new ServerSocket(PORTNR);
        System.out.println("Logg for tjenersiden. Nå venter vi...");
        Socket forbindelse = tjener.accept();

        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream());

        skriveren.println("Hei, du har kontakt med tjenersiden!");
        skriveren.println("Her kan du skriv!");

        String enLinje = leseren.readLine();
        while (enLinje != null) {
            System.out.println("En klient skrev" + enLinje);
            skriveren.println("Du skrev skrev" + enLinje);
            String[] numbers = enLinje.split("[+-/*]+");
            String[] operators = enLinje.split("[^+-/*]+");
            if (numbers.length < 2 || operators.length < 1 || numbers.length - 1 != operators.length) {
                skriveren.println("det var noe galt med inputet");
            }

            enLinje = leseren.readLine();
        }
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }

    public int[] stringArrayToIntArray(String[] stringArray) {
        int[] intNumbers = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            intNumbers[i] = Integer.parseInt(stringArray[i]);
        }
        return intNumbers;
    }

    public int equation(String[] stringArray, String[] operators) {
        int[] numbers = stringArrayToIntArray(stringArray);
        int result = 0;
        String operator = "";
        for (int i = 0; i < stringArray.length; i++) {
            if (i == 0) {
                result = numbers[0];
            } else {
                operator = operators[i];
                switch (operator) {
                    case "+" -> result += numbers[i];
                    case "-" -> result -= numbers[i];
                    case "*" -> result *= numbers[i];
                    case "/" -> result /= numbers[i];
                }
            }
        }
        return result;
    }
}
