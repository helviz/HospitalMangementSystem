package org.example.views;

import java.util.Scanner;

public class PatientView extends PersonelView{

    public Long getPatientId(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Patient Id");
        return scanner.nextLong();

    }





}
