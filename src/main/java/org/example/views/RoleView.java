package org.example.views;

import org.example.enums.Role;
import org.example.models.entities.User;

import java.util.InputMismatchException;
import java.util.Scanner;

public class RoleView {
    Scanner UserInput = new Scanner(System.in);

    public Role getRole(){
        System.out.println("Enter the role: " +
                "\n1. ADMIN" +
                "\n2. NURSE" +
                "\n3. RECEPTION");

        Role role;


        while (true){
            try {
                int index = UserInput.nextInt();
                switch (index){
                    case 1:
                        role = Role.ADMIN;
                        break;
                    case 2:
                        role = Role.NURSE;
                        break;
                    case 3:
                        role = Role.RECEPTION;
                        break;
                    default:
                        System.out.println("Eneter a number only optionos accepted are 1, 2, 3:");
                        continue;

                }

                return role;

            } catch (InputMismatchException e){
                System.out.println("Error in gettingRole: " + e.getMessage());
                UserInput.nextLine();
            }


        }

    }

}
