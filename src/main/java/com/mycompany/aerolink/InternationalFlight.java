package com.mycompany.aerolink;

/**
 * An international flight is a Flight that also records the departure
 * terminal, the boarding gate and whether customs clearance is required.
 * It inherits everything else from Flight using the extends keyword (Oracle, n.d.e).
 */
public class InternationalFlight extends Flight {
 
    /** Shown as the boarding gate until a gate has been allocated. */
    public static final String NO_GATE = "Not allocated";
 
    // Extra attributes that only an international flight has.
    private String departureTerminal;
    private String boardingGate;
    private boolean customsClearanceRequired;
 
    /**
     * Creates an international flight. The inherited attributes are passed up
     * to the Flight constructor using super(), and the category is always
     * INTERNATIONAL.
     */
    
public internationalFlight(String flightId, String airlineName, String destination, 
        String departureTime, int passengerCapacity, int bookedPassengers, FlightStatus status, 
        String departureTerminal, String boardingGate, boolean customClearenceRequired){
    
    //Super() must be the first statement: it runs the flight constructor (Oracle n.d.e)
    super(flightId, airlineName, destination, departureTime, passengerCapacity,
                bookedPassengers, FlightCategory.INTERNATIONAL, status);
    
    setDepartureTerminal(departureTerminal);
    setBoardingGate(boardingGate);
    this.customsClearanceRequired = customsClearenceRequired;
    
    }

    public String getDepartureTerminal(){
        return departureTerminal;
    }
    
    public void setDepartureTerminal(String departureTerminal) {
        if (departureTerminal == null || departureTerminal.trim().isEmpty()){
            throw new IllegalArgumentException("Departure terminal may not be empty.");
        }
        this.departureTerminal = departureTerminal.trim();
    }
    
    public String getBoardingGate(){
        return boardingGate;
    }
    
    public void setBoardingGate(String boardingGate){
        if (boardingGate == null || boardingGate.trim().isEmpty()){
            //No gate yet, so store a clear placeholder instead
            this.boardingGate = NO_GATE;
        } else {
            this.boardingGate = boardingGate.trim();
        }
    }
    
    public boolean isCustomClearanceRequired(){
        return customsClearanceRequired;
    }
    
    public void setCustomsClearanceRequired(boolean customsClearanceRequired){
        this.customsClearanceRequired = customsClearanceRequired;
    }
    
    /**
     * Displays the inherited flight details first and then the extra
     * international flight information.
     */
    
    @Override
    public void displayDetails(){
        //Reuse the superclass version, then add the international details (Oracle, n.d.e)
        super.displayDetails();
        System.out.println("Departure Terminal : " + departureTerminal);
        System.out.println("Boarding Gate      : " + boardingGate);
        //The conditional operator chooses the text to print from the boolean (Farrell, 2023).
        System.out.println("Custom Clearence   : " + (customsClearanceRequired ? "Required" : "Not required"));
    }
}