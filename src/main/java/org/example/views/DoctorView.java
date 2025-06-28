package org.example.views;

import org.example.enums.Speciality;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DoctorView extends PersonelView {
    Scanner userInput = new Scanner(System.in);


    public Speciality getSpeciality() {
        System.out.println("Enter the number corresponding to the doctor's speciality:" +
                "\n1. GENERAL_MEDICINE" +
                "\n2. PEDIATRICS" +
                "\n3. GYNECOLOGY" +
                "\n4. ORTHOPEDICS" +
                "\n5. DERMATOLOGY");

        Speciality speciality;

        while (true) {
            try {
                System.out.print("Your choice: ");
                int index = userInput.nextInt();

                switch (index) {
                    case 1:
                        speciality = Speciality.GENERAL_MEDICINE;
                        break;
                    case 2:
                        speciality = Speciality.PEDIATRICS;
                        break;
                    case 3:
                        speciality = Speciality.GYNECOLOGY;
                        break;
                    case 4:
                        speciality = Speciality.ORTHOPEDICS;
                        break;
                    case 5:
                        speciality = Speciality.DERMATOLOGY;
                        break;
                    default:
                        System.out.println("Please enter a number between 1 and 5.");
                        continue;
                }
                return speciality;

            } catch (InputMismatchException e) {
                System.out.println("You did not enter an integer. Try again.");
                userInput.nextLine();
            }
        }

    }

    public Long getPatientId(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Patient Id");
        return scanner.nextLong();

    }

    public String writeDiagnosis(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the patient Diagnosis for the patient");
        return scanner.nextLine();
    }





}
