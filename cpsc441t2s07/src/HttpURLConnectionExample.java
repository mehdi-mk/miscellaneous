import java.io.InputStream;
import java.net.*;

public class HttpURLConnectionExample {
    public static void main (String [] args) {
        try{
            URL url = new URL("http://pages.cpsc.ucalgary.ca/~sina.keshvadi1/");
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();

            for(int i = 1; i <= 8; i++)
                System.out.println(httpconn.getHeaderFieldKey(i) + " = " + httpconn.getHeaderField(i));
            httpconn.disconnect();
        }catch (Exception e) {
            System.out.println(e);
        }
    }
}
