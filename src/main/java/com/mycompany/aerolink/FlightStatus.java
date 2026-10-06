package com.mycompany.aerolink;
 
/**
 * The possible states of a flight.
 * An enum limits the status to these fixed values only (Oracle, n.d.d).
 */
public enum FlightStatus {
    SCHEDULED,  // default status of a new flight
    BOARDING,   // passengers are getting on
    DELAYED,    // listed in the delayed flights report
    DEPARTED,   // has left, so it can no longer be given a gate
    CANCELLED   // a cancelled flight releases its gate allocation
}
 