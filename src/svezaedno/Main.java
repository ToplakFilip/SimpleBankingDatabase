package svezaedno;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        UI ui = new UI();
        ui.Start();
    }
}

class UI {

    private CreditCard card;
    private Scanner scan = new Scanner(System.in);
    private AccountStorage accountStorage = new AccountStorage();
    private CardCreation cardCreation = new CardCreation();

    protected void Start() {

        boolean programRunning = true;
        while (programRunning) {
            System.out.println("1 - Create an account \n2 - Log into account \n0 - Exit");
            String input = scan.nextLine();
            System.out.print("\n");

            if (input.equals("1")) {
                System.out.println("Your card has been created");
                CreditCard card = cardCreation.createNewAccount();
                this.accountStorage.addAccount(card);
                System.out.print("\n");

            } else if (input.equals("2")) {
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
            } else if (input.equals("0")) {
                programRunning = false;
            } else {
                System.out.println("Incorrect input");
            }
        }
    }

    private boolean cardBalanceUI(String PIN) {
        while (true) {
            System.out.println("1 - Balance \n2 - Log out \n0 - Exit");
            String input = scan.nextLine();

            if (input.equals("1")) {
                System.out.println("\nBalance: " + accountStorage.printAccBalance(PIN) + "\n");
            } else if (input.equals("2")) {
                System.out.println("\nYou have successfully logged out!\n");
                return false;
            } else if (input.equals("0")) {
                return true;
            } else {
                System.out.println("Incorrect input");
            }
        }
    }

}


class AccountStorage {

    HashMap<String, CreditCard> accStorage = new HashMap<>();


    void addAccount(CreditCard card) {
        accStorage.put(card.getPIN(), card);
        System.out.println(accStorage.size());
    }

    protected boolean logInValidation(Long enteredNumber, String enteredPin) {
        CreditCard compare = new CreditCard(enteredPin, enteredNumber);

        if (accStorage.containsKey(enteredPin)) {
            return accStorage.get(enteredPin).equals(compare);
        }
        return false;
    }

    protected int printAccBalance(String PIN) {
        return accStorage.get(PIN).getBalance();
    }

}

class CardCreation {

    private Random rand = new Random();
    private HashSet<String> duplicateCheck = new HashSet<>();

    protected CreditCard createNewAccount() {
        Long cardNum = createNewCreditCard();
        System.out.println("Your card number:\n" + cardNum);
        String pin = createNewPIN();
        System.out.println("Your card PIN:\n" + pin);

        return new CreditCard(pin, cardNum);
    }

    private Long createNewCreditCard() {
        StringBuilder builder = new StringBuilder();

        builder.append("400000");
        int number = rand.nextInt(899999999)+100000000;
        builder.append(number);
        builder.append(checkSum(builder.toString()));

        return Long.parseLong(builder.toString());
    }

    private String createNewPIN() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String number = String.valueOf(rand.nextInt(10));
            builder.append(number);
        }

        while (duplicateCheck.contains(builder.toString())) {
            builder = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                String number = String.valueOf(rand.nextInt(10));
                builder.append(number);
            }
        }

        duplicateCheck.add(builder.toString());
        return builder.toString();
    }

    private int checkSum(String initialNumber) {

        int doublingCounter = 1;
        int sum = 0;

        for (int i = initialNumber.length() - 1; i >= 0; i--) {
            int number = Integer.parseInt(String.valueOf(initialNumber.charAt(i)));
            if (doublingCounter % 2 == 1) {
                number = number * 2;
                while (number > 9) {
                    int firstDigit = Integer.parseInt(String.valueOf(String.valueOf(number).charAt(0)));
                    int secondDigit = Integer.parseInt(String.valueOf(String.valueOf(number).charAt(1)));
                    number = firstDigit + secondDigit;
                }
            }
            sum += number;
            doublingCounter++;
        }

        int broj = 10 - (sum % 10);
        if (broj == 10) broj = 0;
        return broj;

    }
}

class CreditCard {
    private String PIN;
    private Long cardNumber;
    private int balance;


    CreditCard(String PIN, Long cardNumber) {
        this.PIN = PIN;
        this.cardNumber = cardNumber;
        this.balance = 0;
    }

    int getBalance() {
        return balance;
    }

    String getPIN() {
        return PIN;
    }

    @Override
    public int hashCode() {
        return Objects.hash(PIN, cardNumber);
    }

    @Override
    public boolean equals(Object object) {

        if (object == this) {
            return true;
        }
        if (!(object instanceof CreditCard)) {
            return false;
        }
        CreditCard c = (CreditCard) object;

        return cardNumber.equals(c.cardNumber)
                && PIN.equals(c.PIN);
    }

    @Override
    public String toString() {
        return this.cardNumber + ", " + this.PIN;
    }
}

