package org.example.views;

import java.time.LocalDate;
import java.util.Scanner;

public class AppointmentView {


    public LocalDate getAppointmentDate() {
        Scanner userInput = new Scanner(System.in);
        LocalDate inputDate = null;

        while (true) {
            try {
                System.out.println("Enter the appointment date:");

                System.out.print("Year (e.g., 2025): ");
                int year = userInput.nextInt();

                System.out.print("Month (1-12): ");
                int month = userInput.nextInt();

                System.out.print("Day (1-31): ");
                int day = userInput.nextInt();

                // Attempt to create the LocalDate
                inputDate = LocalDate.of(year, month, day);

                // If LocalDate.of succeeds, exit loop
                break;

            } catch (Exception e) {
                System.out.println("Invalid date. Please enter valid numeric values for year, month, and day.");
                userInput.nextLine(); // Clear bad input
            }
        }

        return inputDate;
    }

    public Long getAppointmentId(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the appointment id");
        return  scanner.nextLong();
    }

}
