public class User {
    private String accountNumber;
    private String pin;
    private double balance;

    // Constructor
    public User(String accountNumber, String pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
    }

    // Getter methods
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    // Method to update balance
    public void updateBalance(double amount) {
        balance += amount;
    }
}
