package com.techelevator;

import java.time.LocalDate;
import java.util.List;

public interface SiteDAO {
	
	public List<Site> getAllSites();
	
	public List<Site> getSitesByCampground(Campground campgroundToCheck);
	
	public Site getSiteById(Integer siteId);
	
	//public Site getSiteByNumber(Integer siteNumber);
	
	public boolean getAvailabilityByDate(Site siteToCheck, LocalDate dateToCheck);
	
	public boolean getAvailabilityByDateRange(Site siteToCheck, LocalDate startDate, LocalDate endDate);
	
	public List<Reservation> getAllReservationsBySite(Site siteToCheck);
	
	//public void displayAvailableSitesByCampground(Campground selectedCampground, LocalDate startDate, LocalDate endDate);
	
	
}
