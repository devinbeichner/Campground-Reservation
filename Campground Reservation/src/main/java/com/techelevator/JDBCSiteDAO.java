package com.techelevator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCSiteDAO implements SiteDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	@Override
	public List<Site> getAllSites() {
		List<Site> allSites = new ArrayList<Site>();
		String sqlSiteQuery = "SELECT * " +
								"FROM site";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlSiteQuery);
		while(result.next()) {
			Site tempSite = new Site();
			tempSite = mapRowToSite(result);
			allSites.add(tempSite);
		}
		return allSites;
	}
	
	@Override
	public Site getSiteById(Integer siteId) {
		Site siteToReturn = new Site();
		String sqlSiteString = "SELECT * " +
								"FROM site " +
								"WHERE site_id = ?;";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlSiteString, siteId);
		
		if(result.next()) {
			siteToReturn = mapRowToSite(result);
		}						
		return siteToReturn;
	}

	@Override
	public boolean getAvailabilityByDate(Site siteToCheck, LocalDate dateToCheck) {
		List<Reservation> siteReservationList = getAllReservationsBySite(siteToCheck);
		for (Reservation siteReservation : siteReservationList) {
			LocalDate startDate = siteReservation.getFromDate();
			LocalDate endDate = siteReservation.getToDate();
			if(dateToCheck.compareTo(startDate) >= 0 && dateToCheck.compareTo(endDate) < 0) {
				return false;
			} 
		}
		return true;
	}

	@Override
	public boolean getAvailabilityByDateRange(Site siteToCheck, LocalDate startDate, LocalDate endDate) {
		LocalDate currentDate = startDate;
		while(currentDate.compareTo(endDate) < 0) {
			boolean dateIsAvailable = getAvailabilityByDate(siteToCheck, currentDate);
			if (dateIsAvailable == false) {
				return false;
			}
			currentDate = currentDate.plusDays(1);
		}
		return true;
	}

	@Override
	public List<Reservation> getAllReservationsBySite(Site siteToCheck) {
		List<Reservation> allReservations = new ArrayList<Reservation>();
		
		String sqlReservationSearch = "SELECT * " +
										"FROM site " +
										"JOIN reservation ON reservation.site_id = site.site_id " +
										"WHERE site.site_id = ?;";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlReservationSearch, siteToCheck.getSiteId());
		
		while(results.next()) {
			Reservation newReservation = new Reservation();
			newReservation.setReservationId(results.getInt("reservation_id"));
			newReservation.setSiteId(results.getInt("site_id"));
			newReservation.setName(results.getString("name"));
			LocalDate fromDate = results.getDate("from_date").toLocalDate();
			LocalDate toDate = results.getDate("to_date").toLocalDate();
			LocalDate createDate = results.getDate("create_date").toLocalDate();
			newReservation.setFromDate(fromDate);
			newReservation.setToDate(toDate);
			newReservation.setCreateDate(createDate);
			allReservations.add(newReservation);
		}
		siteToCheck.setReservation_list(allReservations);
		return allReservations;
	}

	@Override
	public List<Site> getSitesByCampground(Campground campgroundToCheck) {
		List<Site> campgroundSites = new ArrayList<Site>();
		String sqlcampsite = "SELECT * " + 
							"FROM site " +
							"WHERE site.campground_id = ?;";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlcampsite, campgroundToCheck.getCampgroundId());
		
		while(results.next()){
			Site campsite = new Site();
			campsite = mapRowToSite(results);
			campgroundSites.add(campsite);
		}
		
		return campgroundSites;
	}
	
	public List<Site> getAvailableSitesByCampgroundReservation(Campground selectedCampground, Reservation myReservation){
		LocalDate startDate = myReservation.getFromDate();
		LocalDate endDate = myReservation.getToDate();
		LocalDate campgroundOpen = LocalDate.of(startDate.getYear(), Integer.parseInt(selectedCampground.getOpenFromMm()), 01);
		LocalDate campgroundClosed = LocalDate.of(startDate.getYear(), (Integer.parseInt(selectedCampground.getOpenToMM()) % 12) + 1, 01);
		List<Site> allSites = getSitesByCampground(selectedCampground);
		List<Site> availableSites = new ArrayList<Site>();
		for(Site thisSite : allSites) {
			boolean siteIsAvailable = getAvailabilityByDateRange(thisSite, startDate, endDate);
			if(siteIsAvailable && startDate.isAfter(campgroundOpen) && endDate.isBefore(campgroundClosed)) {
				availableSites.add(thisSite);
			}
		}
		List<Site> fiveSites = new ArrayList<Site>();
		for(int i = 0; i < availableSites.size() && i < 5; i++ ) {
			fiveSites.add(availableSites.get(i));
		}
 		return fiveSites;
		
	}
	
	public List<Site> getAvailableSitesByCampground(Campground selectedCampground, LocalDate startDate, LocalDate endDate){
		List<Site> allSites = getSitesByCampground(selectedCampground);
		List<Site> availableSites = new ArrayList<Site>();
		for(Site thisSite : allSites) {
			boolean siteIsAvailable = getAvailabilityByDateRange(thisSite, startDate, endDate);
			if(siteIsAvailable) {
				availableSites.add(thisSite);
			}
		}
		List<Site> fiveSites = new ArrayList<Site>();
		for(int i = 0; i < availableSites.size() && i < 5; i++ ) {
			fiveSites.add(availableSites.get(i));
		}
 		return fiveSites;
	}
	
	public void getFiveSitesPerCampground(List<Campground> campgroundList, Reservation reservationDates) {
		List<Site> siteList = new ArrayList<Site>();
		for(Campground thisCampground: campgroundList) {
			siteList = getAvailableSitesByCampgroundReservation(thisCampground, reservationDates);
			System.out.println(" ");
			System.out.println("For Campground " + thisCampground.getName() + ":");
			printTopFiveSites(siteList, thisCampground, reservationDates);
		}
		
	}
	
	public void printTopFiveSites(List<Site> topFiveSites, Campground campground, Reservation reservationDates) {
		LocalDate startDate = reservationDates.getFromDate();
		LocalDate endDate = reservationDates.getToDate();
		BigDecimal stayLength = new BigDecimal(endDate.compareTo(startDate));
		
		//System.out.println("Your stay will be " + stayLength + " nights.");
		System.out.println("");
		System.out.println("********************** TOP FIVE AVAILABLE SITES **********************");
		//System.out.printf("%1$10d | %2$10d | %3$10s | %4$10d | %5$10s | $%6$d\n", "Site No.", "Max Occupancy", "Accessible?", "Max RV Length", "Utility?", "Cost");
		System.out.printf("%1$-8s | %2$-9s | %3$-10s | %4$-10s | %5$-8s | %6$s\n", "Site No.", "Max Occup.", "Accessible?", "Max RV Length", "Utility?", "Cost");
		System.out.println("----------------------------------------------------------------------");
		for (int i = 0; i < topFiveSites.size(); i++) {
			//System.out.println("Site No.   Max Occup.   Accessible?   Max RV Length  Utility  Cost");
			System.out.print("(" + (i+1) + ")  ");
			System.out.printf("%1$-3d | %2$-10d | %3$-11s | %4$-13d | %5$-8s | $%6$s\n", topFiveSites.get(i).getSiteNumber(), topFiveSites.get(i).getMax_occupancy(), topFiveSites.get(i).getAccessible(), topFiveSites.get(i).getMaxRvLength(), topFiveSites.get(i).getUtilities(), campground.getDailyFee().multiply(stayLength));
			//System.out.println(topFiveSites.get(i).getSiteNumber() + " " + topFiveSites.get(i).getMax_occupancy() + " " +topFiveSites.get(i).getAccessible() + " " + topFiveSites.get(i).getMaxRvLength() + " " + topFiveSites.get(i).getUtilities() + " $" + campground.getDailyFee());
		}
		System.out.println("----------------------------------------------------------------------");
	}

	
	//helper
	private Site mapRowToSite(SqlRowSet result) {
		Site newSite = new Site();
		newSite.setSiteId(result.getInt("site_id"));
		newSite.setCampgroundId(result.getInt("campground_id"));
		newSite.setSiteNumber(result.getInt("site_number"));
		newSite.setMaxOccupancy(result.getInt("max_occupancy"));
		newSite.setAccessible(result.getBoolean("accessible"));
		newSite.setMaxRvLength(result.getInt("max_rv_length"));
		newSite.setUtilities(result.getBoolean("utilities"));
		return newSite;
	}



