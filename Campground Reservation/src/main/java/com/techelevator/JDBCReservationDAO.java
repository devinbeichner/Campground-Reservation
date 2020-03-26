package com.techelevator;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCReservationDAO implements ReservationDAO{
	
	private JdbcTemplate jdbc;
	
	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public void createReservation(Reservation reservationTemplate, String reservationName, Integer siteSelection) {
		Reservation reservationToInput = reservationTemplate;
		reservationToInput.setName(reservationName);
		reservationToInput.setSiteId(siteSelection);
		reservationToInput.setCreateDate(LocalDate.now());
		reservationToInput.setReservationId(getNextReservationId());
		
		String sqlInputReservation = "INSERT INTO reservation (reservation_id, site_id, name, from_date, to_date, create_date)" +
									 "VALUES (?, ?, ?, ?, ?, ?)";
		jdbc.update(sqlInputReservation, reservationToInput.getReservationId(), reservationToInput.getSiteId(), reservationToInput.getName(), reservationToInput.getFromDate(), reservationToInput.getToDate(), reservationToInput.getCreateDate());
		
		System.out.println();
		System.out.println("Your reservation has been confirmed! Your reservation id number is: " + reservationToInput.getReservationId());
		System.out.println();
		
	}
	
	private Integer getNextReservationId() {
		Integer nextReservation = 0;
		
		String getNextNumber = "SELECT nextval('reservation_reservation_id_seq') FROM reservation";
		SqlRowSet result = jdbc.queryForRowSet(getNextNumber);
		result.next();
		nextReservation = result.getInt(1);
		
		return nextReservation;
	}

}
