/*  Name:   
     Course: CNT 4714 Summer 2022 
     Assignment title: Project 1 – Synchronized, Cooperating Threads Under Locking 
     Due Date: June 5, 2022 
*/

package project2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import project2.DepositAgent;

public class SynchronizedBankAccount implements BankAccount {
    private int balance = 0;

    private Lock accessLock = new ReentrantLock();
    private Condition canWithdrawal = accessLock.newCondition();
    private BufferedWriter bufferedWriter;
    Boolean occupied = false;

    SynchronizedBankAccount() {
        accessLock = new ReentrantLock();
        canWithdrawal = accessLock.newCondition();
    }

    public void deposit(int amount, String agentName) {

        try {
            accessLock.lock();
            if (amount < 1)
                System.out.println("Deposit may not be a negative value or less than $1"); // Lower bound.
            else if (amount > 499)
                System.out.println("Deposit may not exceed $500"); // Upper bound.
            else {
                balance += amount;
                System.out.printf("Agent %s deposits $%d", agentName, amount);
                displayState("+");
                if (amount > 350)
                    flag(amount, agentName, "Deposit"); // Money laundering flag.
            }
        } catch (Exception exception) {
            System.out.println();
            exception.printStackTrace();
        } finally {
            canWithdrawal.signalAll();
            accessLock.unlock();
        }

    }

    public void withdrawal(int amount, String agentName) {
        try {
            accessLock.lock();
            System.out.printf("\t\t\t\tAgent %s withdraws $%d", agentName, amount);
            if (amount < 1)
                System.out.println("Withdrawal cannot be a negative value or less than $1."); // Lower bound.
            else if (amount > 99)
                System.out.println("Withdrawal amount may not exceed $99."); // Upper bound.
            else if (amount > balance) {
                System.out.printf("\t\t\t(******) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS!!!\n", agentName, amount);
                canWithdrawal.await();
            } else {
                balance -= amount;
                displayState("-");
                if (amount > 75) {
                    flag(amount, agentName, "Withdrawal"); // Money laundering flag.
                }
            }
        } catch (InterruptedException exception) {
            System.out.println();
            exception.printStackTrace();
        } finally {
            accessLock.unlock();
        }
    }

    public void flag(int amount, String agentName, String transactionType) {
        
        System.out.printf(
                "\n* * * Flagged Transaction - %s Agent %s Made A %s In Excess of %d USD - See Flagged Transaction Log\n",
                transactionType, agentName, transactionType, (transactionType == "Withdrawal" ? 75 : 350));
        WriteToTransactionLog(amount, agentName, transactionType);
    }

    public void displayState(String operation) {
        if (operation == "+")
            System.out.printf("\t\t\t\t\t\t\t(%s)\tBalance is %d\n", operation, balance);
        else
            System.out.printf("\t\t\t(%s)\tBalance is %d\n", operation, balance);

    }

    public void WriteToTransactionLog(int amount, String agentName, String transactionType) {

        try {
            if (!occupied) {
                occupied = true;
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.txt", true));
                if (transactionType == "Withdrawal")
                    bufferedWriter.append("    ");
                LocalDate date = LocalDate.now();
                LocalTime time = LocalTime.now();
                bufferedWriter
                        .append(String.format("%s Agent %s issued %s of %d.00 at: ", transactionType, agentName,
                                transactionType, amount));
                bufferedWriter.append(date.toString() + " ");
                bufferedWriter.append(time.toString());
                bufferedWriter.newLine();
                bufferedWriter.close();
                occupied = false;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }
}