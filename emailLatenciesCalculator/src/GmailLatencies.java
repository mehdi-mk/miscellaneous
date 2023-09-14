import java.util.Scanner;
import java.io.*;

public class GmailLatencies {

    public static void main(String[] args) throws IOException {

        BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please enter the name of the file containing emails with headers: ");
        String fileName = ob.readLine();
        System.out.println();
        Scanner sc = new Scanner(new File(fileName));
        String str = sc.next();
        String strPre4 = null, strPre3 = null, strPre2 = null, strPre1 = null, strNext = null;
        String receivedTime = null, sentTime = null, timeInMiddle = null;
        String afterMXTime = null, beforeMXTime = null;
        boolean insideEmailHeaders = false, foundReceivedTime = false, foundSentTime = false;
        boolean GmailReceivedTimeExplanatory = true;
        int foundAfterMX = 0;
        int emailCounter = 0;

        while (sc.hasNext()) {
            if (!insideEmailHeaders && str.equals("From")) {
                insideEmailHeaders = true;
                foundReceivedTime = false;
                foundSentTime = false;
                foundAfterMX = 0;
                emailCounter++;
            }
            else if (insideEmailHeaders && !foundReceivedTime && GmailReceivedTimeExplanatory &&
                    str.matches("(((0|1)[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]")) {
                strNext = sc.next();
                receivedTime = strPre3 + " " + strPre2 + " " + strPre1 + " " + str + " " + strNext;
                foundReceivedTime = true;
                GmailReceivedTimeExplanatory = false;
            }
            else if (insideEmailHeaders && foundReceivedTime && !GmailReceivedTimeExplanatory &&
                    str.matches("(((0|1)[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]")) {
                strNext = sc.next();
                receivedTime = strPre3 + " " + strPre2 + " " + strPre1 + " " + str + " " + strNext;
                timeInMiddle = receivedTime;
                GmailReceivedTimeExplanatory = true;
            }
            else if (insideEmailHeaders && foundReceivedTime && GmailReceivedTimeExplanatory && (foundAfterMX == 0) &&
                    str.matches("(((0|1)[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]")) {
                strNext = sc.next();
                timeInMiddle = strPre3 + " " + strPre2 + " " + strPre1 + " " + str + " " + strNext;
            }
            else if (insideEmailHeaders && foundReceivedTime && (foundAfterMX ==0) &&
                    str.contains("mx.google.com")) {
                afterMXTime = timeInMiddle;
                foundAfterMX++;
            }
            else if (insideEmailHeaders && foundReceivedTime && (foundAfterMX == 1) &&
                    str.matches("(((0|1)[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]")) {
                strNext = sc.next();
                beforeMXTime = strPre4 + " " + strPre3 + " " + strPre2 + " " + strPre1 + " " + str + " " + strNext;
                foundAfterMX++;
            }
            else if (insideEmailHeaders && foundReceivedTime && str.equals("Date:")) {
                insideEmailHeaders = false;
            }
            else if (!insideEmailHeaders && !foundSentTime &&
                    str.matches("(((0|1)[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]")) {
                strNext = sc.next();
                sentTime = strPre4 + " " + strPre3 + " " + strPre2 + " " + strPre1 + " " + str + " " + strNext;
                foundSentTime = true;

                System.out.println("Email #" + emailCounter + " :");
                System.out.println("Received time = " + receivedTime + "\t" + "Sent time = " + sentTime);
                if (foundAfterMX == 2) {
                    System.out.println("After MX.GOOGLE = " + afterMXTime + "\t" +
                            "Before MX.GOOGLE = " + beforeMXTime);
                } else
                    System.out.println("MX.GOOGLE was not in this trace.");
                System.out.println();
            }
            strPre4 = strPre3;
            strPre3 = strPre2;
            strPre2 = strPre1;
            strPre1 = str;
            str = sc.next();
        }
    }
}
