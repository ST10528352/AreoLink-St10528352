package com.mycompany.aerolink;
 
/**
 * The departure schedule of the terminal: five gates by eight time slots,
 * held in a 5 x 8 two-dimensional array. Each row is a gate and each column
 * is a departure time slot. A cell holds the Flight ID using that gate at
 * that time, or null when the gate is free.
 */
public class GateSchedule {
 
    // Row labels (gates) and column labels (time slots) of the schedule (Oracle, n.d.a).
    public static final String[] GATES = {"G01", "G02", "G03", "G04", "G05"};
    public static final String[] TIME_SLOTS =
            {"06:00", "08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00"};
 
    // Printed in the grid when a gate is free.
    private static final String EMPTY_CELL = "---";
 
    // 5 x 8 two-dimensional array: schedule[gate][timeSlot] (Oracle, n.d.a).
    private final String[][] schedule = new String[GATES.length][TIME_SLOTS.length];
 
    // ------------------------------------------------------------------
    // Lookups
    // ------------------------------------------------------------------
    
    /**
     * @return the column index of a time slot such as "10:00", or -1
     */
    public static int getTimeSlotIndex(String time){
       if (time == null) {
           return -1;
       } 
       for (int slot = 0; slot < TIME_SLOTS.length; slot ++){
           //Linear search through time slots (Farrell, 2023)
           if(TIME_SLOTS[slot].equals(time.trim())){
               return slot;
           }
       }
       return -1;
    }
    
    /**
     * Return the row index
     */
    
    public static int getGateIndex(String gate){
        if (gate == null) {
            return -1;
        }
        for (int row = 0; row < GATES.length; row++){
            if (GATES[row].equalsIgnoreCase(gate.trim())){
                return row;
            }
        }
        return -1;
    }
    
    public static boolean isValidTimeSlot(String time){
        return getTimeSlotIndex(time) !=-1;
    }
    
    private boolean isValidPosition(int gateIndex, int slotIndex){
        //stops the array from being read outside its bounds (Oracle n.d.e)
        return gateIndex >= 0 && gateIndex < GATES.length && slotIndex >= 0 && slotIndex < TIME_SLOTS.length;
        
    }
     /**
     * return true if the gate is free during the time slot
     */
    public boolean isAvailable(int gateIndex, int slotIndex) {
        // A null cell means no flight has that gate at that time.
        return isValidPosition(gateIndex, slotIndex) && schedule[gateIndex][slotIndex] == null;
    }
    
    /**
     * @return the Flight ID at the gate and time slot, or null if it is free
     */
    public String getFlightAt(int gateIndex, int slotIndex){
        if(!isValidPosition(gateIndex, slotIndex)){
            return null;
        }
        return schedule[gateIndex][slotIndex];
    }
    /**
     * finds where a flight is 
     * returns gate index and slot index or null
     */
    public int[] findFlight(String flightId){
        if (flightId == null){
            return null;
        }
        for (int gate = 0; gate < GATES.length; gate++){
            for(int slot = 0; slot < TIME_SLOTS.length; slot++){
                //check every cel of the 2d array for flight ID
                if(flightId.trim().equalsIgnoreCase(schedule[gate][slot])){
                    return new int[]{gate,slot};
                }
            }
        }
        return null;
    }
    
    public boolean isFlightScheduled(String flightId){
        return findFlight(flightId)!=null;
    }
    
    /**
     * return the gate name of flight
     */
    public String getGateOfFlight(String flightId){
        int[]position = findFlight(flightId);
        return position == null ? null : GATES[position[0]];
    }
    
    // ------------------------------------------------------------------
    // Allocate, release and reschedule
    // ------------------------------------------------------------------
 
    /**
     * Allocates a gate and time slot to a flight.
     *
     * @return true if allocated; false if the Flight ID is empty, the gate or
     *         time slot does not exist, the gate is already occupied at that
     *         time, or the flight already has a gate allocation
     */
    
    public boolean allocateGate(String flightId, int gateIndex, int slotIndex){
        if(flightId == null || flightId.trim().isEmpty()){
            return false;
        }
        // Rule 1: one flight per gate per time slot.
        if (!isAvailable(gateIndex, slotIndex)) {
            return false;
        }
        // Rule 2: a flight may only be scheduled once.
        if (isFlightScheduled(flightId)) {
            return false;
        }
        // Both rules passed, so store the Flight ID in the cell.
        schedule[gateIndex][slotIndex] = flightId.trim().toUpperCase();
        return true;
    }
    
     /**
     * Releases the gate allocated to a flight.
     *
     * @return true if a gate was released, false if the flight had no gate
     */
    
    public boolean releaseGate(String flightId){
        int[]position = findFlight(flightId);
        if (position == null ){
            return false;
        }
        //setting the call back to null
        schedule[position[0]][position[1]] =null;
        return true;
      
    }
    
     /**
     * Moves a scheduled flight to another available gate and/or time slot.
     * If the move is not possible the original allocation is kept.
     *
     * return true if the flight was moved
     */
    
    public boolean rescheduleFlight(String flightId, int newGateIndex, int newSlotIndex) {
        int[] current = findFlight(flightId);
        if (current == null || !isAvailable(newGateIndex, newSlotIndex)) {
            return false;
        }
        // Clear the old cell before filling the new one, so the flight is never in two places.
        schedule[current[0]][current[1]] = null;
        schedule[newGateIndex][newSlotIndex] = flightId.trim().toUpperCase();
        return true;
    
    }
    
