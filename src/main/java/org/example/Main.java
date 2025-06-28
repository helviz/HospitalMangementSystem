package org.example;

import org.example.activity.PerformAdminActivity;
import org.example.activity.PerformDoctorActivity;
import org.example.activity.PerformReceptionActivity;
import org.example.controller.Login;
import org.example.enums.Role;
import org.example.models.entities.User;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Scanner userinput = new Scanner(System.in);
        boolean systemState = true;

        while(systemState) {
            System.out.println("*****************KAZEMINE HOSPITAL MANAGEMENT SYS*****************");

            User user;
            do {
                user = new Login().userLogin();
                System.out.println("Welcome to Kazemine Hospital Management System");
            } while(user == null);

//            Detecting the user Roles in the System.
            Role userRole = user.getRole();
            String role;
            if (userRole == Role.ADMIN) {
                role = "Admin";
            } else if (userRole ==Role.DOCTOR){
                role = "Doctor";
            } else if (userRole ==Role.RECEPTION){
                role = "Reception";
            } else {
                role = "Nurse";
            }


            switch (role){
                case "Admin":
                    systemState = new PerformAdminActivity(user).run(systemState);
                    break;
                case "Doctor":
                    systemState = new PerformDoctorActivity(user).run(systemState);
                    break;
                case "Reception":
                    systemState = new PerformReceptionActivity(user).run(systemState);
                    break;
                default:
                    System.out.println("You have no roles in the system");
            }
        }


    }



}