package com.techelevator;

public interface ReservationDAO {
	
	public void createReservation(Reservation reservationTemplate, String reservationName, Integer siteSelection);

}
