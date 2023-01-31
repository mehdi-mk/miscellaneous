import java.net.*;
import java.io.*;

public class Server {
    public static void main(String args[])throws Exception{
        ServerSocket ss = new ServerSocket(6666);
        Socket s = ss.accept();
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        String str = "";

        Process proc = null;
        BufferedReader reader = null;

        while(!str.equals("stop")){
            str = br.readLine();
            proc = Runtime.getRuntime().exec("/bin/bash -c " + str);
            reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";
            while((line = reader.readLine()) != null) {
                System.out.print(line + "\n");
            }
            proc.waitFor();
        }
        //dis.close();
        dos.close();
        br.close();
        s.close();
        ss.close();
    }
}  