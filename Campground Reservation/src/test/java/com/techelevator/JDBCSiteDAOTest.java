package com.techelevator;

import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import org.junit.Assert;


public class JDBCSiteDAOTest {
	/* Using this particular implementation of DataSource so that
	 * every database interaction is part of the same database
	 * session and hence the same database transaction */
	private static SingleConnectionDataSource dataSource;
	JDBCSiteDAO jdbcSite;
	JdbcTemplate jdbc;

	@Test
	public void test_single_date_availability() {
		LocalDate dateToTest1 = LocalDate.of(2020, 02, 13); //site one available site two available
		LocalDate dateToTest2 = LocalDate.of(2020, 02, 14); //unavailable two available
		LocalDate dateToTest3 = LocalDate.of(2020, 02, 16); //unavailable two available
		LocalDate dateToTest4 = LocalDate.of(2020, 02, 17); //available two available
		LocalDate dateToTest5 = LocalDate.of(2020, 02, 18); //unavailable two unavailable
		LocalDate dateToTest6 = LocalDate.of(2020, 02, 21); //unavailable unavailable
		LocalDate dateToTest7 = LocalDate.of(2020, 02, 22); //available available
		LocalDate dateToTest8 = LocalDate.of(2020, 02, 23); //available available
		
		Site site1 = jdbcSite.getSiteById(1);
		Site site2 = jdbcSite.getSiteById(2);
		
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDate(site1, dateToTest1));
		Assert.assertEquals(false, jdbcSite.getAvailabilityByDate(site1, dateToTest2));
		Assert.assertEquals(false, jdbcSite.getAvailabilityByDate(site1, dateToTest3));
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDate(site1, dateToTest4));
		Assert.assertEquals(false, jdbcSite.getAvailabilityByDate(site1, dateToTest5));
		Assert.assertEquals(false, jdbcSite.getAvailabilityByDate(site1, dateToTest6));
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDate(site1, dateToTest7));
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDate(site1, dateToTest8));
		
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDate(site2, dateToTest1));
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDate(site2, dateToTest2));
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDate(site2, dateToTest3));
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDate(site2, dateToTest4));
		Assert.assertEquals(false, jdbcSite.getAvailabilityByDate(site2, dateToTest5));
		Assert.assertEquals(false, jdbcSite.getAvailabilityByDate(site2, dateToTest6));
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDate(site2, dateToTest7));
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDate(site2, dateToTest8));
	}
	
	@Test
	public void test_date_range_availability() {
		LocalDate dateToTest1 = LocalDate.of(2020, 02, 13); //site one available site two available
		LocalDate dateToTest2 = LocalDate.of(2020, 02, 14); //unavailable two available
		LocalDate dateToTest3 = LocalDate.of(2020, 02, 16); //unavailable two available
		LocalDate dateToTest4 = LocalDate.of(2020, 02, 17); //available two available
		LocalDate dateToTest5 = LocalDate.of(2020, 02, 18); //unavailable two unavailable
		LocalDate dateToTest6 = LocalDate.of(2020, 02, 21); //unavailable unavailable
		LocalDate dateToTest7 = LocalDate.of(2020, 02, 22); //available available
		LocalDate dateToTest8 = LocalDate.of(2020, 02, 23); //available available
		
		Site site1 = jdbcSite.getSiteById(1);
		Site site2 = jdbcSite.getSiteById(2);
		
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDateRange(site1, dateToTest1, dateToTest2));//true
		Assert.assertEquals(false, jdbcSite.getAvailabilityByDateRange(site1, dateToTest1, dateToTest3));//false
		Assert.assertEquals(false, jdbcSite.getAvailabilityByDateRange(site1, dateToTest3, dateToTest4));//false
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDateRange(site1, dateToTest4, dateToTest5));//true
		Assert.assertEquals(false, jdbcSite.getAvailabilityByDateRange(site1, dateToTest4, dateToTest6));//false
		Assert.assertEquals(false, jdbcSite.getAvailabilityByDateRange(site1, dateToTest5, dateToTest7));//false
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDateRange(site1, dateToTest7, dateToTest8));//true
		
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDateRange(site2, dateToTest1, dateToTest2));//true
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDateRange(site2, dateToTest1, dateToTest3));//true
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDateRange(site2, dateToTest3, dateToTest4));//true
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDateRange(site2, dateToTest4, dateToTest5));//true
		Assert.assertEquals(false, jdbcSite.getAvailabilityByDateRange(site2, dateToTest4, dateToTest6));//false
		Assert.assertEquals(false, jdbcSite.getAvailabilityByDateRange(site2, dateToTest5, dateToTest7));//false
		Assert.assertEquals(true, jdbcSite.getAvailabilityByDateRange(site2, dateToTest7, dateToTest8));//true
	}
	
	@Test
	public void test_getAllSites_length() {
		int correctNumberOfSites = 622;
		int numSites = jdbcSite.getAllSites().size();
		Assert.assertEquals(correctNumberOfSites, numSites);
		}
	
	@Test
	public void test_getAllReservationsBySite() {
		Site mySite1 = jdbcSite.getSiteById(1);//2
		Site mySite2 = jdbcSite.getSiteById(2);//1
		Site mySite3 = jdbcSite.getSiteById(3);//1
		Site mySite4 = jdbcSite.getSiteById(4);//1
		
		Assert.assertEquals(2,  jdbcSite.getAllReservationsBySite(mySite1).size());
		Assert.assertEquals(1,  jdbcSite.getAllReservationsBySite(mySite2).size());
		Assert.assertEquals(1,  jdbcSite.getAllReservationsBySite(mySite3).size());
		Assert.assertEquals(1,  jdbcSite.getAllReservationsBySite(mySite4).size());
	}
	
	@Test
	public void test_getSiteById() {
		Integer validSiteId = 290;
		Integer expectedCampgroundId = 2;
		Integer expectedSiteNumber = 14;
		Integer expectedMaxOcc = 6;
		boolean expectedAccess = false;
		Integer expectedRV = 0;
		boolean expectedUtil = false;
		
		Site siteToTest = jdbcSite.getSiteById(validSiteId);
		
		Assert.assertEquals(expectedCampgroundId,  siteToTest.getCampgroundId());
		Assert.assertEquals(expectedSiteNumber,  siteToTest.getSiteNumber());
		Assert.assertEquals(expectedMaxOcc,  siteToTest.getMax_occupancy());
		Assert.assertEquals(expectedAccess,  siteToTest.getAccessible());
		Assert.assertEquals(expectedRV,  siteToTest.getMaxRvLength());
		Assert.assertEquals(expectedUtil,  siteToTest.getUtilities());
	}
	
