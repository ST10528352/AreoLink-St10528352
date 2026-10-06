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
             // The menu repeats until the user chooses Exit (Farrell, 2023).
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
            
            // readInt only returns 0 to 4, and the switch runs the matching option (Farrell, 2023).
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
    private void gateMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println(DOUBLE_LINE);
            System.out.println("        GATE SCHEDULING MANAGEMENT");
            System.out.println(DOUBLE_LINE);
            System.out.println(" 1. Allocate a Gate to a Flight");
            System.out.println(" 2. Release a Gate Allocation");
            System.out.println(" 3. Reschedule a Flight");
            System.out.println(" 4. Display Complete Gate Schedule");
            System.out.println(" 5. Display Available Gates and Time Slots");
            System.out.println(" 6. Display Occupied Gates and Time Slots");
            System.out.println(" 0. Back to Main Menu");
            System.out.println(SINGLE_LINE);
 
            switch (readInt("Select an option: ", 0, 6)) {
                case 1:
                    allocateGate();
                    break;
                case 2:
                    releaseGate();
                    break;
                case 3:
                    rescheduleFlight();
                    break;
                case 4:
                    gateSchedule.displaySchedule();
                    break;
                case 5:
                    gateSchedule.displayAvailableSlots();
                    break;
                case 6:
                    gateSchedule.displayOccupiedSlots();
                    break;
                default:
                    back = true;
            }
        }
    }
        private void reportMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println(DOUBLE_LINE);
            System.out.println("       AIRPORT OPERATIONAL REPORTS");
            System.out.println(DOUBLE_LINE);
            System.out.println(" 1. Display All Scheduled Flights");
            System.out.println(" 2. Display All Delayed Flights");
            System.out.println(" 3. Airport Operations Report");
            System.out.println(" 4. Sort Flights by Flight ID");
            System.out.println(" 5. Sort Flights by Departure Time");
            System.out.println(" 0. Back to Main Menu");
            System.out.println(SINGLE_LINE);
 
            switch (readInt("Select an option: ", 0, 5)) {
                case 1:
                    reports.displayScheduledFlights();
                    break;
                case 2:
                    reports.displayDelayedFlights();
                    break;
                case 3:
                    reports.displayOperationsReport();
                    break;
                case 4:
                    reports.displayFlightsSortedById();
                    break;
                case 5:
                    reports.displayFlightsSortedByDepartureTime();
                    break;
                default:
                    back = true;
            }
        }
    }
    
        //============================================================
        //Feature 1; Flight managment
        //============================================================
        
        private void registerFlight(){
            System.out.println();
            System.out.println("REGISTER A NEW FLIGHT");
            System.out.println(SINGLE_LINE);
            
            string flightId = readText("Flight ID (e.g. FL101: ").toUpperCase();
            //Check for a dupicate starght away, before the user types the other details.
            if(flightManager.flightExists(flightId)){
                System.out.println("ERROR: A flight with the ID" + flightId + " already exists. Duplicate Flight IDs are not allowed.");
                    
                return;       
            }
            
            String airline = readText("Airline name: ");
            String destination = readText("Destination: ");
            String departureTime = GateScedule.TIME_SLOTS[readTimeSlot("Departure time slot")];
            FlightCategory catergory = readCategory();
        }
        
        int booked = 0;
        if(capacity > 0) {
        //Assumption: only a cargo flight may have a passenger capacity of 0.
        if(category == FlightCategory.CARGO){
           capacity = readInt("Passenger capacity(0 for a cargo-only flight): ", 0, capacity);
}
        Flight flight;
        //International flights use the subclass; the others use the base class (Oracle, n.d.e).
        if (category == FlightCategory.INTERNATIONAL){
        String terminal = readText("Departure terminal (e.g. Terminal A): ");
        boolean customs = readYesNo("Customs clearance required? (Y/N): ");
        flight = new InternationalFlight(flightId, airline, destination, departureTime, capacity, booked, status, terminal, InternationalFlight.NO)GATE,customs);
        } else {
            flight = new Flight(flightId, airline, destination, departureTime, capacity, booked,
                    category, status);
        }
 
        if (flightManager.registerFlight(flight)) {
            System.out.println();
            System.out.println("Flight " + flightId + " registered successfully.");
            System.out.println(SINGLE_LINE);
            flight.displayDetails();
        } else {
            System.out.println("ERROR: The flight could not be registered.");
        }
    }
}