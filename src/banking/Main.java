package banking;

public class Main {

    public static void main(String[] args) {
        // write your code here
        //System.out.println(args[0]);
        if (args[0].equals("-filename")) {
            UI ui = new UI();
            ui.Start(args[1]);
        }
    }
}
