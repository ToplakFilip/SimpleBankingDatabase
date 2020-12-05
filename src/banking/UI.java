package banking;

import java.util.Scanner;

public class UI {

    private final Scanner scan = new Scanner(System.in);
    private final AccountStorage accountStorage = new AccountStorage();
    private final CardCreation cardCreation = new CardCreation();

    protected void Start() {

        boolean programRunning = true;
        while (programRunning) {
            System.out.println("1 - Create an account \n2 - Log into account \n0 - Exit");
            String input = scan.nextLine();
            System.out.print("\n");

            switch (input) {
                case "1":
                    System.out.println("Your card has been created");
                    CreditCard card = cardCreation.createNewAccount();
                    this.accountStorage.addAccount(card);
                    System.out.print("\n");

                    break;
                case "2":
                    System.out.println("Enter your card number: ");
                    Long enteredNumber = scan.nextLong();
                    System.out.println("Enter your PIN: ");
                    String enteredPin = scan.next();
                    scan.nextLine();
                    System.out.print("\n");

                    if (this.accountStorage.logInValidation(enteredNumber, enteredPin)) {
                        System.out.println("You have successfully logged in!\n");
                        boolean quitProgram = cardBalanceUI(enteredPin);
                        if (quitProgram) {
                            programRunning = false;
                        }
                    } else {
                        System.out.println("Wrong card number or PIN!\n");
                    }
                    break;
                case "0":
                    programRunning = false;
                    break;
                default:
                    System.out.println("Incorrect input");
                    break;
            }
        }
    }

    private boolean cardBalanceUI(String PIN) {
        while (true) {
            System.out.println("1 - Balance \n2 - Log out \n0 - Exit");
            String input = scan.nextLine();

            switch (input) {
                case "1":
                    System.out.println("\nBalance: " + accountStorage.printAccBalance(PIN) + "\n");
                    break;
                case "2":
                    System.out.println("\nYou have successfully logged out!\n");
                    return false;
                case "0":
                    return true;
                default:
                    System.out.println("Incorrect input");
                    break;
            }
        }
    }

}
