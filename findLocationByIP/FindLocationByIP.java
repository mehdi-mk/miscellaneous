/* This program takes a columnar file with delimiter '|' as the first input which contains a column of
IP addresses (in either IP format or integer) and another column as the counts for each of the
IPs (could be number of flows, traffic bytes, etc.). These 2 columns must be passed to the program
as second and third arguments. For each IP, it searches through a source database file, taken as
the fourth argument to find the location (e.g., country, city, organization, etc.) of each IP. 
The output is a file named 'INPUT_FILE_output.txt' that aggregates the counts available for each location.
*/

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindLocationByIP {

    public static void main(String[] args) {
        System.out.println("This program takes a columnar file with delimiter '|' as the first input which contains a column of " +
                "IP addresses (in either IP format or integer) and another column as the counts for each of the " +
                "IPs (could be number of flows, traffic bytes, etc.). These 2 columns must be passed to the program " +
                "as second and third arguments. For each IP, it searches through a source database file, taken as " +
                "the fourth argument to find the location (e.g., country, city, organization, etc.) of each IP. " +
                "The output is a file named 'INPUT_FILE_output.txt' that aggregates the counts available for each location.\n");

        if (args.length != 4 || !isInteger(args[1]) || !isInteger(args[2])) {
            System.out.println("Usage: java FindCountryByIP <input file> <column number of IPs> " +
                    "<column number of counts> <database file>");
            return;
        }
        //=====================================================================================//
        //===== Input file is listOfIPsFile and the source database file is ip2location. ======//
        //=====================================================================================//
        BufferedReader listOfIPsFile, ip2location;
        BufferedWriter writer;
        String fileName, line, line2, ipStr = null, flowsStr = null, database;
        String[] entry;
        int length, ipColumnNum, flowsColumnNum;
        long  ip, flows, startIP, endIP;
        List<Integer> indicesOfVerticalBars = new ArrayList<>();

        //=====================================================================================//
        //==== Output is stored as a HashMap, with Keys as countries, and Values as counts. ===//
        //==== It will be written to the output file at the end. ==============================//
        //=====================================================================================//
        HashMap<String, Long> countries_count = new HashMap<>();
        fileName = args[0];
        database = args[3];
        ipColumnNum = Integer.parseInt(args[1]);
        flowsColumnNum = Integer.parseInt(args[2]);

        System.out.println("Processing the file ...");

        try {

            listOfIPsFile = new BufferedReader(new FileReader("./" + fileName));
            writer = new BufferedWriter(new FileWriter("./" + fileName + "_output.txt"));
            line = listOfIPsFile.readLine();
            length = line.length();

        //=====================================================================================//
        //== The boundaries of columns are found here by finding the index of '|' character. ==//
        //=====================================================================================//
            indicesOfVerticalBars.add(-1);
            for (int i = 1; i <= length; i++) {
                if (line.substring(i).startsWith("|")) {
                    indicesOfVerticalBars.add(i);
                }
            }
            indicesOfVerticalBars.add(length);
            for (Integer i : indicesOfVerticalBars) {
            }

            //=====================================================================================//
        //=============================== The main loop is here ===============================//
        //=====================================================================================//
            while (line != null && !line.contains("(")) {

        //=====================================================================================//
        //====================== Finds the value of IP and flow (count) here ==================//
        //=====================================================================================//
                for (int i = 1; i <= ipColumnNum; i++) {
                    if (i == ipColumnNum) {
                        ipStr = line.substring(indicesOfVerticalBars.get(i - 1) + 1,
                                indicesOfVerticalBars.get(i) - 1).trim();
                    }
                }

                for (int i = 1; i <= flowsColumnNum; i++) {
                    if (i == flowsColumnNum) {
                        flowsStr = line.substring(indicesOfVerticalBars.get(i - 1) + 1,
                                indicesOfVerticalBars.get(i) - 1).trim();
                    }
                }

                if (isInteger(ipStr) && isInteger(flowsStr)) {
                    ip = Long.parseLong(ipStr);
                    flows = Long.parseLong(flowsStr);
                } else if (ipStr.contains(".") && isInteger(flowsStr)) {
                    String[] octets;
                    octets = ipStr.split("\\.", -2);
                    ip = (Long.parseLong(octets[0]) * 16777216) +
                            (Long.parseLong(octets[1]) * 65536) +
                            (Long.parseLong(octets[2]) * 256) +
                            (Long.parseLong(octets[3]));
                    flows = Long.parseLong(flowsStr);
                } else {
                    line = listOfIPsFile.readLine();
                    continue;
                }

                ip2location = new BufferedReader(new FileReader("./" + database));
                line2 = ip2location.readLine();

                //=====================================================================================//
                //== The second loop where it looks into the source database file to find the =========//
                //== corresponding country of this IP. ================================================//
                //=====================================================================================//
                while (line2 != null) {

                    entry = line2.split(",", -2);
                    startIP = Long.parseLong(entry[0]);
                    endIP = Long.parseLong(entry[1]);

                    if (startIP <= ip && ip <= endIP) {
                        String key = "";
                        key += entry[2];
                        for (int i = 3; i < entry.length; i++) {
                            key = key + "," + entry[i];
                        }
                        if (countries_count.containsKey(key)) {
                            long tmp = countries_count.remove(key);
                            countries_count.put(key, tmp + flows);
                            break;
                        } else {
                            countries_count.put(key, flows);
                            break;
                        }
                    }
                    line2 = ip2location.readLine();
                }
                ip2location.close();
                line = listOfIPsFile.readLine();
            }
        //=====================================================================================//
        //========================= Writes to the output file here. ===========================//
        //=====================================================================================//
            for (String i : countries_count.keySet()) {
                writer.write(i + ": " + countries_count.get(i) + "\n");
            }
            listOfIPsFile.close();
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

        //=====================================================================================//
        //== This function returns true if all the characters in a string are integer, and ====//
        //== false otherwise. =================================================================//
        //=====================================================================================//
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
//        int i = 0;
//        if (str.charAt(0) == '-') {
//            if (length == 1) {
//                return false;
//            }
//            i = 1;
//        }
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
