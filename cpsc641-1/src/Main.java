import java.io.*;
import java.util.*;
import java.text.*;

public class Main {

    public static void main(String[] args) {

        /////Input reader/////////////////////////////////////////////
        BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));
        /////File readers for two files of "small" and "big" dice/////
        BufferedReader smallReader, bigReader;

        String lineSmall, lineBig;
        int wins = 0, losses = 0, count = 0, value, point;
        double winsProb = 0.00;
        double lostProb = 0.00;

        DecimalFormat df = new DecimalFormat("#.##");

        /////A flag for a new game///////////////////////////////////
        boolean newGame = true;

        try {
            System.out.print("Do you want to run the trace-driven mode or the built-in PRNG? (t/p) ");
            String mode = ob.readLine();

            if (!mode.equalsIgnoreCase("t") && !mode.equalsIgnoreCase("p"))
                System.out.print("Wrong input!!");



            //=======================================================================================
            //=============================Trace-driven mode runs here===============================
            //=======================================================================================
            else if (mode.equalsIgnoreCase("t")) {
                System.out.print("Name of the file for small dice? ");
                String smallDiceFile = ob.readLine();
                System.out.print("Name of the file for big dice? ");
                String bigDiceFile = ob.readLine();

                smallReader = new BufferedReader(new FileReader("./" + smallDiceFile));
                bigReader = new BufferedReader(new FileReader("./" + bigDiceFile));

                lineSmall = smallReader.readLine();
                System.out.println("(Small) First line: " + lineSmall);
                lineBig = bigReader.readLine();
                System.out.println("(Big) First line: " + lineBig);
                count++;

                if (lineSmall == null || lineBig == null)
                    return;

                /////New game here here//////////////////////////////////////////////
                while (lineSmall != null && lineBig != null && newGame) {
                    value = Integer.valueOf(lineSmall) + Integer.valueOf(lineBig);
                    System.out.println("New game: " + value);

                    if (value == 7 || value == 11) {
                        System.out.println("First roll wins!");
                        wins++;
                        lineSmall = smallReader.readLine();
                        lineBig = bigReader.readLine();
                        count++;
                    } else if (value == 2 || value == 3 || value == 12) {
                        System.out.println("First roll loses!");
                        losses++;
                        lineSmall = smallReader.readLine();
                        lineBig = bigReader.readLine();
                        count++;
                    } else {
                        point = value;
                        System.out.println("Point here!!");
                        newGame = false;

                        lineSmall = smallReader.readLine();
                        lineBig = bigReader.readLine();
                        System.out.println("Next roll after point: " + lineSmall + " and " + lineBig);
                        count++;

                        /////Rolling the dice until point comes for winning or 7 comes for losing/////
                        while (lineSmall != null && !newGame) {
                            value = Integer.valueOf(lineSmall) + Integer.valueOf(lineBig);
                            System.out.println("Next roll after point (value): " + value);

                            if (value == 7) {
                                System.out.println("Loses after point!");
                                losses++;
                                newGame = true;
                            } else if (value == point) {
                                System.out.println("Wins after point!");
                                wins++;
                                newGame = true;
                            }
                            lineSmall = smallReader.readLine();
                            lineBig = bigReader.readLine();
                            System.out.println("Next roll again: " + lineSmall + " and " + lineBig);
                            count++;
                        }
                    }
                }
                /////Print the result here///////////////////////////////////////////////
                System.out.println();
                count--;
                int total = wins + losses;
                winsProb = (wins / ((double) (total))) * 100.00 ;
                lostProb = (losses / ((double) (total))) * 100.00;

                System.out.println("Number of wins = " + wins + " ===> Probability = " + df.format(winsProb) + "%");
                System.out.println("Number of losses = " + losses + " ===> Probability = " + df.format(lostProb) + "%");

                System.out.println("Number of rolling the dice = " + count);

                ob.close();
            }



            //================================================================================
            //================================PRNG Mode runs here=============================
            //================================================================================
            else if (mode.equalsIgnoreCase("p")) {
                String fairness;
                System.out.print("Enter a value: 'f' for two fair dice, 's' for single biased dice,");
                System.out.print(" 't' for two homogeneous biased dice, or 'h' for two heterogeneous biased dice: ");
                fairness = ob.readLine();

                if (!fairness.equalsIgnoreCase("f") && !fairness.equalsIgnoreCase("s")
                && fairness.equalsIgnoreCase("t") && fairness.equalsIgnoreCase("h"))
                    System.out.print("Wrong input!!");


                //============================Two fair dice====================================
                if (fairness.equalsIgnoreCase("f")) {
                    Random rand = new Random();
                    int smallRoll, bigRoll;
                    int games;
                    System.out.print("Enter the number of games: ");
                    String gameNum = ob.readLine();
                    games = Integer.valueOf(gameNum);

                    /////New games start here//////////////////////////////////////////////////
                    for (int i = 0; i < games; i++) {
                        smallRoll = rand.nextInt(6) + 1;
                        bigRoll = rand.nextInt(6) + 1;

                        value = smallRoll + bigRoll;

                        if (value == 7 || value == 11) {
                            wins++;
                            count++;
                        } else if (value == 2 || value == 3 || value == 12) {
                            losses++;
                            count++;
                        } else {
                            point = value;
                            newGame = false;

                            smallRoll = rand.nextInt(6) + 1;
                            bigRoll = rand.nextInt(6) + 1;
                            count++;

                            /////Rolling the dice until point comes for winning or 7 comes for losing/////
                            while (!newGame) {
                                value = smallRoll + bigRoll;

                                if (value == 7) {
                                    losses++;
                                    newGame = true;
                                } else if (value == point) {
                                    wins++;
                                    newGame = true;
                                }
                                smallRoll = rand.nextInt(6) + 1;
                                bigRoll = rand.nextInt(6) + 1;
                                count++;
                            }
                        }
                    }
                    /////Print the result here///////////////////////////////////////////////////
                    System.out.println();

                    winsProb = ((double) (wins) / ((double) (games))) * 100.00 ;
                    lostProb = ((double) (losses) / ((double) (games))) * 100.00;

                    count--;

                    System.out.println("Number of wins = " + wins + " ===> Probability = " + df.format(winsProb) + "%");
                    System.out.println("Number of losses = " + losses + " ===> Probability = " + df.format(lostProb) + "%");
                    System.out.println();
                    System.out.println("Average number of rolling the dice for each game = " + df.format(((double) (count)) / ((double) (games))));

                    ob.close();
                }


                //=============================Single biased dice===================================
                else if (fairness.equalsIgnoreCase("s")) {
                    Random rand = new Random();
                    int smallRoll, bigRoll;
                    double games;

                    System.out.print("Enter the number of games: ");
                    String gameNum = ob.readLine();
                    games = Double.valueOf(gameNum);

                    for (int v = 1; v <= 6; v++) {
                        for (double p = 0.04; p <= 0.40; p = p + 0.04) {

                            wins = losses = 0;

                            /*
                            **If you wanted to ask for value v of the biased dice and its probability p, from the
                            **user, enable this part, and disable two for loops above.

                            System.out.print("Enter the value of 'v' for the face that one dice (say small one) is biased to: ");
                            String biasedValue = ob.readLine();
                            v = Integer.valueOf(biasedValue);

                            System.out.print("Enter the value of 'p' for the probability of face v: ");
                            String biasedProb = ob.readLine();
                            p = Double.valueOf(biasedProb);*/

                            /////New games start here//////////////////////////////////////////////////
                            for (double i = 0; i < games; i++) {
                                smallRoll = biasedDice(v, p);
                                bigRoll = rand.nextInt(6) + 1;

                                value = smallRoll + bigRoll;

                                if (value == 7 || value == 11) {
                                    wins++;
                                    count++;
                                } else if (value == 2 || value == 3 || value == 12) {
                                    losses++;
                                    count++;
                                } else {
                                    point = value;
                                    newGame = false;

                                    smallRoll = biasedDice(v, p);
                                    bigRoll = rand.nextInt(6) + 1;
                                    count++;

                                    /////Rolling the dice until point comes for winning or 7 comes for losing/////
                                    while (!newGame) {
                                        value = smallRoll + bigRoll;

                                        if (value == 7) {
                                            losses++;
                                            newGame = true;
                                        } else if (value == point) {
                                            wins++;
                                            newGame = true;
                                        }
                                        smallRoll = biasedDice(v, p);
                                        bigRoll = rand.nextInt(6) + 1;
                                        count++;
                                    }
                                }
                            }
                            System.out.println();

                            System.out.println("'v' is: " + v + " and 'p' is: " + df.format(p));

                            winsProb = ((double) (wins) / ((double) (games))) * 100.00;
                            lostProb = ((double) (losses) / ((double) (games))) * 100.00;

                            count--;

                            System.out.println("Number of wins = " + wins + " ===> Probability = " + df.format(winsProb) + "%");
                            System.out.println("Number of losses = " + losses + " ===> Probability = " + df.format(lostProb) + "%");
                            System.out.println();
                            System.out.println("Average number of rolling the dice for each game = " + df.format(((double) (count)) / ((double) (games))));

                            ob.close();
                        }
                    }
                }


                //==============================Two homogeneous biased dice=================================
                else if (fairness.equalsIgnoreCase("t")) {
                    int smallRoll, bigRoll;
                    double games;

                    System.out.print("Enter the number of games: ");
                    String gameNum = ob.readLine();
                    games = Double.valueOf(gameNum);

                    for (int v = 1; v <= 6; v++) {
                        for (double p = 0.04; p <= 0.40; p = p + 0.04) {

                            wins = losses = 0;

                            /*
                            **If you wanted to ask for value v of the biased dice and its probability p, from the
                            **user, enable this part, and disable two for loops above.

                            System.out.print("Enter the value of 'v' for the face that two dice are homogeneously biased to: ");
                            String biasedValue = ob.readLine();
                            v = Integer.valueOf(biasedValue);

                            System.out.print("Enter the value of 'p' for the probability of face v: ");
                            String biasedProb = ob.readLine();
                            p = Double.valueOf(biasedProb);
                            */

                            /////New games start here//////////////////////////////////////////////////
                            for (double i = 0; i < games; i++) {
                                smallRoll = biasedDice(v, p);
                                bigRoll = biasedDice(v, p);

                                value = smallRoll + bigRoll;

                                if (value == 7 || value == 11) {
                                    wins++;
                                    count++;
                                } else if (value == 2 || value == 3 || value == 12) {
                                    losses++;
                                    count++;
                                } else {
                                    point = value;
                                    newGame = false;

                                    smallRoll = biasedDice(v, p);
                                    bigRoll = biasedDice(v, p);
                                    count++;

                                    /////Rolling the dice until point comes for winning or 7 comes for losing/////
                                    while (!newGame) {
                                        value = smallRoll + bigRoll;

                                        if (value == 7) {
                                            losses++;
                                            newGame = true;
                                        } else if (value == point) {
                                            wins++;
                                            newGame = true;
                                        }
                                        smallRoll = biasedDice(v, p);
                                        bigRoll = biasedDice(v, p);
                                        count++;
                                    }
                                }
                            }
                            System.out.println();

                            System.out.println("'v' is: " + v + " and 'p' is: " + df.format(p));

                            winsProb = ((double) (wins) / ((double) (games))) * 100.00;
                            lostProb = ((double) (losses) / ((double) (games))) * 100.00;

                            count--;

                            System.out.println("Number of wins = " + wins + " ===> Probability = " + df.format(winsProb) + "%");
                            System.out.println("Number of losses = " + losses + " ===> Probability = " + df.format(lostProb) + "%");
                            System.out.println();
                            System.out.println("Average number of rolling the dice for each game = " + df.format(((double) (count)) / ((double) (games))));

                            ob.close();
                        }
                    }
                }


                //==============================Two Heterogeneous biased dice==============================
                else if (fairness.equalsIgnoreCase("h")) {
                    int smallRoll, bigRoll;
                    double games, p_2;
                    System.out.print("Enter the number of games: ");
                    String gameNum = ob.readLine();
                    games = Double.valueOf(gameNum);

                    for (int v_1 = 1; v_1 <= 6; v_1++) {
                        for (int v_2 = 1; v_2 <= 6; v_2++) {
                            System.out.println();
                            System.out.println(v_1 + " " + v_2);
                            for (double p_1 = 0.04; p_1 <= 0.40; p_1 = p_1 + 0.04) {

                                p_2 = p_1;
                                wins = losses = 0;

                                /*
                                **If you wanted to ask for value v of the biased dice and its probability p, from the
                                **user, enable this part, and disable two for loops above.

                                System.out.print("Enter the value of 'v' for the face that the small dice is biased to: ");
                                String smallBiasedDiceValue = ob.readLine();
                                v_1 = Integer.valueOf(smallBiasedDiceValue);

                                System.out.print("Enter the value of 'p' for the probability of face v of the small dice: ");
                                String smallBiasedDiceProb = ob.readLine();
                                p_1 = Double.valueOf(smallBiasedDiceProb);

                                System.out.print("Enter the value of 'v' for the face that the big dice is biased to: ");
                                String bigBiasedDiceValue = ob.readLine();
                                v_2 = Integer.valueOf(bigBiasedDiceValue);

                                System.out.print("Enter the value of 'p' for the probability of face v of the big dice: ");
                                String bigBiasedDiceProb = ob.readLine();
                                p_2 = Double.valueOf(bigBiasedDiceProb);*/

                                /////New games start here//////////////////////////////////////////////////
                                for (double i = 0; i < games; i++) {
                                    smallRoll = biasedDice(v_1, p_1);
                                    bigRoll = biasedDice(v_2, p_2);

                                    value = smallRoll + bigRoll;

                                    if (value == 7 || value == 11) {
                                        wins++;
                                        count++;
                                    } else if (value == 2 || value == 3 || value == 12) {
                                        losses++;
                                        count++;
                                    } else {
                                        point = value;
                                        newGame = false;

                                        smallRoll = biasedDice(v_1, p_1);
                                        bigRoll = biasedDice(v_2, p_2);
                                        count++;

                                        /////Rolling the dice until point comes for winning or 7 comes for losing/////
                                        while (!newGame) {
                                            value = smallRoll + bigRoll;

                                            if (value == 7) {
                                                losses++;
                                                newGame = true;
                                            } else if (value == point) {
                                                wins++;
                                                newGame = true;
                                            }
                                            smallRoll = biasedDice(v_1, p_1);
                                            bigRoll = biasedDice(v_2, p_2);
                                            count++;
                                        }
                                    }
                                }
                                /*
                                **The outputs for this one were very confusing, so I trimmed them into printing out
                                **only the combinations of v1 and v2, and for different amounts of the probability p
                                ** for both dice, starting from 4% with steps equal to 4%, until 40% (10 different values).
                                */

                                //System.out.println();

                                //System.out.println("'v1' is: " + v_1 + ", 'v2' is: " + v_2 + ", and 'p' for both is: " + df.format(p_1));

                                winsProb = ((double) (wins) / ((double) (games))) * 100.00;
                                lostProb = ((double) (losses) / ((double) (games))) * 100.00;

                                count--;

                                System.out.println(df.format(winsProb) + "%");

                                /*
                                System.out.println("Number of wins = " + wins + " ===> Probability = " + df.format(winsProb) + "%");
                                System.out.println("Number of losses = " + losses + " ===> Probability = " + df.format(lostProb) + "%");
                                System.out.println();
                                System.out.println("Average number of rolling the dice for each game = " + df.format(((double) (count)) / ((double) (games))));

                                 */

                                ob.close();
                            }
                        }
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int biasedDice (int v, double p) {
        Random rand = new Random();
        double r = rand.nextDouble();
        int roll = 0;
        ArrayList<Integer> face = new ArrayList<Integer>();

        for (int i = 1; i <= 6; i++) {
            if (i != v) {
                face.add(i);
            }
        }
        double q = ((1.0000 - p) / 5.0000);

        if (r < q)
            roll = face.get(0);
        else if (r < (q + q))
            roll = face.get(1);
        else if (r < (q + q + q))
            roll = face.get(2);
        else if (r < (q + q + q + q))
            roll = face.get(3);
        else if (r < (q + q + q + q + q))
            roll = face.get(4);
        else
            roll = v;

        return roll;
    }
}