    // ------------------------------------------------------------------
    // Counting
    // ------------------------------------------------------------------
    
    public int countFlightsAtGate(int gateIndex){
        int count =0;
        for (int slot = 0; slot < TIME_SLOTS.length; slot++){
            //walk along row one and count the cells 
            if (schedule[gateIndex][slot] != null){
                count++;
            }
        }
        return count;
    }
    
    public int countFlightsInTimeSlot(int slotIndex){
        int count = 0;
         for (int gate = 0; gate < GATES.length; gate++) {
            // Walk down one column and count the occupied cells.
            if (schedule[gate][slotIndex] != null) {
                count++;
            }
        }
        return count;
    }
    
    public int countOccupied(){
        int count = 0;
        for (int gate = 0; gate <GATES.length; gate++){
            count += countFlightsAtGate(gate);
        }
        return count;
    }
    
    public int countAvailable(){
        //total cells minus occupied ones
        return GATES.length * TIME_SLOTS.length -countOccupied();
    }
    
    /**
     * return true if everygate is occupied
     */
    
    public boolean isFull(){
        return countAvailable() == 0;
    }
    
    /**
     * return the index of the gates with the most flights
     */
    
    public int getBusiestGateIndex(){
        int busiest =-1;
        int highest = 0;
        for (int gate = 0; gate < GATES.length; gate++){
            int count = countFlightsAtGate(gate);
            //keep the gate with the highest count (Farrell, 2023)
            if (count>highest){
                highest = count;
                busiest = gate;
            }
        }
        return busiest;
    }
    
     /**
     * @return the index of the time slot with the most departures, or -1 if
     *         the schedule is empty (the earliest slot wins a tie)
     */
    public int getBusiestTimeSlotIndex() {
        int busiest = -1;
        int highest = 0;
        for (int slot = 0; slot < TIME_SLOTS.length; slot++) {
            // Using > and not >= means the earliest time slot wins a tie.
            int count = countFlightsInTimeSlot(slot);
            if (count > highest) {
                highest = count;
                busiest = slot;
            }
        }
        return busiest;
    }
    
    // ------------------------------------------------------------------
    // Display
    // ------------------------------------------------------------------
 
    /**
     * Displays the complete gate schedule as a grid using nested loops.
     */
    
    public void displaySchedule(){
        String line = "-".repeat(10 + TIME_SLOTS.length * 8);
        
        System.out.println();
        System.out.println("AIRPORT GATE DEPARTURE SCHEDULE");
        System.out.println(line);
        //left alligns text in fixed width do colums line up (Farrell, 2023)
        
        System.out.printf("%-10s", "Gate");
        for (int slot = 0; slot < TIME_SLOTS.length; slot++) {
            System.out.printf("%-8s", TIME_SLOTS[slot]);
        }
        System.out.println();
        System.out.println(line);
 
        // Nested loops: the outer loop walks the rows (gates) and the inner
        // loop walks the columns (time slots) of the 2D array (Oracle, n.d.a).
        for (int gate = 0; gate < GATES.length; gate++) {
            System.out.printf("%-10s", "Gate " + GATES[gate]);
            for (int slot = 0; slot < TIME_SLOTS.length; slot++) {
                // Show --- for a free gate, otherwise the Flight ID.
                String cell = schedule[gate][slot] == null ? EMPTY_CELL : schedule[gate][slot];
                System.out.printf("%-8s", cell);
            }
            System.out.println();
        }
        System.out.println(line);
    }
    
    /**
     * Displays every gate and time slot combination that is still free.
     */
    public void displayAvailableSlots(){
        System.out.println();
        System.out.println("AVAILABLE GATE AND TIME SLOT COMBINATIONS");
        System.out.println("-----------------------------------------");
        for(int gate = 0; gate < GATES.length; gate++){
            System.out.printf("Gate %s : ", GATES[gate]);
            int free = 0;
            for (int slot = 0; slot < TIME_SLOTS.length; slot++){
                //Only time slots that are still free
                if (schedule[gate][slot]==null){
                    System.out.print(TIME_SLOTS[slot]+ " ");
                    free++;
                }
            }
            if (free == 0){
                System.out.print("No Availble time slots");
            }
            System.out.println();
        }
        System.out.println("Total available: " + countAvailable() + " of " + (GATES.length * 
                TIME_SLOTS.length));
    }
    
    /**
     * Displays every gate and time slot combination that is in use.
     */
    
    public void displayOccupiedSlots(){
        System.out.println();
        System.out.println("OCCUPIED GATE AND TIME-SLOT COMBINATIONS");
        System.out.println("----------------------------------------");
        if (countOccupied() == 0) {
            System.out.println("No gates are currently allocated.");
            return;
        }
        System.out.printf("%-8s%-8s%s%n", "Gate", "Time", "Flight");
        for (int gate = 0; gate < GATES.length; gate++) {
            for (int slot = 0; slot < TIME_SLOTS.length; slot++) {
                // Only the cells that hold a flight are printed.
                if (schedule[gate][slot] != null) {
                    System.out.printf("%-8s%-8s%s%n", GATES[gate], TIME_SLOTS[slot], schedule[gate][slot]);
                }
            }
        }
        System.out.println("Total occupied: " + countOccupied() + " of "
                + (GATES.length * TIME_SLOTS.length));
    }
}