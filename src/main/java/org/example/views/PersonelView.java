package org.example.views;

import java.time.LocalDate;
import java.util.Scanner;

public class PersonelView {
    Scanner userInput = new Scanner(System.in);

    public String getFirstName(){
        System.out.println("Enter your firstName: ");
        return userInput.nextLine().trim();
    }

    public String getMiddleName(){
        System.out.println("Enter your middleName or (Press Enter to skip): ");
        String input = userInput.nextLine().trim();
        return input.isEmpty() ? null : input;
    }

    public String getLastName(){
        System.out.println("Enter your lastName: ");
        return userInput.nextLine().trim();
    }

    public LocalDate getDate(){
        int year, month, date;
        LocalDate InputDate = null;

        while(true){
            try {
                System.out.println("Enter your DateOfBirth:");
                System.out.println("Enter the year: ");
                year = userInput.nextInt();
                userInput.nextLine();

                System.out.println("Enter the month: ");
                month = userInput.nextInt();
                userInput.nextLine();

                System.out.println("Enter the date");
                date = userInput.nextInt();
                userInput.nextLine();

                InputDate =  LocalDate.of(year, month, date);
                break;

            } catch (Exception e){
                System.out.println("Invalid: Please make sure you have entered a valid Date.");
            }
        }

        return  InputDate;

    }

    public String getContactNumber(){
        System.out.println("Enter your telephoneNumber +256XXXXXXXXX: ");
        return userInput.nextLine().trim();
    }

    public String getEmail(){
        System.out.println("Enter your Email Address: ");
        return userInput.nextLine().trim();
    }

    public Long getID(){
        System.out.println("Enter the ID: ");
        return userInput.nextLong();
    }




}
