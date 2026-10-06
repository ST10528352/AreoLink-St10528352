package com.mycompany.aerolink;
 
import java.util.ArrayList;
import java.util.List;
 
/**
 * Produces the airport operational reports from the flight records and the
 * gate schedule.
 */
public class ReportGenerator {
 
    private static final String DOUBLE_LINE = "=============================================";
 
    // The reports read from both the flight records and the gate schedule.
    private final FlightManager flightManager;
    private final GateSchedule gateSchedule;
 
    public ReportGenerator(FlightManager flightManager, GateSchedule gateSchedule) {
        this.flightManager = flightManager;
        this.gateSchedule = gateSchedule;
    }
    
    // ------------------------------------------------------------------
    // Calculations
    // ------------------------------------------------------------------
 
    /**
     * @return the flights that currently have a gate and time slot
     */
    
    public List<Flight> getScheduledFlights(){
        List<Flight> scheduled = new ArrayList<>();
        for(Flight flight : flightManager.getAllFlights()){
            //A flight counts as a schedule once it has a gate and time
            if(gateSchedule.isFlightScheduled(flight.getFlightId())){
                scheduled.add(flight);
            }
        }
        return scheduled;
    }
    
    /**
     * Return the flights with a status of Delayed 
     */
    
  public List<Flight> getDelayedFlights() {
        List<Flight> delayed = new ArrayList<>();
        for (Flight flight : flightManager.getAllFlights()) {
            // Enum values can be compared with == (Oracle, n.d.d).
            if (flight.getStatus() == FlightStatus.DELAYED) {
                delayed.add(flight);
            }
        }
        return delayed;
    }
  
    /**
     * Compares the passenger load percentage of the passenger flights
     * (flights with a capacity above zero).
     *
     * @return the flight with the highest load, or null if there is none
     */
  
  public Flight getHighestPassengerLoadFlight(){
      Flight highest = null;
      for(Flight flight : flightManager.getAllFlights()){
          //Skip flights with no seats, then keep the highest load found so far (farrell 2023)
          if (flight.getPassengerCapacity() > 0
                    && (highest == null
                    || flight.getPassengerLoadPercentage() > highest.getPassengerLoadPercentage())) {
                highest = flight;
            }
        }
        return highest;
    }
  
  /**
   * Return the average passenger load percentage
   */
  
  public double getAveragePassengerLoad(){
      double total = 0;
      int passengerFlights = 0;
      
      for( Flight flight : flightManager.getAllFlights()){
          if(flight.getPassengerCapacity()> 0){
              //add up the load of passengers
              total += flight.getPassengerLoadPercentage();
              passengerFlights++;
          }
      }
      //Average = total / count
      return passengerFlights == 0 ? 0.0 : total / passengerFlights;
  }
  
    // ------------------------------------------------------------------
    // Display
    // ------------------------------------------------------------------
 
    /**
     * Prints a list of flights as a table, including the allocated gate.
     */

  public void displayFlightTable(String title, List<Flight> flights){
      String line = "-".repeat(108);
      System.out.println();
        System.out.println(title);
        System.out.println(line);
        // Show a clear message instead of an empty table.
        if (flights.isEmpty()) {
            System.out.println("No flights to display.");
            System.out.println(line);
            return;
        }
        //Fixed column widths keep the header an the rows lined up (Farrell 2023)
        System.out.printf("%-9s%-22s%-18s%-7s%-10s%-8s%-9s%-15s%-11s%s%n",
                "ID", "Airline", "Destination", "Time", "Capacity", "Booked", "Load %",
                "Category", "Status", "Gate");
        System.out.println(line);
        for (Flight flight : flights) {
            // Look up the gate in the schedule; null means no gate is allocated.
            String gate = gateSchedule.getGateOfFlight(flight.getFlightId());
            System.out.printf("%-9s%-22s%-18s%-7s%-10d%-8d%-9.2f%-15s%-11s%s%n",
                    flight.getFlightId(),
                    shorten(flight.getAirlineName(), 20),
                    shorten(flight.getDestination(), 16),
                    flight.getDepartureTime(),
                    flight.getPassengerCapacity(),
                    flight.getBookedPassengers(),
                    flight.getPassengerLoadPercentage(),
                    flight.getCategory(),
                    flight.getStatus(),
                    gate == null ? "---" : gate);
        }
        System.out.println(line);
        System.out.println("Total: " + flights.size() + " flight(s)");
  }
  private String shorten(String text, int maxLength) {
        // Long names are cut short so they do not push the columns out of line.
        return text.length() <= maxLength ? text : text.substring(0, maxLength - 1) + ".";
    }
 
    public void displayAllFlights() {
        displayFlightTable("ALL REGISTERED FLIGHTS", flightManager.getAllFlights());
    }
 
    public void displayScheduledFlights() {
        displayFlightTable("SCHEDULED FLIGHTS (GATE ALLOCATED)", getScheduledFlights());
    }
 
    public void displayDelayedFlights() {
        displayFlightTable("DELAYED FLIGHTS", getDelayedFlights());
    }
 
    public void displayFlightsSortedById() {
        displayFlightTable("FLIGHTS SORTED BY FLIGHT ID", flightManager.getFlightsSortedById());
    }
 
    public void displayFlightsSortedByDepartureTime() {
        displayFlightTable("FLIGHTS SORTED BY DEPARTURE TIME",
                flightManager.getFlightsSortedByDepartureTime());
    }
 
    /**
     * Prints the airport operations summary report.
     */
    public void displayOperationsReport() {
        System.out.println();
        System.out.println(DOUBLE_LINE);
        System.out.println("       AEROLINK AIRPORT OPERATIONS REPORT");
        System.out.println(DOUBLE_LINE);
        System.out.println();
        System.out.printf("Registered Flights: %d%n", flightManager.getFlightCount());
        System.out.printf("Scheduled Flights:  %d%n", getScheduledFlights().size());
        System.out.printf("Delayed Flights:    %d%n", getDelayedFlights().size());
        System.out.println();
 
        System.out.println("Busiest Gate:");
        // The busiest gate is found by counting the flights in each row; -1 means none.
        int gate = gateSchedule.getBusiestGateIndex();
        if (gate == -1) {
            System.out.println("No flights have been allocated to a gate yet.");
        } else {
            System.out.println(GateSchedule.GATES[gate] + " - "
                    + gateSchedule.countFlightsAtGate(gate) + " Scheduled Flights");
        }
        System.out.println();
 
        System.out.println("Busiest Departure Time:");
        // The busiest time is found by counting the flights in each column; -1 means none.
        int slot = gateSchedule.getBusiestTimeSlotIndex();
        if (slot == -1) {
            System.out.println("No departures have been scheduled yet.");
        } else {
            System.out.println(GateSchedule.TIME_SLOTS[slot] + " - "
                    + gateSchedule.countFlightsInTimeSlot(slot) + " Scheduled Departures");
        }
        System.out.println();
 
        System.out.println("Highest Passenger Load:");
        Flight highest = getHighestPassengerLoadFlight();
        if (highest == null) {
            System.out.println("No passenger flights have been registered yet.");
        } else {
            System.out.println("Flight ID: " + highest.getFlightId());
            System.out.println("Booked Passengers: " + highest.getBookedPassengers());
            System.out.println("Passenger Capacity: " + highest.getPassengerCapacity());
            System.out.printf("Passenger Load: %.2f%%%n", highest.getPassengerLoadPercentage());
        }
        System.out.println();
 
        System.out.printf("Average Passenger Load: %.2f%%%n", getAveragePassengerLoad());
        System.out.println();
        System.out.println(DOUBLE_LINE);
    }
}