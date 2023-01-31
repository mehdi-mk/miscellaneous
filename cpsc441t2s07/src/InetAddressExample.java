import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressExample {
    public static void main (String [] args) throws UnknownHostException {
        InetAddress ip = InetAddress.getByName("www.ucalgary.ca");
        InetAddress localIP = InetAddress.getLocalHost();

        String s = "www.ucalgary.ca";

        System.out.println("Host name: " + ip.getHostName());
        System.out.println("IP Address: " + ip.getHostAddress());
        System.out.println("\nLocal Host: " + localIP.getHostName());
        System.out.println("Local IP Address: " + localIP.getHostAddress());
    }
}
