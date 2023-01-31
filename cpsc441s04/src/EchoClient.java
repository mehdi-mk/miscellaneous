import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

public class EchoClient {
    public static void main(String[] args) throws IOException {
        String serveraddress = args[0];
        int port = Integer.parseInt(args[1]);

        Socket s = new Socket(serveraddress, port);

        BufferedReader dis = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter dos = new PrintWriter(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String input = br.readLine();
        String serverEcho;

        while(!input.equals("stop")){
            dos.println(input);
            dos.flush();
            serverEcho = dis.readLine();
            System.out.println("Server echos: " + serverEcho);
            input = br.readLine();
        }
    }
}
