package banking;

import java.util.HashSet;
import java.util.Random;
import java.lang.Math;

public class CardCreation {

    private final Random rand = new Random();
    private HashSet<String> duplicateCheck = new HashSet<>();

    protected CreditCard createNewAccount() {
        Long cardNum = createNewCreditCard();
        System.out.println("Your card number:\n" + cardNum);
        String pin = createNewPIN();
        System.out.println("Your card PIN:\n" + pin);

        return new CreditCard(pin, cardNum);
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

    private Long createNewCreditCard() {
        StringBuilder builder = new StringBuilder();

        builder.append("400000");
        int number = rand.nextInt(899999999)+100000000;
        builder.append(number);

        builder.append(checkSum(builder.toString()));
        return Long.parseLong(builder.toString());
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
