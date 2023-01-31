import java.net.*;
import java.io.*;

public class Client{
    public static void main(String args[])throws Exception{
        Socket s = new Socket("localhost", 6666);
        DataInputStream dis = new DataInputStream(s.getInputStream());
        PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String str = "", str1 = "";

        while(!str.equals("stop")){
            str = br.readLine();
            pw.println(str);
            //dos.flush();
            //str1 = dis.readUTF();
            //System.out.println("Server says: " + str1);
        }
        dis.close();
        pw.close();
        br.close();
        s.close();
    }
}  