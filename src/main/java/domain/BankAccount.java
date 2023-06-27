/**
 * The BankAccount class represents a bank account with a balance.
 * It provides methods for depositing and withdrawing funds from the account.
 */

package domain;

import Exceptions.InsufficientBalanceException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class BankAccount implements Serializable {
    private BigDecimal balance;

    /**
     * Constructs a new BankAccount object with a balance of zero.
     */
    public BankAccount() {
        this.balance = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Returns the current balance of the bank account.
     *
     * @return The balance of the bank account.
     */
    public BigDecimal getBalance() {
        return this.balance;
    }

    /**
     * Deposits the specified amount into the bank account.
     *
     * @param amount The amount to deposit.
     */
    public void deposit(BigDecimal amount) {
        if (amount != null) {
            this.balance = this.balance.add(amount).setScale(2, RoundingMode.HALF_UP);
        }
    }

    /**
     * Withdraws the specified amount from the bank account.
     *
     * @param amount The amount to withdraw.
     * @throws InsufficientBalanceException If the account does not have sufficient balance for the withdrawal.
     */
    public void withdraw(BigDecimal amount) throws InsufficientBalanceException {
        if (amount != null) {
            if (this.balance.subtract(amount).doubleValue() >= 0) {
                this.balance = this.balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
            } else {
                throw new InsufficientBalanceException();
            }
        }
    }

    /**
     * Returns a string representation of the bank account.
     *
     * @return A string representation of the bank account.
     */
    @Override
    public String toString() {
        return "BankAccount{" +
                "balance=" + balance +
                '}';
    }
}
