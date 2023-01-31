import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoClient {
    public static void main(String args[]) {
        if (args.length != 2) {
            System.out.println("Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        try(
                Socket s = new Socket(hostname, port);

                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(s.getInputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                ){
            String input = br.readLine();
            while(!input.equals("stop")){
                dos.writeUTF(input);
                dos.flush();
                System.out.println("Echo from Server: " + in.readLine();
                input = br.readLine();
            }

        }catch (UnknownHostException e) {
            System.out.println("Don't recognize the host " + hostname);
            System.exit(1);
        }catch (IOException e){
            System.out.println("Coudn't work with I/O at Client side " + hostname);
            System.exit(1);
        }

    }
}
