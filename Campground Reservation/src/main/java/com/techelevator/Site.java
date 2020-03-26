package com.techelevator;

import java.util.List;

public class Site {
	
	private Integer siteId;		//  primary key
	private Integer campgroundId;	//  foreign key
	private Integer siteNumber; 	//  arbitrary number
	private Integer maxOccupancy; 	
	private Boolean accessible;		//  is it handicap accessible?
	private Integer maxRvLength;	//	0 for nothing can fit
	private Boolean utilities; 		//  has utility access
	
	private List<Reservation> reservationList;

	
	//getters and setters
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getCampgroundId() {
		return campgroundId;
	}

	public void setCampgroundId(Integer campgroundId) {
		this.campgroundId = campgroundId;
	}

	public Integer getSiteNumber() {
		return siteNumber;
	}

	public void setSiteNumber(Integer siteNumber) {
		this.siteNumber = siteNumber;
	}

	public Integer getMax_occupancy() {
		return maxOccupancy;
	}

	public void setMaxOccupancy(Integer maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}

	public Boolean getAccessible() {
		return accessible;
	}

	public void setAccessible(Boolean accessible) {
		this.accessible = accessible;
	}

	public Integer getMaxRvLength() {
		return maxRvLength;
	}

	public void setMaxRvLength(Integer maxRvLength) {
		this.maxRvLength = maxRvLength;
	}

	public Boolean getUtilities() {
		return utilities;
	}

	public void setUtilities(Boolean utilities) {
		this.utilities = utilities;
	}

	public List<Reservation> getReservationList() {
		return reservationList;
	}

	public void setReservation_list(List<Reservation> reservationList) {
		this.reservationList = reservationList;
	}  
}
