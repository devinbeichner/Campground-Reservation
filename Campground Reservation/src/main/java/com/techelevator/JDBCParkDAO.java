package com.techelevator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCParkDAO implements ParkDAO{

	private JdbcTemplate jdbc;
	
	public JDBCParkDAO (DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}
	
	//Method that prints out all the parks and returns the end of range to assist with user selection
	public Integer displayAllParks() {
		
		Integer lastInRange = 0; 
		List<Park> allParks = getListOfParks();
		
		//Prints out the park names
		for (int i = 0; i < allParks.size(); i++) {
			System.out.println((i + 1) + ") " + allParks.get(i).getName());
		}
		
		//Range for user selection.
		lastInRange = allParks.size();
		
		return lastInRange;
		
	}
	
	//Method that uses returned selection to display meta data on that particular park.
	public void printSelectedPark(int parkSelection) {
		
		Park parkSelected = this.getListOfParks().get(parkSelection - 1);
		
		//Print out the park object
		System.out.println(parkSelected.getName());
		System.out.println("Location: " + parkSelected.getLocation());
		System.out.println("Established: " + parkSelected.getEstablishDate());
		System.out.println("Area: " + parkSelected.getArea() + " sq km");
		System.out.println("Annual Visitors: " + parkSelected.getVisitors());
		System.out.println();
		System.out.println(parkSelected.getDescription());

	}
	
	//Get List of park names, can also be used to help keep track of index for user selection
	private List<Park> getListOfParks(){
		
		List<Park> parkNames = new ArrayList<>();
		String getAllParkNames = "SELECT * FROM park ORDER BY name";
		
		SqlRowSet results= jdbc.queryForRowSet(getAllParkNames);
		
		//Sets just the names for the parks since that's all we need to display right now.
		while(results.next()) {
			Park park = new Park();
			park.setName(results.getString("name"));
			park.setParkId(results.getInt("park_id"));			
			park.setArea(results.getInt("area"));
			park.setLocation(results.getString("location"));
			LocalDate estabDate = results.getDate("establish_date").toLocalDate();
			park.setEstablishDate(estabDate);
			park.setDescription(results.getString("description"));
			park.setVisitors(results.getInt("visitors"));
			
			parkNames.add(park);
		}
		
		return parkNames;
	}
	
	public Park getParkById(int parkId) {
		Park myPark = new Park();
		String getPark = "SELECT * FROM park WHERE park_id = ?;";
		
		SqlRowSet results= jdbc.queryForRowSet(getPark, parkId);
		
		if(results.next()) {
			myPark.setName(results.getString("name"));
			myPark.setParkId(results.getInt("park_id"));			
			myPark.setArea(results.getInt("area"));
			myPark.setLocation(results.getString("location"));
			LocalDate estabDate = results.getDate("establish_date").toLocalDate();
			myPark.setEstablishDate(estabDate);
			myPark.setDescription(results.getString("description"));
			myPark.setVisitors(results.getInt("visitors"));
		}
		return myPark;
	}
	
	public void printAllParkReservationsNext30(Park selectedPark) {
		LocalDate searchDate = LocalDate.now().plusDays(30);
		//List<Reservation> allParkReservations = new ArrayList<Reservation>();
		String sqlParkReservationSearch = "SELECT park.name as parkname, campground.name as campgroundname, site.site_number, reservation.reservation_id, reservation.name as reservationname, reservation.from_date, reservation.to_date "
											+"FROM park "
											+"JOIN campground ON campground.park_id = park.park_id "
											+"JOIN site ON site.campground_id = campground.campground_id "
											+"JOIN reservation ON reservation.site_id = site.site_id "
											+"WHERE park.park_id = ? AND reservation.from_date < ? AND reservation.to_date > ? "
											+"ORDER by from_date ;";
		SqlRowSet tableOfParkReservations = jdbc.queryForRowSet(sqlParkReservationSearch, selectedPark.getParkId(), searchDate, LocalDate.now());
		System.out.println("CAMPGROUND | CAMPSITE NUMBER | RESERVATION NUMBER | RESERVATION NAME | FROM | TO ");
		while(tableOfParkReservations.next()) {
			String campground = tableOfParkReservations.getString("campgroundname");
			int siteNumber = tableOfParkReservations.getInt("site_number");
			int resNumber = tableOfParkReservations.getInt("reservation_id");
			String resName = tableOfParkReservations.getString("reservationname");
			LocalDate fromDate = tableOfParkReservations.getDate("from_date").toLocalDate();
			LocalDate toDate = tableOfParkReservations.getDate("to_date").toLocalDate();
			System.out.println(campground + " " + siteNumber + " " + resNumber + " " + resName + " " + fromDate + " " + toDate);
			
		}
		System.out.println();
	}
	
}
