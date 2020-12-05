package banking;

import java.util.HashMap;

public class AccountStorage {

    HashMap<String, CreditCard> accStorage = new HashMap<>();

    void addAccount(CreditCard card) {
        accStorage.put(card.getPIN(), card);
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