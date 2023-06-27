package Exceptions;

public class InsufficiantAmountException extends Exception {
    public InsufficiantAmountException() {
        super("Insufficient Amount of Crypto in Wallet!");
    }
}
