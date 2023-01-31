public class ReplaceExample {
    public static void main(String args[]){
        String s = "In CPSC department: Java is used for assignment 1 of CPSC441.";
        String sPrime = s.replace("CPSC" , "SENG");
        System.out.println(sPrime);
    }
}
