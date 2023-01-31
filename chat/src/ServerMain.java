import java.io.*;
import java.net.*;

public class ServerMain {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = null;
        ss = new ServerSocket(3333);
        Socket s = ss.accept();

        DataInputStream din=new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        ServReceive sreceive = new ServReceive(s, din);
        ServRead sread = new ServRead(s, dout, br);
        sread.start();
        sreceive.start();

        while (sreceive.isAlive() && sread.isAlive()) {
            ;
        }
        din.close();
        s.close();
        ss.close();
    }
}
