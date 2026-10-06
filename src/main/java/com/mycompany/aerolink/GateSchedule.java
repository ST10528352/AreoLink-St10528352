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
    
    
}