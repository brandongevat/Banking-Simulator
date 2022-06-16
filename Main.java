/*  Name:   
     Course: CNT 4714 Summer 2022 
     Assignment title: Project 1 – Synchronized, Cooperating Threads Under Locking 
     Due Date: June 5, 2022 
*/ 

package project2;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import project2.DepositAgent;
import project2.SynchronizedBankAccount;
import project2.WithdrawalAgent;

class Main {
    public static void main(String[] args) throws IOException{

        // Initalize account. 
        SynchronizedBankAccount account = new SynchronizedBankAccount();

        // Initalize ArrayLists for depositors and agents.
        ArrayList<DepositAgent> depositAgents = new ArrayList<DepositAgent>();
        ArrayList<WithdrawalAgent> withdrawalAgents = new ArrayList<WithdrawalAgent>();

        // Initialize application using 15 threads.
        ExecutorService application = Executors.newFixedThreadPool(15);

        // Begin formatting output.
        System.out.println("Deposit Agents\t\t\tWithdrawal Agents\t\t\t\tBalance");
        System.out.println("--------------\t\t\t-----------------\t\t\t---------------");

        // Intialize agents into their respective array lists.
        for (int i = 0; i < 10; i++) {
            withdrawalAgents.add(new WithdrawalAgent(account));
            if (i < 5) {
                depositAgents.add(new DepositAgent(account));
            }
        }

        try
        {
            // Run the algorithm forever, until you cancel it from command line.
            while (true) {
                for (int i = 0; i < 10; i++) {
                    application.execute(withdrawalAgents.get(i));
                    if (i < 5)
                        application.execute(depositAgents.get(i));
                }
            }

        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

        // Shutdown application.
        application.shutdown();
    }
}
