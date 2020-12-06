package banking;

import java.util.Scanner;

public class UI {

    private final Scanner scan = new Scanner(System.in);
    private AccountStorage accountStorage;
    private final CardCreation cardCreation = new CardCreation();

    protected void Start() {

        System.out.println("  ______   __                          __        _______                       __       \n" +
                " /      \\ |  \\                        |  \\      |       \\                     |  \\      \n" +
                "|  $$$$$$\\ \\$$ ______ ____    ______  | $$      | $$$$$$$\\  ______   _______  | $$   __ \n" +
                "| $$___\\$$|  \\|      \\    \\  /      \\ | $$      | $$__/ $$ |      \\ |       \\ | $$  /  \\\n" +
                " \\$$    \\ | $$| $$$$$$\\$$$$\\|  $$$$$$\\| $$      | $$    $$  \\$$$$$$\\| $$$$$$$\\| $$_/  $$\n" +
                " _\\$$$$$$\\| $$| $$ | $$ | $$| $$  | $$| $$      | $$$$$$$\\ /      $$| $$  | $$| $$   $$ \n" +
                "|  \\__| $$| $$| $$ | $$ | $$| $$__/ $$| $$      | $$__/ $$|  $$$$$$$| $$  | $$| $$$$$$\\ \n" +
                " \\$$    $$| $$| $$ | $$ | $$| $$    $$| $$      | $$    $$ \\$$    $$| $$  | $$| $$  \\$$\\\n" +
                "  \\$$$$$$  \\$$ \\$$  \\$$  \\$$| $$$$$$$  \\$$       \\$$$$$$$   \\$$$$$$$ \\$$   \\$$ \\$$   \\$$\n" +
                "                            | $$                                                        \n" +
                "                            | $$                                                        \n" +
                "                             \\$$                                                        ");

        this.accountStorage = new AccountStorage();
        // accountStorage.printit(); PRINTS EVERY ACCOUNT

        boolean programRunning = true;
        while (programRunning) {
            System.out.println("1 - Create an account \n2 - Log into account \n0 - Exit");
            String input = scan.nextLine();
            System.out.print("\n");

            switch (input) {
                case "1":
                    System.out.println("[ Your card has been created ]\n");
                    CreditCard card = cardCreation.createNewAccount();
                    this.accountStorage.addAccount(card);
                    System.out.print("\n");

                    break;
                case "2":
                    System.out.println("Enter your card number: ");
                    String enteredNumber = scan.next();
                    System.out.println("Enter your PIN: ");
                    String enteredPin = scan.next();
                    scan.nextLine();
                    System.out.print("\n");

                    if (this.accountStorage.logInValidation(enteredPin, enteredNumber)) {
                        System.out.println("[ Successfully logged in ]\n");
                        boolean quitProgram = cardBalanceUI(enteredPin, enteredNumber);
                        if (quitProgram) {
                            programRunning = false;
                        }
                    } else {
                        System.out.println("! Wrong card number or PIN !\n");
                    }
                    break;
                case "0":
                    programRunning = false;
                    break;
                default:
                    System.out.println("! Incorrect input !");
                    break;
            }
        }
    }

    private boolean cardBalanceUI(String PIN, String enteredNumber) {
        while (true) {
            System.out.println("1 - Balance \n2 - Add income " +
                    "\n3 - Transfer income \n4 - Close account" +
                    "\n5 - Log out \n0 - Exit");
            String input = scan.nextLine();

            switch (input) {
                case "1":
                    System.out.println("\nBalance: " + accountStorage.printAccBalance(PIN, enteredNumber) + "\n");
                    break;
                case "2":
                    System.out.println("\nEnter income:");
                    int income = scan.nextInt();
                    scan.nextLine();
                    accountStorage.addIncomeToAccount(PIN, enteredNumber, income);
                    break;
                case "3":
                    System.out.println("\nTransfer\nEnter card number:");
                    String receiverNumber = scan.nextLine();
                    if (accountStorage.accountCheck(receiverNumber)) {
                        if (receiverNumber.equals(enteredNumber)) {
                            System.out.println("! Can't transfer money to the same account !");
                            break;
                        }
                        System.out.println("Enter how much money you want to transfer:");
                        income = scan.nextInt();
                        scan.nextLine();
                        if (accountStorage.transferIncome(PIN, enteredNumber, receiverNumber, income)) {
                            System.out.println("[ Success ]\n");
                        }
                    }
                    break;
                case "4":
                    while(true) {
                        System.out.println("Are you sure you want to close your account?(Y/N)");
                        String answer = scan.nextLine();
                        if (answer.equals("Y")) {
                            accountStorage.removeAccount(PIN, enteredNumber);
                            System.out.println("\n[ Your account has been permanently closed ]\n");
                            return false;
                        } else if (answer.equals("N")) {
                            System.out.println("\n[ Action terminated ]\n");
                            break;
                        } else {
                            System.out.println("! Incorrect input !");
                        }
                    }
                    break;
                case "5":
                    System.out.println("\n[ Successfully logged out ]\n");
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
