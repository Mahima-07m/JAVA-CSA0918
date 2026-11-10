import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class InsufficientBalanceException extends Exception {
    InsufficientBalanceException(String message) { super(message); }
}
public class ATMWithdrawal {
    private static double balance = 5000.0;

    static void withdraw(String account, double amount) throws InsufficientBalanceException {
        if (account == null || !account.matches("\\d{6,12}"))
            throw new IllegalArgumentException("Invalid account number");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (amount > balance) throw new InsufficientBalanceException("Insufficient account balance");
        balance -= amount;
        System.out.println("Withdrawal successful. Balance: Rs " + balance);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Account number: ");
            String account = sc.nextLine();
            System.out.print("Withdrawal amount: ");
            double amount = Double.parseDouble(sc.nextLine());
            if (Files.exists(Path.of("card-reader.txt")))
                System.out.println("Card reader file found");
            else
                throw new IOException("Card-reader file is missing");
            withdraw(account, amount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: enter a numeric amount");
        } catch (IllegalArgumentException e) {
            System.out.println("Input error: " + e.getMessage());
        } catch (InsufficientBalanceException e) {
            System.out.println("Transaction declined: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Device error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            System.out.println("ATM session closed");
            sc.close();
        }
    }
}