/* Written by Mehdi Karamollahi */

import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;

public class llcd {
    public static void main(String[] args) {
        BufferedReader reader;
        BufferedWriter writer;
        BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));

        try {
            //Gets the name of the file

            System.out.print("This program calculates the CDF of Log2 of values, ready for plotting the CDF and LLCD histograms. Please enter the name of the file in current folder: ");
            String fileName = ob.readLine();

            System.out.print("Enter the number, you want your dataset be divided by: ");
            int numOfIntervals = Integer.valueOf(ob.readLine());
            //Opens the input and output files

            reader = new BufferedReader(new FileReader("./" + fileName));
            writer = new BufferedWriter(new FileWriter("./" + fileName + "_LLCD.txt"));

            //Calculates the number of lines (values)

            String line = reader.readLine();
            int numOfLines = 0;

            while (line != null) {
                numOfLines++;
                line = reader.readLine();
            }
            System.out.print("\n Number of values: " + numOfLines + "\n");
            reader.close();

            //Reads the file again and stores the iat values in an array

            reader = new BufferedReader(new FileReader("./" + fileName));

            double dataset[] = new double[numOfLines];
            line = reader.readLine();
            int i = 0;

            while (line != null) {
                dataset[i] = Double.valueOf(line);
                line = reader.readLine();
                i++;
            }

            // Calculate minimum and maximum

            double minimum = dataset[0];
            double maximum = dataset[0];

            for (int j = 1; j < numOfLines; j++) {
                if (minimum > dataset[j])
                    minimum = dataset[j];

                if (maximum < dataset[j])
                    maximum = dataset[j];
            }

            System.out.print("\nMinimum value: " + minimum);
            System.out.print("\nMaximum value: " + maximum);

            ///////////////////////////// Main job //////////////////////////////////

            double min = Math.log(minimum) / Math.log(2);
            double max = Math.log(maximum) / Math.log(2);

            System.out.println();
            System.out.println("Log2(minimum)= " + min);
            System.out.println("Log2(maximum)= " + max);

            double intervalsLength = (max - min) / (numOfIntervals * 1.0);
            System.out.println("Intervals Length= " + intervalsLength);

            // Defining and calculating intervals

            double [][] intervals = new double[numOfIntervals][3];

            intervals[0][0] = Math.floor(min);
            intervals[0][1] = 0;
            intervals [0][2] = 0;
            intervals[numOfIntervals - 1][0] = Math.floor(max);
            intervals[numOfIntervals - 1][1] = 0;
            intervals[numOfIntervals - 1][2] = 0;

            for (int j = 1; j < numOfIntervals - 1; j++) {
                intervals[j][0] = intervals[j-1][0] + intervalsLength;
                for (int k = 1; k < 3; k++) {
                    intervals[j][k] = 0;
                }
            }

            for (int j = 0; j < numOfLines; j++){
                for (int k = 0; k < numOfIntervals - 1; k++){
                    if ((intervals[k][0] <= ((Math.log(dataset[j]))/Math.log(2)) && (((Math.log(dataset[j]))/Math.log(2))) < intervals[k+1][0])){
                        intervals[k][1]++;
                    }
                }
                if (intervals[numOfIntervals-1][0] <= (Math.log(dataset[j])/Math.log(2)))
                    intervals[numOfIntervals - 1][1]++;
            }

            intervals[0][2] = intervals[0][1] / numOfLines;
            for (int j = 1; j < numOfIntervals; j++){
                intervals[j][2] = (intervals[j][1] / numOfLines) + intervals[j-1][2];
            }

            System.out.println();
            for (int j = 0; j < numOfIntervals; j++){
                System.out.println(intervals[j][0] + "\t" + intervals[j][1] + "\t" + intervals[j][2]);
                writer.write(intervals[j][0] + "\t" + intervals[j][1] + "\t" + intervals[j][2] + "\n");
            }

            reader.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
