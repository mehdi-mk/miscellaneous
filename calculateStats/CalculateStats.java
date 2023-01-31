import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;

public class CalculateStats
{
   public static void main(String[] args)
   {  
      BufferedReader reader;
      BufferedWriter writer;
      BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));

      try
      {
         //Gets the name of the file
         
         System.out.print("Name of the file? ");
         String fileName = ob.readLine();
         
         //Opens the input and output files

         reader = new BufferedReader(new FileReader("./" + fileName));
         writer = new BufferedWriter(new FileWriter("./iatStats.txt"));
        
         //Calculates the number of lines (interarrivals)

         String line = reader.readLine();
         int numOfLines = 0;

         while(line != null)
         {
            numOfLines++;
            line = reader.readLine();
         }
         System.out.print("\nNumber of interarrivals: " + numOfLines + "\n\n" + "The Dataset:" + "\n");
         writer.write("Number of interarrivals: " + numOfLines + "\n");
         reader.close();
         
         //Reads the file again and stores the iat values in an array

         reader = new BufferedReader(new FileReader("./" + fileName));

         double dataset [] = new double [numOfLines];
         line = reader.readLine();
         int i = 0;

         while(line != null)
         {
            dataset[i] = Double.valueOf(line);
            line = reader.readLine();
            //System.out.print(dataset[i] + "\n");
            i++;
         }

         // Calculate minimum

         double minimum = dataset[0];
         double maximum = dataset[0];

         for (int j = 1; j < numOfLines; j++)
         {
            if (minimum > dataset[j])
               minimum = dataset[j];

            if (maximum < dataset[j])
               maximum = dataset[j];
         }

         System.out.print("\nMinimum: " + minimum);
         writer.write("\nMinimum: " + minimum);
         System.out.print("\nMaximum: " + maximum);
         writer.write("\nMaximum: " + maximum);

         // Calculate mean (average)

         double sum = 0, mean = 0;
         for (int j = 0; j < numOfLines; j++)
            sum =  sum + dataset[j];

         mean = sum / numOfLines;
         System.out.print("\nMean: " + mean);
         writer.write("\nMean: " + mean);
    
         // Calculate median (middle number)

         double median = 0;
         double pos1 = Math.floor((numOfLines - 1.0) / 2.0);
         double pos2 = Math.ceil((numOfLines - 1.0) / 2.0);
         
         if (pos1 == pos2 )
         {
            median = dataset[(int)pos1];
         }
         else
         {
            median = (dataset[(int)pos1] + dataset[(int)pos2]) / 2.0 ;
         }

         System.out.print("\nMedian: " + median);
         writer.write("\nMedian: " + median);
         
         // Calculate mode
         /*
         double mode = 0;
         int maxCount = 0;

         for (int h = 0; h < numOfLines; ++h) 
         {
            int count = 0;

            for (int j = 0; j < numOfLines; ++j) 
                if (dataset[j] == dataset[h])
                    ++count;

            if (count > maxCount) 
            {
                maxCount = count;
                mode = dataset[h];
            }
         }
         */
         HashMap<Double,Double> hm = new HashMap<Double,Double>();
         double max  = 1;
         double mode = 0;

         for(int h = 0; h < numOfLines; h++) 
         {

            if (hm.get(dataset[h]) != null) 
            {

               double count = hm.get(dataset[h]);
               count++;
               hm.put(dataset[h], count);

               if(count > max) 
               {
                  max  = count;
                  mode = dataset[h];
               }
            }

            else 
               hm.put(dataset[h],1.0);
         }

         System.out.print("\nMode: " + mode);
         writer.write("\nMode: " + mode);

         // Calculate Standard Deviation (SD)

         double variance = 0, stanDev = 0;

         for (int j = 0; j < numOfLines; j++)
         {
            variance = variance + Math.pow((dataset[j] - mean) , 2);
         }
         stanDev = Math.sqrt(variance / (numOfLines - 1));

         System.out.print("\nStandard Deviation: " + stanDev + "\n");
         writer.write("\nStandard Deviation: " + stanDev + "\n");

         writer.close();

         System.out.print("\nThese statistics are sorted and saved to the file 'iatStats.txt'\n");
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      
   }
}