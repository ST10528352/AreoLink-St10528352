package com.mycompany.aerolink;
 
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
 
/**
 * Tests the 5 x 8 gate schedule: allocate, release, reschedule, occupied
 * gate/time-slot validation and the busiest gate and time slot.
 * Written with JUnit 5: @BeforeEach gives every test fresh data and the
 * assert methods compare the expected result with the actual result (Bechtold et al., 2024).
 */
public class GateScheduleTest {
 
    private GateSchedule schedule;
 
    @BeforeEach
    public void setUp() {
        schedule = new GateSchedule();
    }
    
    // ---------------- Allocate and release ----------------
    
    @Test
    public void testNewSchedule_HasFortyAvailableSlots(){
        assertEquals(5, GateSchedule.GATES.length);
        assertEquals(8, GateSchedule.TIME_SLOTS.length);
        // 5 gates x 8 time slots = 40 free combinations.
        assertEquals(40, schedule.countAvailable());
        assertEquals(0, schedule.countOccupied());
    }
 
    @Test
    public void testAllocateGate_Success() {
        assertTrue(schedule.allocateGate("FL101", 0, 0));
 
        assertEquals("FL101", schedule.getFlightAt(0, 0));
        assertFalse(schedule.isAvailable(0, 0));
        assertTrue(schedule.isFlightScheduled("FL101"));
        assertEquals("G01", schedule.getGateOfFlight("FL101"));
        assertEquals(39, schedule.countAvailable());
    }
 
    @Test
    public void testReleaseGate_FreesTheSlot() {
        schedule.allocateGate("FL101", 2, 3);
 
        assertTrue(schedule.releaseGate("FL101"));
        assertTrue(schedule.isAvailable(2, 3));
        assertFalse(schedule.isFlightScheduled("FL101"));
        assertEquals(40, schedule.countAvailable());
    }
 
    @Test
    public void testReleaseGate_FlightNotScheduledFails() {
        assertFalse(schedule.releaseGate("FL999"));
    }
 
    @Test
    public void testReleasedSlot_CanBeAllocatedAgain() {
        schedule.allocateGate("FL101", 1, 1);
        schedule.releaseGate("FL101");
 
        assertTrue(schedule.allocateGate("FL202", 1, 1));
        assertEquals("FL202", schedule.getFlightAt(1, 1));
    }
 
    // ---------------- Occupied gate and time slot ----------------
 
    @Test
    public void testAllocateGate_OccupiedSlotRejected() {
        schedule.allocateGate("FL101", 0, 0);
 
        // Same gate and same time slot as FL101, so this must be refused.
        assertFalse(schedule.allocateGate("FL202", 0, 0));
        // The first flight keeps the gate and the second is not scheduled.
        assertEquals("FL101", schedule.getFlightAt(0, 0));
        assertFalse(schedule.isFlightScheduled("FL202"));
    }
 
    @Test
    public void testAllocateGate_SameGateDifferentTimeAllowed() {
        schedule.allocateGate("FL101", 0, 0);
 
        assertTrue(schedule.allocateGate("FL202", 0, 1));
    }
 
    @Test
    public void testAllocateGate_SameTimeDifferentGateAllowed() {
        schedule.allocateGate("FL101", 0, 0);
 
        assertTrue(schedule.allocateGate("FL202", 1, 0));
    }
 
    @Test
    public void testAllocateGate_SameFlightTwiceRejected() {
        schedule.allocateGate("FL101", 0, 0);
 
        // FL101 already has a gate, so a second allocation must be refused.
        assertFalse(schedule.allocateGate("FL101", 3, 5));
        assertTrue(schedule.isAvailable(3, 5));
        assertEquals(1, schedule.countOccupied());
    }
 
    // ---------------- Boundaries ----------------
 
    @Test
    public void testAllocateGate_FirstAndLastCellsAreValid() {
        assertTrue(schedule.allocateGate("FL101", 0, 0));
        assertTrue(schedule.allocateGate("FL202", 4, 7));
    }
 
