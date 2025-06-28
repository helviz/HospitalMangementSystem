package org.example.views;

import java.util.Scanner;

public class UserView {
    Scanner userinput = new Scanner(System.in);


    public String getUserPassword(){
        System.out.println("Enter the user password at least 8 characters, 1 uppercase, 1 lowercase, 1 digit, 1 special char");
        return userinput.nextLine();

    }
}
