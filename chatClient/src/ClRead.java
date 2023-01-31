import java.net.*;
import java.io.*;

public class ClRead extends Thread {
    Socket soc = null;
    DataOutputStream outstr = null;
    BufferedReader bufr = null;
    ClRead(Socket s, DataOutputStream dout, BufferedReader br) {
        soc = s;
        outstr = dout;
        bufr = br;
    }
    public void run() {
        String str1 = "";

        while (!str1.equals("\u001a")) {
            try {
                str1 = bufr.readLine();
                outstr.writeUTF(str1);
                outstr.flush();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            outstr.close();
            bufr.close();
            soc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