    @Test
    public void testAllocateGate_OutOfRangeRejected() {
        // Boundary test: valid gates are 0-4 and valid slots are 0-7; each of these is just outside.
        assertFalse(schedule.allocateGate("FL101", -1, 0));
        assertFalse(schedule.allocateGate("FL101", 5, 0));
        assertFalse(schedule.allocateGate("FL101", 0, -1));
        assertFalse(schedule.allocateGate("FL101", 0, 8));
        assertEquals(0, schedule.countOccupied());
    }
 
    @Test
    public void testAllocateGate_EmptyFlightIdRejected() {
        assertFalse(schedule.allocateGate("", 0, 0));
        assertFalse(schedule.allocateGate(null, 0, 0));
    }
 
    @Test
    public void testFullSchedule_NoFurtherAllocationPossible() {
        // Fill all 40 cells with different flights using nested loops.
        int number = 100;
        for (int gate = 0; gate < GateSchedule.GATES.length; gate++) {
            for (int slot = 0; slot < GateSchedule.TIME_SLOTS.length; slot++) {
                assertTrue(schedule.allocateGate("FL" + number, gate, slot));
                number++;
            }
        }
 
        assertTrue(schedule.isFull());
        assertEquals(0, schedule.countAvailable());
        assertFalse(schedule.allocateGate("FL999", 2, 2));
    }
 
    // ---------------- Reschedule ----------------
 
    @Test
    public void testRescheduleFlight_MovesToNewGateAndTime() {
        schedule.allocateGate("FL101", 0, 0);
 
        // The old cell must be freed and the new cell must hold the flight.
        assertTrue(schedule.rescheduleFlight("FL101", 3, 4));
        assertTrue(schedule.isAvailable(0, 0));
        assertEquals("FL101", schedule.getFlightAt(3, 4));
        assertArrayEquals(new int[]{3, 4}, schedule.findFlight("FL101"));
        assertEquals(1, schedule.countOccupied());
    }
 
    @Test
    public void testRescheduleFlight_ToOccupiedSlotRejectedAndKeepsOriginal() {
        schedule.allocateGate("FL101", 0, 0);
        schedule.allocateGate("FL202", 1, 1);
 
        assertFalse(schedule.rescheduleFlight("FL101", 1, 1));
        assertEquals("FL101", schedule.getFlightAt(0, 0));
        assertEquals("FL202", schedule.getFlightAt(1, 1));
    }
 
    @Test
    public void testRescheduleFlight_NotScheduledFails() {
        assertFalse(schedule.rescheduleFlight("FL101", 1, 1));
        assertNull(schedule.findFlight("FL101"));
    }
 
    // ---------------- Lookups and report counts ----------------
 
    @Test
    public void testTimeSlotAndGateLookups() {
        assertEquals(0, GateSchedule.getTimeSlotIndex("06:00"));
        assertEquals(7, GateSchedule.getTimeSlotIndex("20:00"));
        assertEquals(-1, GateSchedule.getTimeSlotIndex("07:00"));
        assertEquals(4, GateSchedule.getGateIndex("g05"));
        assertEquals(-1, GateSchedule.getGateIndex("G06"));
    }
 
    @Test
    public void testBusiestGateAndTimeSlot() {
        schedule.allocateGate("FL101", 0, 6);
        schedule.allocateGate("FL102", 2, 0);
        schedule.allocateGate("FL103", 2, 3);
        schedule.allocateGate("FL104", 2, 6);
        schedule.allocateGate("FL105", 4, 6);
 
        // Gate index 2 (G03) has three flights and slot index 6 (18:00) has three.
        assertEquals(2, schedule.getBusiestGateIndex());
        assertEquals(3, schedule.countFlightsAtGate(2));
        assertEquals(6, schedule.getBusiestTimeSlotIndex());
        assertEquals(3, schedule.countFlightsInTimeSlot(6));
    }
 
    @Test
    public void testBusiestGate_EmptyScheduleReturnsMinusOne() {
        assertEquals(-1, schedule.getBusiestGateIndex());
        assertEquals(-1, schedule.getBusiestTimeSlotIndex());
    }
}
 