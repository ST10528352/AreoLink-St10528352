package com.mycompany.aerolink;
/**
 * The three categories of flights handled by AeroLink International Airport.
 * An enum limits the category to these fixed values only (Oracle, n.d.d).
 */
public enum FlightCategory {
    DOMESTIC,       // flies inside the country, uses the Flight class
    INTERNATIONAL,  // uses the InternationalFlight subclass
    CARGO           // carries freight, so the passenger capacity may be zero
}
 