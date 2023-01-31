public class SplitExample {

    public static void main(String[] args) {
	    String s = "Java is used for the assignment 1 in CPSC441.";
        String[] words = s.split("t");
	    for (String w:words)
            System.out.println(w);
    }
}
