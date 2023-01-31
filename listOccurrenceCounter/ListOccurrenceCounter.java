import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ListOccurrenceCounter {

    public static void main(String[] args) {
        BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader file;
        BufferedWriter writer;
        String fileName, line, ip, baseIP;
        double dur, min, max, sum, mean;
        int count = 0;
        int index, length;
        try {
            System.out.println("This program looks into the first column of a file and finds the groups of " +
                    "consecutive occurrences of strings, and for each group finds the minimum, maximum, and " +
                    "mean of the corresponding values, in the second column. For example, the first column " +
                    "could be the list of Source IPs that made a connection to a particular host, sorted based " +
                    "on the time of connection establishment, and the second column is the duration of each " +
                    "connection. Using this program, If an IP made multiple connections to the host at " +
                    "almost the same time, we can find the minimum, maximum, and mean of the durations " +
                    "of those connections. Please note that columns must be separated by '|' character.\n");
            System.out.print("Enter the name of the file: ");
            fileName = ob.readLine();
            file = new BufferedReader(new FileReader("./" + fileName));
            writer = new BufferedWriter(new FileWriter("./" + fileName + "_occur-stats.txt"));
            line = file.readLine();
            //System.out.println("line: " + line);
            index = line.indexOf("|");
            length = line.length();
            ip = line.substring(0, index);
            //System.out.println("ip: " + ip);
            baseIP = ip;
            dur = Double.parseDouble(line.substring(index+1, length));
            //System.out.println("dur: " + dur);
            min = dur; max = dur; sum = dur; count++;
            line = file.readLine();
            while(line != null) {
                //System.out.println("line: " + line);
                index = line.indexOf("|");
                length = line.length();
                ip = line.substring(0, index);
                //System.out.println("ip: " + ip);
                if (baseIP.equals(ip)) {
                    dur = Double.parseDouble(line.substring(index+1, length));
                    //System.out.println("dur: " + dur);
                    sum += dur;
                    if (dur < min) min = dur;
                    if (dur > max) max = dur;
                    count++;
                }
                else {
                    mean = sum / count;
                    writer.write(baseIP + "|" + min + "|" + mean + "|" + max + "|" + count + "\n");
                    baseIP = ip;
                    dur = Double.parseDouble(line.substring(index+1, length));
                    //System.out.println("dur: " + dur);
                    min = dur; max = dur; sum = dur; count = 1;
                }
                line = file.readLine();
            }
            System.out.println("Done! Results are stored in the '" + fileName + "_occur-stats.txt' file.");
            file.close(); writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
