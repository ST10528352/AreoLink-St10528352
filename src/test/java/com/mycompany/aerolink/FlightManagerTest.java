package com.mycompany.aerolink;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
 
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
 
/**
 * Tests the flight records: register, search, update, delete, duplicate
 * Flight IDs, passenger validation and sorting.
 * Written with JUnit 5: @BeforeEach gives every test fresh data and the
 * assert methods compare the expected result with the actual result (Bechtold et al., 2024).
 */
public class FlightManagerTest {
 
    private FlightManager manager;
 
    @BeforeEach
    public void setUp() {
        // Three flights are registered out of order so the sort tests have work to do.
        manager = new FlightManager();
        manager.registerFlight(new Flight("FL300", "FlySafair", "Cape Town", "14:00", 180, 150,
                FlightCategory.DOMESTIC));
        manager.registerFlight(new Flight("FL100", "Airlink", "Durban", "18:00", 120, 60,
                FlightCategory.DOMESTIC));
        manager.registerFlight(new InternationalFlight("FL200", "Emirates", "Dubai", "06:00", 350, 350,
                FlightStatus.SCHEDULED, "Terminal A", "G01", true));
    }
    
      // ---------------- Register and search ----------------
    
    @Test
    public void testRegisterFlight_addsFlight(){
        Flight flight = new Flight ("FL400", "Lift", "George", "10:00", 160, 80, FlightCategory.DOMESTIC);
        
        assertTrue(manager.registerFlight(flight));
        assertEquals(4, manager.getFlightCount());
    }
    
    @Test
    public void testSearchFlight_ReturnsRegisteredFlight(){
        Flight found = manager.searchFlight("FL300");
        
        assertNotNull(found);
        assertEquals("FlySafair", found.getAirlineName());
        assertEquals("Cape Town", found.getDestination());
        assertEquals(FlightStatus.SCHEDULED, found.getStatus());
    }
    
    @Test
    public void testSearchFlight_IsNotCaseSensitive(){
        assertNotNull(manager.searchFlight("fl300"));
    }
    
    @Test
    public void testSearchFlight_UnknownIdReturnsNull() {
        assertNull(manager.searchFlight("FL999"));
        assertNull(manager.searchFlight(null));
    }
 
    @Test
    public void testRegisterFlight_CargoWithZeroCapacityAllowed() {
        Flight cargo = new Flight("FL500", "DHL Aviation", "Nairobi", "20:00", 0, 0, FlightCategory.CARGO);
 
        assertTrue(manager.registerFlight(cargo));
        assertEquals(0.0, cargo.getPassengerLoadPercentage(), 0.001);
    }
    
     // ---------------- Duplicate Flight IDs ----------------
    
    @Test
    public void testRegisterFlight_DuplicateIdRejected(){
        Flight duplicate = new Flight("FL300", "Lift", "George", "08:00", 100, 10, FlightCategory.DOMESTIC);
 
        // The duplicate is refused and the number of flights stays the same.
        assertFalse(manager.registerFlight(duplicate));
        assertEquals(3, manager.getFlightCount());
        // The original record must be unchanged.
        assertEquals("FlySafair", manager.searchFlight("FL300").getAirlineName());
    }
    
    @Test
    public void testRegisterFlight_DuplicateIdInDifferentCaseRejected() {
        Flight duplicate = new Flight("fl300", "Lift", "George", "08:00", 100, 10, FlightCategory.DOMESTIC);
 
        assertFalse(manager.registerFlight(duplicate));
    }
 
    @Test
    public void testRegisterFlight_NullRejected() {
        assertFalse(manager.registerFlight(null));
    }
    
    // ---------------- Update ----------------
    
    @Test
    public void testUpdateFlight_ChangesDetails() {
        boolean updated = manager.updateFlight("FL100", "CemAir", "Hoedspruit", "20:00", 130, 100,
                FlightStatus.DELAYED);
 
        Flight flight = manager.searchFlight("FL100");
        assertTrue(updated);
        assertEquals("CemAir", flight.getAirlineName());
        assertEquals("Hoedspruit", flight.getDestination());
        assertEquals("20:00", flight.getDepartureTime());
        assertEquals(130, flight.getPassengerCapacity());
        assertEquals(100, flight.getBookedPassengers());
        assertEquals(FlightStatus.DELAYED, flight.getStatus());
    }
 
    @Test
    public void testUpdateFlight_UnknownIdFails() {
        assertFalse(manager.updateFlight("FL999", "CemAir", "Hoedspruit", "20:00", 130, 100,
                FlightStatus.DELAYED));
    }
 
    @Test
    public void testUpdateFlight_BookedAboveCapacityRejectedAndNothingChanges() {
        // Boundary test: 101 booked on a capacity of 100 is one over the limit.
        boolean updated = manager.updateFlight("FL100", "CemAir", "Hoedspruit", "20:00", 100, 101,
                FlightStatus.DELAYED);
 
        Flight flight = manager.searchFlight("FL100");
        assertFalse(updated);
        assertEquals("Airlink", flight.getAirlineName());
        assertEquals(120, flight.getPassengerCapacity());
        assertEquals(60, flight.getBookedPassengers());
    }
 