//	public void test_getSiteByNumber() {
//		Integer validSiteId = 290;
//		Integer expectedCampgroundId = 2;
//		Integer expectedSiteNumber = 14;
//		Integer expectedMaxOcc = 6;
//		boolean expectedAccess = false;
//		Integer expectedRV = 0;
//		boolean expectedUtil = false;
//		
//		Site siteToTest = jdbcSite.getSiteByNumber(validSiteId);
//		
//		Assert.assertEquals(expectedCampgroundId,  siteToTest.getCampgroundId());
//		Assert.assertEquals(expectedSiteNumber,  siteToTest.getSiteNumber());
//		Assert.assertEquals(expectedMaxOcc,  siteToTest.getMax_occupancy());
//		Assert.assertEquals(expectedAccess,  siteToTest.getAccessible());
//		Assert.assertEquals(expectedRV,  siteToTest.getMaxRvLength());
//		Assert.assertEquals(expectedUtil,  siteToTest.getUtilities());
//	}
	
	/* Before any tests are run, this method initializes the datasource for testing. */
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		/* The following line disables autocommit for connections
		 * returned by this DataSource. This allows us to rollback
		 * any changes after each test */
		dataSource.setAutoCommit(false);
	}

	/* After all tests have finished running, this method will close the DataSource */
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
	
	@Before
	public void make_new_jdbc_to_test() {
		jdbcSite = new JDBCSiteDAO(dataSource);
		jdbc = new JdbcTemplate(dataSource);
	}

	/* After each test, we rollback any changes that were made to the database so that
	 * everything is clean for the next test */
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
}
