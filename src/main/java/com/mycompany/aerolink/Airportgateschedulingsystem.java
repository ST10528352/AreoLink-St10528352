package com.mycompany.aerolink;
 
import java.util.NoSuchElementException;
import java.util.Scanner;
 
/**
 * AeroLink International Airport - Airport Gate Scheduling System.
 *
 * Console based, menu driven application that lets airport operations staff
 * manage flight records, allocate departure gates and view operational
 * reports. All information is kept in memory while the program is running.
 */
public class AirportGateSchedulingSystem {
    
    private static final String DOUBLE_LINE = "=============================================";
    private static final String SINGLE_LINE = "---------------------------------------------";
 
    // One Scanner is shared by the whole program to read keyboard input (Oracle, n.d.c).
    private final Scanner input = new Scanner(System.in);
    private final FlightManager flightManager = new FlightManager();
    private final GateSchedule gateSchedule = new GateSchedule();
    private final ReportGenerator reports = new ReportGenerator(flightManager, gateSchedule);
    
    public static void main(String[] args){
        AirportGateSchedulingSystem system = new AirportGateSchedulingSystem();
        try{
             system.run();         
        } catch( NoSuchElementException e){
        // The input stream was closed, so there is nothing more to read.
            System.out.println();
            System.out.println("Input closed. Exiting the system.");
        }
    }
    
    //=======================================================================
    //Menus
    //=======================================================================
    
    private void run(){
        boolean running = true;
        while (running){
            
            System.out.println();
            System.out.println(DOUBLE_LINE);
            System.out.println("      AEROLINK INTERNATIONAL AIRPORT");
            System.out.println("      AIRPORT GATE SCHEDULING SYSTEM");
            System.out.println(DOUBLE_LINE);
            System.out.println(" 1. Flight Management");
            System.out.println(" 2. Gate Scheduling Management");
            System.out.println(" 3. Airport Operational Reports");
            System.out.println(" 4. Load Sample Data");
            System.out.println(" 0. Exit");
            System.out.println(SINGLE_LINE);
            
            switch(readInt("Select an option: ", 0,4)){
                case 1:
                    flightMenu();
                    break;
                case 2:
                    gateMenu();
                    break;
                case 3;
                    reportMenu();
                    break;
                case 4;
                    loadSampleData();
                    break:
                default:
                    running = false;
                    System.out.println();
                    System.out.println("Thank you for using the AeroLink Gate Scheduling System GoodBye!");
            }
        }
    }
    
    private void flightMenu(){
        boolean back = false;
        while(!back){
            System.out.println();
            System.out.println(DOUBLE_LINE);
            System.out.println("            FLIGHT MANAGEMENT");
            System.out.println(DOUBLE_LINE);
            System.out.println(" 1. Register a New Flight");
            System.out.println(" 2. Search for a Flight");
            System.out.println(" 3. Update Flight Details");
            System.out.println(" 4. Delete a Flight");
            System.out.println(" 5. Display All Flights");
            System.out.println(" 0. Back to Main Menu");
            System.out.println(SINGLE_LINE);
            
            switch(readInt("Select an option: ", 0, 5)){
                case 1:
                   registerFlight();
                    break;
                case 2:
                    searchFlight();
                    break;
                case 3:
                    updateFlight();
                    break:
                case 4:
                    deleteFlight();
                    break;
                case 5:
                    reports.displayAllFlights();
                    break;
                default:
                    back = true;
                
            }
        }
    }
}