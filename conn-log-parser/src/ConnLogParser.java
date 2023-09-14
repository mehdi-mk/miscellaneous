import java.io.*;
import java.util.HashMap;

public class ConnLogParser {

    public static void main(String[] args) {
        BufferedReader reader1, reader2;
        BufferedWriter writer;

        try {
            reader1 = new BufferedReader(new FileReader("./imaps_2020_100mb_ip.dat"));
            int numOfLines = 0;
            String line1 = reader1.readLine();

            while(line1 != null) {
                numOfLines++;
                line1 = reader1.readLine();
            }
            reader1.close();
            reader1 = new BufferedReader(new FileReader("./imaps_2020_100mb_ip.dat"));
            reader2 = new BufferedReader(new FileReader("./imaps_2020_100mb_t.dat"));

            String ips [] = new String [numOfLines];
            String times [] = new String [numOfLines];

            line1 = reader1.readLine();
            String line2 = reader2.readLine();
            int i = 0;

            while(line1 != null) {
                ips[i] = line1;
                times[i] = line2;

                line1 = reader1.readLine();
                line2 = reader2.readLine();

                i++;
            }

            HashMap<String,String> hm = new HashMap<String,String>();

            for(int h = 0; h < numOfLines; h++) {
                if (hm.get(ips[h]) == null) {
                    hm.put(ips[h], times[h]);
                }
            }

            writer = new BufferedWriter(new FileWriter("./imaps_2020_100mb_clean.dat"));

            for(int h = 0; h < numOfLines; h++) {
                if(hm.get(ips[h]) != null) {
                    writer.write(ips[h] + "\t" + hm.get(ips[h]) + "\n");
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
