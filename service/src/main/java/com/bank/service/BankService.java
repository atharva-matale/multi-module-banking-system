package com.bank.service;
import com.bank.core.Account;
public class BankService {
    private final Account account = new Account();
    public synchronized double deposit(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        account.setBalance(account.getBalance() + amount);
        return account.getBalance();
    }
    public synchronized double withdraw(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (amount > account.getBalance()) throw new IllegalArgumentException("Insufficient balance");
        account.setBalance(account.getBalance() - amount);
        return account.getBalance();
    }
    public synchronized double balance() { return account.getBalance(); }
}
