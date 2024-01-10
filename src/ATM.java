import java.util.HashMap;
import java.util.Map;

public class ATM {
    private Map<String, User> users;

    public ATM() {
        users = new HashMap<>();
        users.put("123456", new User("123456", "1234", 1000.0));
        users.put("789012", new User("789012", "5678", 2000.0));
    }

    public User authenticateUser(String accountNumber, String pin) {
        if (users.containsKey(accountNumber)) {
            User user = users.get(accountNumber);
            if (user.getPin().equals(pin)) {
                return user;
            }
        }
        return null;
    }

    public void withdrawCash(User user, double amount) {
        if (amount > 0 && amount <= user.getBalance()) {
            user.updateBalance(-amount);
            System.out.println("Cash withdrawn successfully. Remaining balance: $" + user.getBalance());
        } else {
            System.out.println("Invalid amount or insufficient funds.");
        }
    }

    public void checkBalance(User user) {
        System.out.println("Account Balance: $" + user.getBalance());
    }

    public void depositMoney(User user, double amount) {
        if (amount > 0) {
            user.updateBalance(amount);
            System.out.println("Deposit successful. New balance: $" + user.getBalance());
        } else {
            System.out.println("Invalid amount for deposit.");
        }
    }

    public void transferFunds(User fromUser, User toUser, double amount) {
        if (toUser != null && amount > 0 && amount <= fromUser.getBalance()) {
            fromUser.updateBalance(-amount);
            toUser.updateBalance(amount);
            System.out.println("Funds transferred successfully.");
        } else {
            System.out.println("Invalid recipient account or insufficient funds for transfer.");
        }
    }
}
