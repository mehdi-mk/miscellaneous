import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        ServerSocket ss = new ServerSocket(port);
        Socket s = ss.accept();

        BufferedReader dis = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter dos = new PrintWriter(s.getOutputStream());

        String input = dis.readLine();
        while(!input.equals("stop")){
            dos.println(input);
            dos.flush();
            input = dis.readLine();
        }
    }
}
