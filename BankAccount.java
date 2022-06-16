/*  Name:   
     Course: CNT 4714 Summer 2022 
     Assignment title: Project 1 – Synchronized, Cooperating Threads Under Locking 
     Due Date: June 5, 2022 
*/ 

package project2;

public interface BankAccount {
    public void deposit(int amount, String agentName);
    public void withdrawal(int amount, String agentName);
    public void flag(int amount, String agentName, String transactionType);
}