import java.io.*;
import java.net.*;

public class ClientMain {

    public static void main(String[] args) throws IOException {
        Socket s = new Socket("192.168.0.17",3333);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        ClReceive creceive = new ClReceive(s, din);
        ClRead cread = new ClRead(s, dout, br);
        creceive.start();
        cread.start();

        while (creceive.isAlive() && cread.isAlive()) {
            ;
        }
        din.close();
        dout.close();
        br.close();
        s.close();
    }
}
