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

    // ==================================================================
    // Feature 2: Gate scheduling
    // ==================================================================

private void allocateGate(){
    System.out.println();
    System.out.println("ALLOCATE A GATE TO A FLIGHT");
    System.out.println(SINGLE_LINE);
    
    //Nothing can be allocated when all 40 combinations are taken.
    
    if(gateSchedule.isFull()){
        System.out.println("ERROR: No gate and time-slot combinations are availble."
                + "Release a gate before alloecating  another flight.");
        
        return;
    }
    
    Flight fligth = findFlightFromUser();
    if (flight ==null) {
        return;
    }
    String flightId = flight.getFlightIf();
    
     // A flight may only be scheduled once.
        if (gateSchedule.isFlightScheduled(flightId)) {
            int[] position = gateSchedule.findFlight(flightId);
            System.out.println("ERROR: Flight " + flightId + " is already scheduled at gate "
                    + GateSchedule.GATES[position[0]] + " at " + GateSchedule.TIME_SLOTS[position[1]]
                    + ". Use 'Reschedule a Flight' to move it.");
            return;
        }
        if (flight.getStatus() == FlightStatus.CANCELLED || flight.getStatus() == FlightStatus.DEPARTED) {
            System.out.println("ERROR: A gate cannot be allocated to a flight that is "
                    + flight.getStatus() + ".");
            return;
        }
 
        gateSchedule.displaySchedule();
        System.out.println("Flight " + flightId + " is registered to depart at "
                + flight.getDepartureTime() + ".");
        // The user picks the gate (row) and the time slot (column).
        int gate = readGate("Gate");
        int slot = readTimeSlot("Departure time slot");
 
        if (!gateSchedule.isAvailable(gate, slot)) {
            System.out.println("ERROR: Gate " + GateSchedule.GATES[gate] + " is already occupied at "
                    + GateSchedule.TIME_SLOTS[slot] + " by flight " + gateSchedule.getFlightAt(gate, slot)
                    + ". Please choose another gate or time slot.");
            return;
        }
        
        if(gateSchedule.allocateGate(flightId, gate, slot)) {
            applyAllocation(flight, gate, slot);
            System.out.println("Flight " + flightId + " allocated to gate " + GateSchedule.GATES[gate] + "at" + GateSchedule.TIME_SLOTS[slots] + ",");
        } else {
            System.out.println("ERROR: The gate could not be allocated.");
        }
    }
    private void releaseGate() {
        System.out.println();
        System.out.println("RELEASE A GATE ALLOCATION");
        System.out.println(SINGLE_LINE);
 
        String flightId = readText("Flight ID: ").toUpperCase();
        int[] position = gateSchedule.findFlight(flightId);
        if (position == null) {
            System.out.println("ERROR: Flight " + flightId + " does not have a gate allocation.");
            return;
        }
        
        if(gateSchedule.releaseGate(FlightId)){
            clearBoardingGate(flightManager.searchFlight(flightId));
            System.out.println("Gate " + GateSchedule.GATES[position[0]] + " at " + GateSchedule.TIME_SLOTS[position[1]] + " has been released from flight "
                    + flightId + "."););
        }
    }
    private void rescheduleFlight() {
        System.out.println();
        System.out.println("RESCHEDULE A FLIGHT");
        System.out.println(SINGLE_LINE);
        
        Flight flight = findFlightFromUser();
        if (flight == null) {
            return;
        }
        String flightId = flight.getFlightId();
        
        int[] current = gateSchedule.findFlight(flightId);
        if (current == null){
            System.out.println("ERROR: Flight " + flightId + " does not have a gate allocation yet."
                    + " Use 'Allocate a Gate to a Flight' first.");
            return;
        }
        
       if (gateSchedule.isFull()) {
            System.out.println("ERROR: No other gate and time-slot combinations are available.");
            return;
       }
       
       gateSchedule.displaySchedule();
        System.out.println("Flight " + flightId + " is currently at gate "
                + GateSchedule.GATES[current[0]] + " at " + GateSchedule.TIME_SLOTS[current[1]] + ".");
        int gate = readGate("New gate");
        int slot = readTimeSlot("New departure time slot");
 
        // current[0] is the gate row and current[1] is the time-slot column.
        if (gate == current[0] && slot == current[1]) {
            System.out.println("The flight is already at that gate and time slot. Nothing was changed.");
            return;
        }
        if (!gateSchedule.isAvailable(gate, slot)) {
            System.out.println("ERROR: Gate " + GateSchedule.GATES[gate] + " is already occupied at "
                    + GateSchedule.TIME_SLOTS[slot] + " by flight " + gateSchedule.getFlightAt(gate, slot)
                    + ". The flight was not moved.");
            return;
        }
 
        if (gateSchedule.rescheduleFlight(flightId, gate, slot)) {
            applyAllocation(flight, gate, slot);
            System.out.println("Flight " + flightId + " rescheduled to gate " + GateSchedule.GATES[gate]
                    + " at " + GateSchedule.TIME_SLOTS[slot] + ".");
        } else {
            System.out.println("ERROR: The flight could not be rescheduled.");
        }
    }
    /**
     * Keeps the flight record in step with its gate allocation: the departure
     * time becomes the time slot, and an international flight records the gate
     * as its boarding gate.
     */
    private void applyAllocation(Flight flight, int gate, int slot) {
        flight.setDepartureTime(GateSchedule.TIME_SLOTS[slot]);
        // instanceof checks the real type before casting to the subclass (Oracle, n.d.e).
        if (flight instanceof InternationalFlight) {
            ((InternationalFlight) flight).setBoardingGate(GateSchedule.GATES[gate]);
        }
    }
 
    private void clearBoardingGate(Flight flight) {
        if (flight instanceof InternationalFlight) {
            ((InternationalFlight) flight).setBoardingGate(InternationalFlight.NO_GATE);
        }
    }
 
    // ==================================================================
    // Sample data
    // ==================================================================
 
    /**
     * Loads a set of flights and gate allocations so the system can be
     * demonstrated without typing every record in by hand.
     */
    private void loadSampleData() {
        // A Flight array can also hold InternationalFlight objects (polymorphism) (Oracle, n.d.e).
        Flight[] samples = {
            new Flight("FL101", "FlySafair", "Cape Town", "06:00", 180, 162, FlightCategory.DOMESTIC),
            new Flight("FL115", "Airlink", "Durban", "08:00", 120, 96, FlightCategory.DOMESTIC,
                    FlightStatus.BOARDING),
            new InternationalFlight("FL205", "Emirates", "Dubai", "10:00", 350, 329,
                    FlightStatus.SCHEDULED, "Terminal A", null, true),
            new InternationalFlight("FL230", "British Airways", "London", "12:00", 300, 247,
                    FlightStatus.DELAYED, "Terminal A", null, true),
            new Flight("FL310", "DHL Aviation", "Nairobi", "14:00", 0, 0, FlightCategory.CARGO),
            new Flight("FL325", "South African Airways", "Gqeberha", "16:00", 150, 147,
                    FlightCategory.DOMESTIC, FlightStatus.DELAYED),
            new InternationalFlight("FL420", "Qatar Airways", "Doha", "18:00", 280, 210,
                    FlightStatus.SCHEDULED, "Terminal B", null, true),
            new Flight("FL440", "Lift", "George", "18:00", 160, 88, FlightCategory.DOMESTIC)
        };
        // Row and column in the gate schedule for each sample flight above.
        int[][] positions = {{0, 0}, {1, 1}, {0, 2}, {1, 3}, {0, 4}, {1, 5}, {0, 6}, {2, 6}};
 
        int added = 0;
        for (int i = 0; i < samples.length; i++) {
            if (flightManager.registerFlight(samples[i])) {
                added++;
                if (gateSchedule.allocateGate(samples[i].getFlightId(), positions[i][0], positions[i][1])) {
                    applyAllocation(samples[i], positions[i][0], positions[i][1]);
                }
            }
        }
 
        System.out.println();
        if (added == 0) {
            System.out.println("The sample flights are already loaded. Nothing was added.");
        } else {
            System.out.println(added + " sample flight(s) loaded.");
        }
    }
}