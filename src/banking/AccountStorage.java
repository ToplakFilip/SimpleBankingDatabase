package banking;

import java.sql.*;
import org.sqlite.SQLiteDataSource;
import java.sql.PreparedStatement;

public class AccountStorage {

    private final SQLiteDataSource dataSource;
    private int index;
    private final String url;


    AccountStorage() {
        this.url = "jdbc:sqlite:card.db";

        this.dataSource = new SQLiteDataSource();
        this.dataSource.setUrl(this.url);

        try (Connection con = dataSource.getConnection()) {
            //CREATE DATABASE IF IT DOESN'T EXIST
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER NOT NULL," +
                        "number VARCHAR(16)," +
                        "pin VARCHAR(4)," +
                        "balance INTEGER DEFAULT 0)");

                //COUNTING ID
                String query = "SELECT count(*) FROM card";
                ResultSet set = statement.executeQuery(query);
                set.next();
                this.index = set.getInt(1);

            } catch (SQLException e) {
                e.fillInStackTrace();
            }

        } catch (SQLException e) {
            e.fillInStackTrace();
        }

    }


    void addAccount(CreditCard card) {
        this.dataSource.setUrl(this.url);
        try (Connection con = dataSource.getConnection()) {

            String insertAcc = "INSERT INTO card(id, number, pin, balance) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = con.prepareStatement(insertAcc)) {
                // id, number, PIN, balance
                statement.setInt(1, this.index);
                statement.setString(2, card.getCardNumber());
                statement.setString(3, card.getPIN());
                statement.setInt(4, card.getBalance());
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        index++;
    }

    void removeAccount(String PIN, String enteredNumber) {
        this.dataSource.setUrl(this.url);
        try (Connection con = dataSource.getConnection()) {
            String removeAcc = "DELETE FROM card WHERE pin = ? AND number = ?";
            try (PreparedStatement statement = con.prepareStatement(removeAcc)) {
                // deleting account from the database
                statement.setString(1, PIN);
                statement.setString(2, enteredNumber);
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected boolean logInValidation(String enteredPin, String enteredNumber) {
        try (Connection con = dataSource.getConnection()) {

            String query = "SELECT COUNT(pin) FROM card WHERE pin = ? AND number = ?";

            PreparedStatement prep = con.prepareStatement(query);
            prep.setString(1, enteredPin);
            prep.setString(2, enteredNumber);

            ResultSet RS = prep.executeQuery();
            if (RS.getBoolean(1)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected int printAccBalance(String PIN, String number) {
        try (Connection con = dataSource.getConnection()) {
            String printingBalance = "SELECT balance FROM card WHERE pin = ? AND number = ?";
            try (PreparedStatement statement = con.prepareStatement(printingBalance)) {
                statement.setString(1, PIN);
                statement.setString(2, number);
                ResultSet RS = statement.executeQuery();
                if (RS.next()) {
                    return RS.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("something is wrong");
        return 0;

    }

    void addIncomeToAccount(String PIN, String number, int income) {
        this.dataSource.setUrl(this.url);
        try (Connection con = dataSource.getConnection()) {
            String insertIncome = "UPDATE card SET balance = balance + ? WHERE number = ? AND PIN = ?";
            try (PreparedStatement statement = con.prepareStatement(insertIncome)) {
                statement.setInt(1, income);
                statement.setString(2, number);
                statement.setString(3, PIN);
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    boolean accountCheck(String receiverNumber) {
        if (!luhnTest(receiverNumber)) {
            System.out.println("! Probably you made mistake in the card number. Please try again !\n");
            return false;
        }
        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT COUNT(number) FROM card WHERE number = ?";
            PreparedStatement prep = con.prepareStatement(query);
            prep.setString(1, receiverNumber);

            ResultSet RS = prep.executeQuery();
            if (RS.getBoolean(1)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("! Such a card does not exist !\n");
        return false;
    }

    boolean transferIncome(String PIN, String number, String receiverNumber, int income) {
        if (income > printAccBalance(PIN, number)) {
            System.out.println("! Not enough money !\n");
            return false;
        }

        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);

            String givingMoney = "UPDATE card SET balance = balance - ? WHERE number = ? AND pin = ?";
            String gettingMoney = "UPDATE card SET balance = balance + ? WHERE number = ?";

            try (PreparedStatement statement = con.prepareStatement(givingMoney);
                 PreparedStatement statement2 = con.prepareStatement(gettingMoney)) {
                //taking money from account
                statement.setInt(1, income);
                statement.setString(2, number);
                statement.setString(3, PIN);
                statement.executeUpdate();

                //getting money from account
                statement2.setInt(1, income);
                statement2.setString(2, receiverNumber);
                statement2.executeUpdate();

                con.commit();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean luhnTest(String creditNumber) {
        String number = creditNumber.substring(0, creditNumber.length() - 1);
        int luhnNumber = Integer.parseInt(String.valueOf(creditNumber.charAt(creditNumber.length() - 1)));

        int sum = 0;
        int counter = 0;
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(String.valueOf(number.charAt(i)));
            if (counter % 2 == 0) {
                digit *= 2;
                while (digit > 9) {
                    int firstDigit = Integer.parseInt(String.valueOf(String.valueOf(digit).charAt(0)));
                    int secondDigit = Integer.parseInt(String.valueOf(String.valueOf(digit).charAt(1)));
                    digit = firstDigit + secondDigit;
                }
            }
            sum += digit;
            counter++;
        }
        sum += luhnNumber;
        return sum % 10 == 0;
    }

    //printing info for easy debugging
    void printit() {
        this.dataSource.setUrl(this.url);

        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet cardList = statement.executeQuery("SELECT * FROM card")) {
                    System.out.println("PRINTING INFO: ");
                    while (cardList.next()) {
                        int id = cardList.getInt("id");
                        String number = cardList.getString(("number"));
                        String pin = cardList.getString("pin");

                        System.out.printf("ID %d : number [%s], PIN [%s]%n", id, number, pin);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
