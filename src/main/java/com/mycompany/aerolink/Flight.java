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
    
}