    @Test
    public void testUpdateFlight_InvalidDepartureTimeRejected() {
        assertFalse(manager.updateFlight("FL100", "Airlink", "Durban", "07:30", 120, 60,
                FlightStatus.SCHEDULED));
    }
 
    // ---------------- Delete ----------------
 
    @Test
    public void testDeleteFlight_RemovesFlight() {
        // After the delete, a search for the same ID must find nothing.
        assertTrue(manager.deleteFlight("FL100"));
        assertNull(manager.searchFlight("FL100"));
        assertEquals(2, manager.getFlightCount());
    }
 
    @Test
    public void testDeleteFlight_UnknownIdFails() {
        assertFalse(manager.deleteFlight("FL999"));
        assertEquals(3, manager.getFlightCount());
    }
 
    // ---------------- Passenger validation and boundaries ----------------
 
    @Test
    public void testFlight_BookedEqualToCapacityAllowed() {
        Flight full = manager.searchFlight("FL200");
 
        assertEquals(350, full.getBookedPassengers());
        assertEquals(100.0, full.getPassengerLoadPercentage(), 0.001);
    }
 
    @Test
    public void testFlight_BookedAboveCapacityThrows() {
        // Boundary test: one passenger more than the capacity must be rejected (Bechtold et al., 2024).
        assertThrows(IllegalArgumentException.class, () ->
                new Flight("FL600", "Lift", "George", "10:00", 100, 101, FlightCategory.DOMESTIC));
    }
 
    @Test
    public void testSetBookedPassengers_AboveCapacityThrowsAndKeepsOldValue() {
        Flight flight = manager.searchFlight("FL100");
 
        assertThrows(IllegalArgumentException.class, () -> flight.setBookedPassengers(121));
        assertEquals(60, flight.getBookedPassengers());
    }
 
    @Test
    public void testFlight_NegativeValuesThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                new Flight("FL600", "Lift", "George", "10:00", -1, 0, FlightCategory.DOMESTIC));
        assertThrows(IllegalArgumentException.class, () ->
                new Flight("FL600", "Lift", "George", "10:00", 100, -1, FlightCategory.DOMESTIC));
    }
 
    @Test
    public void testFlight_EmptyIdThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new Flight("  ", "Lift", "George", "10:00", 100, 50, FlightCategory.DOMESTIC));
    }
 
    @Test
    public void testPassengerLoadPercentage_Calculation() {
        Flight flight = new Flight("FL204", "Lift", "George", "10:00", 180, 176, FlightCategory.DOMESTIC);
 
        // 176 / 180 x 100 = 97.78; the third value is the rounding allowed (Bechtold et al., 2024).
        assertEquals(97.78, flight.getPassengerLoadPercentage(), 0.01);
    }
 
    // ---------------- Inheritance ----------------
 
    @Test
    public void testInternationalFlight_StoresInheritedAndExtraDetails() {
        Flight flight = manager.searchFlight("FL200");
 
        assertTrue(flight instanceof InternationalFlight);
        InternationalFlight international = (InternationalFlight) flight;
        assertEquals(FlightCategory.INTERNATIONAL, international.getCategory());
        assertEquals("Emirates", international.getAirlineName());
        assertEquals("Terminal A", international.getDepartureTerminal());
        assertEquals("G01", international.getBoardingGate());
        assertTrue(international.isCustomsClearanceRequired());
    }
 
    // ---------------- Sorting ----------------
 
    @Test
    public void testSortByFlightId_AscendingOrder() {
        // Registered as FL300, FL100, FL200; expected back as FL100, FL200, FL300.
        List<Flight> sorted = manager.getFlightsSortedById();
 
        assertEquals("FL100", sorted.get(0).getFlightId());
        assertEquals("FL200", sorted.get(1).getFlightId());
        assertEquals("FL300", sorted.get(2).getFlightId());
    }
 
    @Test
    public void testSortByDepartureTime_EarliestFirst() {
        List<Flight> sorted = manager.getFlightsSortedByDepartureTime();
 
        // Registered as 14:00, 18:00, 06:00; expected back earliest first.
        assertEquals("06:00", sorted.get(0).getDepartureTime());
        assertEquals("14:00", sorted.get(1).getDepartureTime());
        assertEquals("18:00", sorted.get(2).getDepartureTime());
    }
 
    @Test
    public void testSortByDepartureTime_SameTimeOrderedByFlightId() {
        manager.registerFlight(new Flight("FL050", "Lift", "George", "14:00", 100, 50,
                FlightCategory.DOMESTIC));
 
        List<Flight> sorted = manager.getFlightsSortedByDepartureTime();
 
        assertEquals("FL050", sorted.get(1).getFlightId());
        assertEquals("FL300", sorted.get(2).getFlightId());
    }
 
    @Test
    public void testSort_DoesNotChangeRegistrationOrder() {
        manager.getFlightsSortedById();
 
        assertEquals("FL300", manager.getAllFlights().get(0).getFlightId());
    }
 
    @Test
    public void testSort_EmptyListReturnsEmptyList() {
        assertTrue(new FlightManager().getFlightsSortedById().isEmpty());
    }
}