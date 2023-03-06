import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.WeakHashMap;

public class Websocket {
    private final int PORTNR = 3001;
    private final String HOST = "127.0.0.1";
    private final String URL = HOST + ":" + PORTNR;

    public void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORTNR);
        System.out.println("Server started on " + URL);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Scanner scanner = new Scanner(System.in);
                        for(;;) {
                            String message = scanner.nextLine();
                            System.out.println("Message: " + message);
                            if(!message.isBlank() && !message.isEmpty()) {
                                // TODO: Send message to client
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                System.out.println("Client connected");
            } catch (Exception e) {
                e.printStackTrace();
            }   
            if (socket.isConnected()) {
                
            }
        }

    }
}