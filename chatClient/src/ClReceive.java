import java.net.*;
import java.io.*;

public class ClReceive extends Thread {
    Socket soc = null;
    DataInputStream instr = null;
    ClReceive(Socket s, DataInputStream din) {
        soc = s;
        instr = din;
    }
    public void run() {
        String str = "";
        while (!str.equals("\u001a")) {
            try {
                str = instr.readUTF();
                System.out.println("Server says: " + str);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            instr.close();
            soc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
