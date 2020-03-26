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

public class JDBCReservationDAOTest {
	private static SingleConnectionDataSource dataSource;
	JDBCReservationDAO jdbcReservation;
	JdbcTemplate jdbc;
	
	@Test
	public void test_write_reservation() {
		Reservation reservationToSave = new Reservation();
		LocalDate dateToTest1 = LocalDate.of(2020, 04, 13);  
		LocalDate dateToTest2 = LocalDate.of(2020, 04, 14); 
		reservationToSave.setCreateDate(LocalDate.now());
		reservationToSave.setFromDate(dateToTest1);
		reservationToSave.setToDate(dateToTest2);
		String reservationName = "Derek";
		int siteNumber = 620;
		
		JDBCSiteDAO siteChecker = new JDBCSiteDAO(dataSource);
		int numRes = siteChecker.getAllReservationsBySite(siteChecker.getSiteById(siteNumber)).size();
		jdbcReservation.createReservation(reservationToSave, reservationName, siteNumber);
		int newNumRes = siteChecker.getAllReservationsBySite(siteChecker.getSiteById(siteNumber)).size();
		Assert.assertEquals(numRes + 1, newNumRes);
	}
	
	@Test
	public void always_passes() {
		Assert.assertEquals(true, true);
	}
	
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
		jdbcReservation = new JDBCReservationDAO(dataSource);
		jdbc = new JdbcTemplate(dataSource);
	}

	/* After each test, we rollback any changes that were made to the database so that
	 * everything is clean for the next test */
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
}
