package com.mycompany.aerolink;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
 
/**
 * Tests the calculations behind the airport operations report.
 * Written with JUnit 5: @BeforeEach gives every test fresh data and the
 * assert methods compare the expected result with the actual result (Bechtold et al., 2024).
 */
public class ReportGeneratorTest {
 
    private FlightManager manager;
    private GateSchedule schedule;
    private ReportGenerator reports;
 
    @BeforeEach
    public void setUp() {
        manager = new FlightManager();
        schedule = new GateSchedule();
        reports = new ReportGenerator(manager, schedule);
 
        manager.registerFlight(new Flight("FL101", "FlySafair", "Cape Town", "06:00", 200, 100,
                FlightCategory.DOMESTIC));
        manager.registerFlight(new Flight("FL102", "Airlink", "Durban", "08:00", 100, 90,
                FlightCategory.DOMESTIC, FlightStatus.DELAYED));
        manager.registerFlight(new Flight("FL103", "DHL Aviation", "Nairobi", "10:00", 0, 0,
                FlightCategory.CARGO));
        // Only two of the three flights are given a gate.
        schedule.allocateGate("FL101", 0, 0);
        schedule.allocateGate("FL103", 1, 2);
    }
 
    @Test
    public void testScheduledFlights_OnlyFlightsWithAGate() {
        assertEquals(2, reports.getScheduledFlights().size());
    }
 
    @Test
    public void testDelayedFlights_OnlyDelayedStatus() {
        assertEquals(1, reports.getDelayedFlights().size());
        assertEquals("FL102", reports.getDelayedFlights().get(0).getFlightId());
    }
 
    @Test
    public void testHighestPassengerLoadFlight() {
        // FL102 is 90% full, FL101 is 50% full and the cargo flight is ignored.
        assertEquals("FL102", reports.getHighestPassengerLoadFlight().getFlightId());
    }
 
    @Test
    public void testAveragePassengerLoad_IgnoresFlightsWithNoCapacity() {
        // (50% + 90%) / 2 passenger flights; the cargo flight is left out.
        assertEquals(70.0, reports.getAveragePassengerLoad(), 0.001);
    }
 
    @Test
    public void testReports_NoFlights() {
        ReportGenerator empty = new ReportGenerator(new FlightManager(), new GateSchedule());
 
        assertNull(empty.getHighestPassengerLoadFlight());
        assertEquals(0.0, empty.getAveragePassengerLoad(), 0.001);
    }
}