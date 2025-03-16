package com.suyog.Wallet;

import java.util.Scanner;
public class App 
{
    public static void main( String[] args )
    {
    	 Scanner scanner=new Scanner(System.in);
         walletService walletService=new walletService();
         Hibernate hibernate=new Hibernate();
         AutoPayScheduler scheduler=new AutoPayScheduler(hibernate.getSessionFactory());
         
         while(true){
             System.out.println("\n=== Wallet Management System ===");
             System.out.println("1. Register ");
             System.out.println("2. Login ");
             System.out.println("3. Exit ");
             System.out.println("Enter your choice: ");
             int choice=scanner.nextInt();

             switch(choice){
                 case 1:
                     walletService.registerUser();
                     break;

                 case 2:
                     walletService.loginUser();
                     break;

                 case 3:
                     System.out.println("Exiting...");
                     break;
                 default:
                     System.out.println("Invalid choice");
         
             }
             
         }
     }
}
