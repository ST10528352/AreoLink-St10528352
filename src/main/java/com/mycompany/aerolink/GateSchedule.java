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
           if(TIME_SLOTS[slots].equals(time.trim())){
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
                if(flightId().equalsIgnoreCase(schedule[gate][slot])){
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
}