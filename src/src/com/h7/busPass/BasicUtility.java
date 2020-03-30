package com.h7.busPass;

import java.util.Scanner;

public class BasicUtility {

    static Scanner inp = new Scanner(System.in);

    /*
     * This method will check and return the user input for bus registration number where
     * length should be >=4 ..
     */
    static String regNumberCheck() {
        String regNumber = "";

        while (regNumber.length() != 4) {
            System.out.print("Please provide valid 4 digit Reg Number: ");
            regNumber = inp.nextLine();
        }
        return regNumber;
    }
}
