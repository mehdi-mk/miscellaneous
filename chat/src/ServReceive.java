import java.net.*;
import java.io.*;

public class ServReceive extends Thread {
    Socket soc = null;
    DataInputStream instr = null;
    ServReceive(Socket s, DataInputStream din) {
        soc = s;
        instr = din;
    }
    public void run() {
        String str = "";
        while (!str.equals("\u001a")) {
            try {
                str = instr.readUTF();
                System.out.println("Client says: " + str);
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
