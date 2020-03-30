/*
 * This class will define the particular category of five seater vehicle..
 * @author (Ankush)
 * @version (Java 8)
 */

package com.h7.busPass;

public class FiveSeater extends Vehicle {

    // constructor of FiveSeater class
    public FiveSeater(String vehicleNumber) {
        this.capacity = 5;
        this.vehicleNumber = vehicleNumber;
    }
}
