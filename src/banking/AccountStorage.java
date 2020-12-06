package banking;

import java.sql.*;
import org.sqlite.SQLiteDataSource;
import java.sql.PreparedStatement;

public class AccountStorage {

    private final SQLiteDataSource dataSource;
    private int index;
    private final String url;


    AccountStorage(String input) {
        this.url = "jdbc:sqlite:" + input;

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
            try (Statement statement = con.createStatement()) {
                // id, number, PIN, balance
                int i = statement.executeUpdate("INSERT INTO card(id, number, pin, balance) VALUES " +
                        "(" + this.index + ", '" + card.getCardNumber() + "', '" +
                        card.getPIN() + "', " + card.getBalance() + ")");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        index++;

    }

    protected boolean logInValidation(String enteredPin, String enteredNumber) {
        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT COUNT(pin) FROM card WHERE pin = '" + enteredPin + "' AND " +
                    "number = '" + enteredNumber + "'";

            PreparedStatement prep = con.prepareStatement(query);
            ResultSet RS = prep.executeQuery();
            if (RS.getBoolean(1)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected int printAccBalance(String PIN) {
        try (Connection con = dataSource.getConnection()) {
            String query = "SELECT balance FROM card WHERE pin = '" + PIN + "'";

            PreparedStatement prep = con.prepareStatement(query);
            ResultSet RS = prep.executeQuery();
            if(RS.next()) {
                return RS.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("something is wrong");
        return 0;
    }

    void printit() {
        this.dataSource.setUrl(this.url);

        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet cardList = statement.executeQuery("SELECT * FROM card")) {
                    System.out.println("LETS PRINT: ");
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
