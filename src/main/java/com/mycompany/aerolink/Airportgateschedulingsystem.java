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
        private void searchFlight() {
        System.out.println();
        System.out.println("SEARCH FOR A FLIGHT");
        System.out.println(SINGLE_LINE);
        Flight flight = findFlightFromUser();
        if (flight != null) {
            showFlight(flight);
        }
    }
        
        private void updateFligh(){
        System.out.println();
        System.out.println("UPDATE FLIGHT DETAILS");
        System.out.println(SINGLE_LINE);
        Flight flight = findFlightFromUser();
        if (flight == null) {
            return;
        }
        showFlight(flight);
        System.out.println("Press ENTER to keep the current value shown in [brackets].");
        System.out.println("The Flight ID and flight category cannot be changed.");
        System.out.println();
        
        String airline = readOptionalText("Airline name [" + flight.getAirlineName() + "]:",Flight.getAirlineName());
}       String destination = readOptionalText("Destination [" + flight.getDestination() + "]: ", flight.getDestination());

        String departureTime = flight.getDepartureTime();
        if (gateSchedule.isFlightScheduled(flight.getFlightId())) {
            System.out.println("Departure time [" + departureTime + "]: this flight has a gate allocation,"
                    + " use 'Reschedule a Flight' to change its time.");
        } else if (readYesNo("Change the departure time [" + departureTime + "]? (Y/N): ")) {
            departureTime = GateSchedule.TIME_SLOTS[readTimeSlot("New departure time slot")];
        }
 
        // Cargo may have 0 seats; passenger flights need at least 1.
        int minimumCapacity = flight.getCategory() == FlightCategory.CARGO ? 0 : 1;
        int capacity = readOptionalInt("Passenger capacity [" + flight.getPassengerCapacity() + "]: ",
                flight.getPassengerCapacity(), minimumCapacity, 1000);
 
        int booked = flight.getBookedPassengers();
        // Lowering the capacity must not leave more bookings than seats.
        if (booked > capacity) {
            System.out.println("The current bookings (" + booked + ") exceed the new capacity,"
                    + " so the booked passengers must be re-entered.");
            booked = readInt("Number of booked passengers (0 - " + capacity + "): ", 0, capacity);
        } else {
            booked = readOptionalInt("Booked passengers [" + booked + "] (0 - " + capacity + "): ",
                    booked, 0, capacity);
        }
 
        FlightStatus status = flight.getStatus();
        if (readYesNo("Change the flight status [" + status + "]? (Y/N): ")) {
            status = readStatus("New flight status");
        }
 
        if (flight instanceof InternationalFlight) {
            InternationalFlight international = (InternationalFlight) flight;
            international.setDepartureTerminal(readOptionalText(
                    "Departure terminal [" + international.getDepartureTerminal() + "]: ",
                    international.getDepartureTerminal()));
            String customs = international.isCustomsClearanceRequired() ? "Yes" : "No";
            if (readYesNo("Change customs clearance required [" + customs + "]? (Y/N): ")) {
                international.setCustomsClearanceRequired(!international.isCustomsClearanceRequired());
            }
        }
 
        // FlightManager checks the values again and then applies the changes.
        boolean updated = flightManager.updateFlight(flight.getFlightId(), airline, destination,
                departureTime, capacity, booked, status);
        if (!updated) {
            System.out.println("ERROR: The flight could not be updated because a value was invalid.");
            return;
        }
 
        // Assumption: a cancelled flight must release its gate allocation.
        if (status == FlightStatus.CANCELLED && gateSchedule.releaseGate(flight.getFlightId())) {
            clearBoardingGate(flight);
            System.out.println("The flight was cancelled, so its gate allocation has been released.");
        }
        System.out.println();
        System.out.println("Flight " + flight.getFlightId() + " updated successfully.");
        showFlight(flight);
    }
 
        private void deleteFlight() {
        System.out.println();
        System.out.println("DELETE A FLIGHT");
        System.out.println(SINGLE_LINE);
        Flight flight = findFlightFromUser();
        if (flight == null) {
            return;
        }
        showFlight(flight);
        // Ask for confirmation because a delete cannot be undone.
        if (!readYesNo("Are you sure you want to delete flight " + flight.getFlightId() + "? (Y/N): ")) {
            System.out.println("Delete cancelled. The flight was not removed.");
            return;
        }
 
        // A deleted flight may not stay behind in the gate schedule.
        boolean released = gateSchedule.releaseGate(flight.getFlightId());
        if (flightManager.deleteFlight(flight.getFlightId())) {
            System.out.println("Flight " + flight.getFlightId() + " deleted successfully.");
            if (released) {
                System.out.println("Its gate allocation has also been released.");
            }
        } else {
            System.out.println("ERROR: The flight could not be deleted.");
        }
    }
 