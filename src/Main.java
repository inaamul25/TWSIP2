import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private ATM atm = new ATM();
    private User currentUser;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bank App");

        // Create login scene
        Scene loginScene = createLoginScene(primaryStage);

        // Set the initial scene
        primaryStage.setScene(loginScene);

        // Show the stage
        primaryStage.show();
    }

    private Scene createLoginScene(Stage primaryStage) {
        VBox loginLayout = new VBox(10);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(20));

        Label titleLabel = new Label("ATM Interface");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField accountNumberField = new TextField();
        PasswordField pinField = new PasswordField();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            String accountNumber = accountNumberField.getText();
            String pin = pinField.getText();

            currentUser = atm.authenticateUser(accountNumber, pin);

            if (currentUser != null) {
                primaryStage.setScene(createTransactionScene());
            } else {
                showAlert("Authentication Failed", "Invalid account number or PIN.");
            }
        });

        loginLayout.getChildren().addAll(titleLabel, accountNumberField, pinField, loginButton);
        return new Scene(loginLayout, 300, 200);
    }

    private Scene createTransactionScene() {
        VBox transactionLayout = new VBox(10);
        transactionLayout.setAlignment(Pos.CENTER);
        transactionLayout.setPadding(new Insets(20));

        Label titleLabel = new Label("Welcome, " + currentUser.getAccountNumber() + "!");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button withdrawButton = new Button("Withdraw Cash");
        Button checkBalanceButton = new Button("Check Balance");
        Button depositButton = new Button("Deposit Money");
        Button transferButton = new Button("Transfer Funds");

        withdrawButton.setOnAction(e -> showTransactionDialog("Withdraw Cash", "Enter withdrawal amount: $", amount -> atm.withdrawCash(currentUser, amount)));
        checkBalanceButton.setOnAction(e -> showAlert("Account Balance", "Your balance is: $" + currentUser.getBalance()));
        depositButton.setOnAction(e -> showTransactionDialog("Deposit Money", "Enter deposit amount: $", amount -> atm.depositMoney(currentUser, amount)));
        transferButton.setOnAction(e -> showTransferDialog());

        transactionLayout.getChildren().addAll(titleLabel, withdrawButton, checkBalanceButton, depositButton, transferButton);
        return new Scene(transactionLayout, 400, 300);
    }

    private void showTransactionDialog(String title, String content, TransactionHandler handler) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(content);

        dialog.showAndWait().ifPresent(input -> {
            try {
                double amount = Double.parseDouble(input);
                handler.handleTransaction(amount);
                showAlert("Transaction Successful", title + " successful.");
            } catch (NumberFormatException | NullPointerException ex) {
                showAlert("Invalid Input", "Please enter a valid numeric value.");
            }
        });
    }

  private void showTransferDialog() {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Transfer Funds");
    dialog.setHeaderText(null);
    dialog.setContentText("Enter recipient's account number:");

    dialog.showAndWait().ifPresent(recipientAccountNumber -> {
        // Prompt for the recipient's PIN
        TextInputDialog pinDialog = new TextInputDialog();
        pinDialog.setTitle("Transfer Funds");
        pinDialog.setHeaderText(null);
        pinDialog.setContentText("Enter recipient's PIN:");

        pinDialog.showAndWait().ifPresent(recipientPIN -> {
            User recipientUser = atm.authenticateUser(recipientAccountNumber, recipientPIN);

            if (recipientUser != null) {
                showTransactionDialog("Transfer Funds", "Enter transfer amount: $", amount -> atm.transferFunds(currentUser, recipientUser, amount));
            } else {
                showAlert("Recipient Not Found", "Recipient account not found or invalid PIN.");
            }
        });
    });
}



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FunctionalInterface
    private interface TransactionHandler {
        void handleTransaction(double amount);
    }
}