//	@Override
//	//this will check site JOIN reservation to check if a specific date conflicts with 
//	//any existing reservation.  It does this once for each day in the reservation
//	public boolean getAvailabilityByReservation(Reservation potentialReservation) {
//		//get pertinent information from reservation
//		int siteToCheck = potentialReservation.getSiteId();
//		LocalDate startDate = potentialReservation.getFromDate();
//		LocalDate endDate = potentialReservation.getToDate();
//		//List of conflicting reservation_ids
//		List<Integer> conflictingIds = new ArrayList<Integer>();
//		//getting variables for our for loop to check each day individually
//		int reservationLength = endDate.compareTo(startDate);
//		LocalDate dateToCheck = startDate;
//		int numConflicts = 0;
//		String sqlAvailabilityString = "SELECT reservation.reservation_id, from_date, to_date "
//										+ "FROM reservation "
//										+ "JOIN site ON site.site_id = reservation.site_id "
//										+ "WHERE site.site_id = ? AND "
//										+ "? >= reservation.from_date AND "
//										+ "? < reservation.to_date ;";
//		//check if an individual date conflicts with any reservation
//		for(int i = 0; i < reservationLength; i ++) {
//			SqlRowSet conflictingReservations = jdbcTemplate.queryForRowSet(sqlAvailabilityString, 
//																			siteToCheck, 
//																			dateToCheck, 
//																			dateToCheck);
//			while(conflictingReservations.next()) {
//				conflictingIds.add(conflictingReservations.getInt("reservation_id"));
//				System.out.println("conflict with reservation " + conflictingReservations.getInt("reservation_id") + " on " + dateToCheck.toString());
//			}
//			dateToCheck.plusDays(1);
//		}
//
//		return conflictingIds.size() > 0 ? false : true;
//	}


	
}
