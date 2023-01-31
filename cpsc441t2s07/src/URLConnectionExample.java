import java.io.InputStream;
import java.net.*;

public class URLConnectionExample {
    public static void main (String [] args) {
        try{
            URL url = new URL("http://pages.cpsc.ucalgary.ca/~sina.keshvadi1/");
            URLConnection urlconn = url.openConnection();
            InputStream stream = urlconn.getInputStream();
            int i;
            while((i = stream.read()) != -1) {
                System.out.println((char) i);
            }
        }catch (Exception e) {
            System.out.println(e);
        }
    }
}
