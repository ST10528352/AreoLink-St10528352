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