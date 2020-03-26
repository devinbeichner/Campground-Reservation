package com.techelevator;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class CampgroundCLI {
	
	private UserInterface userInput = new UserInterface();
	private JDBCParkDAO jdbcParkDao;
	private JDBCCampgroundDAO jdbcCampgroundDao;
	private JDBCSiteDAO jdbcSiteDao;
	private JDBCReservationDAO jdbcReservationDao;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		this.jdbcParkDao = new JDBCParkDAO(datasource);
		this.jdbcCampgroundDao = new JDBCCampgroundDAO(datasource);
		this.jdbcSiteDao = new JDBCSiteDAO(datasource);
		this.jdbcReservationDao = new JDBCReservationDAO(datasource);
	}

	public void run() {
		
		boolean killswitch1 = true;
		while(killswitch1) {
			
			//Displays all parks and allows users to make selection
			System.out.println("Select park for more information!");
			System.out.println();
			Integer endOfRange = jdbcParkDao.displayAllParks(); //Stores end of range to make sure selection is in range no matter how many parks are added
			System.out.println("Q) Quit");
			System.out.println();
			Integer parkSelection = userInput.selectParkForInquiry(endOfRange); //Get park if they didn't quit
			System.out.println();
			
			boolean killswitch2 = true;
			while (killswitch2) {
				
				jdbcParkDao.printSelectedPark(parkSelection); //Display metadata for selected park
				
				boolean killswitch3 = true;
				while (killswitch3) {
					Integer campgroundOrReserve = userInput.viewCampgroundsOrMakeReservation();
					Integer endOfCampgroundRange = jdbcCampgroundDao.selectCampgroundByPark(parkSelection).size();
					List<Campground> campgroundSelected = jdbcCampgroundDao.selectCampgroundByPark(parkSelection);
				
					if(campgroundOrReserve == 1) {
						jdbcCampgroundDao.displayCampgrounds(parkSelection);
						Integer reserveOrReturn = userInput.reserveOrReturn();
						if (reserveOrReturn == 1) {
							Integer selectedCampground = userInput.selectCampgroundForReservation(endOfCampgroundRange);
							
							if(selectedCampground == null) {
								killswitch3 = false;
							} else {
								reservationMaker(selectedCampground);
								killswitch3 = false;
							}
							
						} else {
							System.out.println();
							killswitch3 = false;
						}
					} else if (campgroundOrReserve == 2){
						
						jdbcCampgroundDao.displayCampgrounds(parkSelection);
						Integer selectedCampground = userInput.selectCampgroundForReservation(endOfCampgroundRange);
						
						if(selectedCampground == null) {
							killswitch3 = false;
						} else {
							reservationMaker(selectedCampground);
							killswitch3 = false;
						}
					} else if (campgroundOrReserve == 3) {
						Park parkToSearchforReservation = jdbcParkDao.getParkById(parkSelection);
						jdbcParkDao.printAllParkReservationsNext30(parkToSearchforReservation);
						killswitch3 = false;
					} else {
						killswitch2 = false;
						killswitch3 = false;
					}
				}
			}
		}
	}
	
	private void reservationMaker(Integer selectedCampground) {
		
		Reservation reservationTemplate = userInput.selectDates(selectedCampground);
		Campground selectedCampgroundObject = jdbcCampgroundDao.selectCampgroundById(selectedCampground);
		List<Site> topFiveSites = jdbcSiteDao.getAvailableSitesByCampgroundReservation(selectedCampgroundObject, reservationTemplate);
		jdbcSiteDao.printTopFiveSites(topFiveSites, selectedCampgroundObject, reservationTemplate);
		
		Integer siteSelection = userInput.selectSiteForReservation(topFiveSites.size());
		String reservationName = userInput.nameForReservation();
		
		jdbcReservationDao.createReservation(reservationTemplate, reservationName, siteSelection);
	}
}
