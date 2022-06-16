/*  Name:   
     Course: CNT 4714 Summer 2022 
     Assignment title: Project 1 – Synchronized, Cooperating Threads Under Locking 
     Due Date: June 5, 2022 
*/ 

package project2;

import java.io.IOException;
import java.util.Random;
import project2.BankAccount;

public class WithdrawalAgent implements Runnable {
    private static Random generator = new Random();
    private BankAccount bankAccount;
    private String agentName;
    static private int instanceCounter = 0;
    private int agentNumber;

    public WithdrawalAgent(BankAccount account) {
        bankAccount = account;
        agentNumber = instanceCounter;
        instanceCounter++;
        agentName = String.format("WT%d", agentNumber);
    }

    public void run(){
        try {
            Thread.sleep(generator.nextInt(2));
            bankAccount.withdrawal(generator.nextInt(98) + 1, agentName);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
