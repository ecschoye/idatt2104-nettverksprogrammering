import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketKlient {
    public static void main(String[] args) throws IOException {
        final int PORT_NR = 2502;

        Scanner leserFraKommandoVindu = new Scanner(System.in);
        System.out.println("Oppgi navnet på maskinen der tjenerprogrammet kjører: ");
        String tjenermaskinen = leserFraKommandoVindu.nextLine();

        Socket forbindelse = new Socket(tjenermaskinen, PORT_NR);
        System.out.println("Nå er forbindelsen opprettet.");

        InputStreamReader leseforbindelsen = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelsen);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream());

        String innledning1 = leseren.readLine();
        String innledning2 = leseren.readLine();
        System.out.println(innledning1 + "\n" + innledning2);

        String enlinje = leserFraKommandoVindu.nextLine();
        while (!enlinje.equals("")) {
            skriveren.println(enlinje);
            String respons = leseren.readLine();
            System.out.println("Fra tjenerprogrammet" + respons);
            enlinje = leserFraKommandoVindu.nextLine();
        }
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}
