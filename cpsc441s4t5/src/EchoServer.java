import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    public static void main(String[] args) {

        if (args.length !=1){
            System.out.printf("Usage: java EchoServer <port number>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        try(
            ServerSocket ss = new ServerSocket(port);
            Socket s = ss.accept();

            DataInputStream dis = new DataInputStream(s.getInputStream());

            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        ){
            String input = dis.readUTF();
            while (!input.equals("stop")){
                dos.writeUTF(input);
                dos.flush();
                input = dis.readUTF();
            }
        }catch (IOException e){
            System.out.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.out.printf(e.getMessage());
        }
    }
}
