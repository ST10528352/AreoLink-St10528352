package com.mycompany.aerolink;
 
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
 
/**
 * Stores the registered flights in memory and provides the register, search,
 * update, delete and sort operations.
 */
public class FlightManager {
 
    // An ArrayList grows and shrinks as flights are registered and deleted (Oracle, n.d.b).
    private final ArrayList<Flight> flights = new ArrayList<>();
 
    /**
     * Registers a new flight.
     *
     * @return true if the flight was added, false if it is null or a flight
     *         with the same Flight ID already exists
     */
    public boolean registerFlight(Flight flight) {
        // Reject a missing flight or a duplicate Flight ID.
        if (flight == null || flightExists(flight.getFlightId())) {
            return false;
        }
        flights.add(flight);
        return true;
    }
    
    /**
     * Searches for a flight by its Flight ID 
     * returns the flight, or null if no flight ID
     */
    
     public Flight searchFlight(String flightId){
         if(flightId == null) {
             return null;
         }
         //Linear search: check each flight until the ID mateches (Farrell, 2023).
         for (Flight flight : flights){
             //equalsIgnoreCase lets the user type the ID in upper or lowwer case.
             if (flight.getFlightId().equalsIgnoreCase(flightId.trim())){
                 return flight;
             }
         }
         return null;
     }
     
     public boolean flightExists(String flightId) {
         return searchFlight(flightId) !=null;
     }
     
     /**
     * Updates the details of an existing flight. The Flight ID and category
     * cannot be changed. Nothing is changed unless every new value is valid.
     *
     * @return true if the flight was updated, false if the flight was not
     *         found or one of the new values is invalid
     */
     
     public boolean updateFlight(String flightId, String airlineName, String destination,
                                String departureTime, int passengerCapacity, int bookedPassengers,
                                FlightStatus status){
         Flight flight = searchFlight(flightId);
         if (flight == null){
             return false;
         }
         //Vallidates everything first so a failed update leaves the flight untouched.
         boolean valid = airlineName != null && !airlineName.trim().isEmpty()
         && destination != null && !destination.trim().isEmpty()
                && GateSchedule.isValidTimeSlot(departureTime)
                && passengerCapacity >= 0
                && bookedPassengers >= 0
                && bookedPassengers <= passengerCapacity
                && status != null;
         if(!valid){
             return false;
         }
         
         //Every value is valid, so apply the changes through the setters.
        flight.setAirlineName(airlineName);
        flight.setDestination(destination);
        flight.setDepartureTime(departureTime);
        flight.setPassengerNumbers(passengerCapacity, bookedPassengers);
        flight.setStatus(status);
        return true;
     }
     
     /**
      * Delets a flight.
      * returns true if a flight was removed and false if not
      */
     
     public boolean deleteFlight(String flightId){
         Flight flight = searchFlight(flightId);
         if (flight == null){
             return false;
         }
         //remove () returns true when the fligh was found
         return flights.remove(flight);
     }
     
     /**
      * Return a copy of the list of flights in the order they were made
      */
     public List<Flight> getAllFlights(){
         //A copy is returned so other classes cannot change the real list.
         return new ArrayList<>(flights);
     }
     
     public int getFlightCount(){
         return flights.size();
     }
     
     /**
      * return a new list of the flights sorted by flight ID
      */
     
     public List<Flight> getFlightsSortedById(){
         //sort a copy so the orginal is kept in order
           List<Flight> sorted = getAllFlights();
        // The Comparator tells sort() which attribute to order the flights by (Oracle, n.d.f).
        sorted.sort(Comparator.comparing(Flight::getFlightId));
        return sorted;
     }
     
     /**
     * @return a new list of the flights sorted by departure time (earliest first). 
     */
     
     public List<Flight> getFlightsSortedByDepartureTime(){
         List<Flight> sorted = getAllFlights();
         //how many times sorted correctly 
         sorted.sort(Comparator.comparing(Flight::getDepartureTime).thenComparing(Flight::getFlightId));
         return sorted;  
     }
}