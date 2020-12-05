package banking;

import java.util.Objects;

public class CreditCard {
    private final String PIN;
    private final Long cardNumber;
    private final int balance;


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
