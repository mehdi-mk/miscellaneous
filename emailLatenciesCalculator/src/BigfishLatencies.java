import java.util.Scanner;
import java.io.*;

public class BigfishLatencies {

    public static void main(String[] args) throws IOException {

        BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please enter the name of the file containing emails with headers: ");
        String fileName = ob.readLine();
        System.out.println();
        Scanner sc = new Scanner(new File(fileName));
        String str = sc.next();
        String strPre4 = null, strPre3 = null, strPre2 = null, strPre1 = null, strNext = null;
        String receivedTime = null, sentTime = null;
        String afterBigfishTime = null, beforeBigfishTime = null;
        boolean insideEmailHeaders = false, foundReceivedTime = false, foundSentTime = false;
        int foundAfterBigfish = 0;
        int emailCounter = 0;

        while (sc.hasNext()) {
            if (!insideEmailHeaders && str.equals("From")) {
                insideEmailHeaders = true;
                foundReceivedTime = false;
                foundSentTime = false;
                foundAfterBigfish = 0;
                emailCounter++;
            }
            else if (insideEmailHeaders && !foundReceivedTime &&
                    str.matches("(((0|1)[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]")) {
                strNext = sc.next();
                receivedTime = strPre3 + " " + strPre2 + " " + strPre1 + " " + str + " " + strNext;
                foundReceivedTime = true;
            }
            else if (insideEmailHeaders && foundReceivedTime && (foundAfterBigfish ==0) &&
                    str.contains("bigfish.com")) {
                foundAfterBigfish++;
            }
            else if (insideEmailHeaders && foundReceivedTime && (foundAfterBigfish == 1) &&
                    str.matches("(((0|1)[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]")) {
                strNext = sc.next();
                afterBigfishTime = strPre4 + " " + strPre3 + " " + strPre2 + " " + strPre1 + " " + str + " " + strNext;
                foundAfterBigfish++;
            }
            else if (insideEmailHeaders && foundReceivedTime && (foundAfterBigfish == 2) &&
                    str.matches("(((0|1)[0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]")) {
                strNext = sc.next();
                beforeBigfishTime = strPre4 + " " + strPre3 + " " + strPre2 + " " + strPre1 + " " + str + " " + strNext;
                foundAfterBigfish++;
            }
            else if (insideEmailHeaders && foundReceivedTime && (foundAfterBigfish == 3) &&
                    str.contains("bigfish.com")) {
                foundAfterBigfish--;
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
                if (foundAfterBigfish == 3) {
                    System.out.println("After Bigfish = " + afterBigfishTime + "\t" +
                            "Before Bigfish = " + beforeBigfishTime);
                } else
                    System.out.println("Bigfish.com was not in this trace.");
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
