import java.net.URL;

public class URLExample {

    public static void main(String[] args) {
        try{
            URL url = new URL("http://pages.cpsc.ucalgary.ca/~sina.keshvadi1/");
            System.out.println("Protocol: " + url.getProtocol());
            System.out.println("Host name: " + url.getHost());
            System.out.println("Port number: " + url.getPort());
            System.out.println("Default port number: " + url.getDefaultPort());
            System.out.println("Query string: " + url.getQuery());
            System.out.println("Path: " + url.getPath());
            System.out.println("File: " + url.getFile());
        }catch (Exception e) {System.err.println(e);}
    }
}
