package com.mycompany.aerolink;
 
/**
 * Represents a single flight. Domestic and cargo flights use this class
 * directly, international flights use the InternationalFlight subclass.
 *
 * All attributes are private (information hiding) and can only be reached
 * through the getters and setters, which also guard the business rules (Farrell, 2023).
 */
public class Flight {
 
    // Private attributes: only this class can read or change them directly (Farrell, 2023).
    private String flightId;
    private String airlineName;
    private String destination;
    private String departureTime;
    private int passengerCapacity;
    private int bookedPassengers;
    private FlightCategory category;
    private FlightStatus status;
    
    /**
     * Creates a flight with the default status of SCHEDULED.
     */
    
public Flight(String flightId, String airlineName, String destination, String departureTime,
                  int passengerCapacity, int bookedPassengers, FlightCategory category) {
        // Constructor chaining: reuse the full constructor instead of repeating it (Farrell, 2023).
        this(flightId, airlineName, destination, departureTime, passengerCapacity,
                bookedPassengers, category, FlightStatus.SCHEDULED);
    }
 
    /**
     * Creates a flight with every attribute supplied.
     *
     * @throws IllegalArgumentException if any value breaks a business rule
     */
    public Flight(String flightId, String airlineName, String destination, String departureTime,
                  int passengerCapacity, int bookedPassengers, FlightCategory category,
                  FlightStatus status) {
        // The setters are used here so every value is validated when the object is built.
        setFlightId(flightId);
        setAirlineName(airlineName);
        setDestination(destination);
        setDepartureTime(departureTime);
        setPassengerNumbers(passengerCapacity, bookedPassengers);
        setCategory(category);
        setStatus(status);
    }
    
    // ------------------------------------------------------------------
    // Getters: read-only access to the private attributes (Farrell, 2023).
    // ------------------------------------------------------------------
 
    public String getFlightId() {
        return flightId;
    }
 
    public String getAirlineName() {
        return airlineName;
    }
 
    public String getDestination() {
        return destination;
    }
 
    public String getDepartureTime() {
        return departureTime;
    }
 
    public int getPassengerCapacity() {
        return passengerCapacity;
    }
 
    public int getBookedPassengers() {
        return bookedPassengers;
    }
 
    public FlightCategory getCategory() {
        return category;
    }
 
    public FlightStatus getStatus() {
        return status;
    }
    
    
    // ------------------------------------------------------------------
    // Setters (each one validates before it stores anything)
    // ------------------------------------------------------------------
 
    public void setFlightId(String flightId) {
        if (flightId == null || flightId.trim().isEmpty()) {
            throw new IllegalArgumentException("Flight ID may not be empty.");
        }
        // Stored in upper case so FL101 and fl101 are treated as the same flight.
        this.flightId = flightId.trim().toUpperCase();
    }
 
    public void setAirlineName(String airlineName) {
        if (airlineName == null || airlineName.trim().isEmpty()) {
            throw new IllegalArgumentException("Airline name may not be empty.");
        }
        this.airlineName = airlineName.trim();
    }
 
    public void setDestination(String destination) {
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination may not be empty.");
        }
        this.destination = destination.trim();
    }
 
    public void setDepartureTime(String departureTime) {
        // Only the airport's eight departure time slots are accepted.
        if (!GateSchedule.isValidTimeSlot(departureTime)) {
            throw new IllegalArgumentException(
                    "Departure time must be one of the airport's eight departure time slots.");
        }
        this.departureTime = departureTime.trim();
    }
 
    public void setPassengerCapacity(int passengerCapacity) {
        setPassengerNumbers(passengerCapacity, this.bookedPassengers);
    }
 
    public void setBookedPassengers(int bookedPassengers) {
        setPassengerNumbers(this.passengerCapacity, bookedPassengers);
    }
  /**
     * Sets the capacity and the booked passengers together so that the rule
     * "booked passengers may not exceed capacity" is checked on the pair.
     */
    public void setPassengerNumber(int passengerCapacity, int bookedPassengers) {
        if(passengerCapacity < 0){
            throw new IllegalArgumentException("Passenger capacity may not be negative.");
        }
        if (bookedPassenger < 0) {
            throw new IllegalException("booked passenger may not be  negative.");
        }
        //Business rule: Booked passengers may never exceed the capacity.
        if(bookedPassengers > passengerCapacity){
            throw new IllegalExecption("Booked passengers (" + bookedPassengers + ") may not exceed the passenger capacity (" + passengerCapacity + ").");
    }
        this.passengerCapacity = passengerCapacity;
        this.bookedPassengers = bookedPassengers;
    }
    
    public void setCategory(FlightCategory catergory){
        if (category == null){
            throw new IllegalArgumentException("Flight category is required.");
        }
        this.category = category;  
    }
    
    public void setStatus(FlightStatus status){
        if (status == null){
            throw new IllegalArgumentException("Flight status is required.");
        }
           this.status = status;
    }
    
}
    