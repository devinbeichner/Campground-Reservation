package com.techelevator;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class JDBCCampgroundDAOTest {

	/* Using this particular implementation of DataSource so that
	 * every database interaction is part of the same database
	 * session and hence the same database transaction */
	private static SingleConnectionDataSource dataSource;
	JDBCCampgroundDAO jdbcCampground;
	JdbcTemplate jdbc;

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
		jdbcCampground = new JDBCCampgroundDAO(dataSource);
		jdbc = new JdbcTemplate(dataSource);
	}

	/* After each test, we rollback any changes that were made to the database so that
	 * everything is clean for the next test */
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void select_campground_by_park_returns_appropriate_amount() {
		
		List<Campground> results1 = jdbcCampground.selectCampgroundByPark(1);
		Assert.assertEquals(3, results1.size());
		
		String sqlAddNewCampsite = 	"INSERT INTO campground (campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee)" +
									"VALUES (DEFAULT, 1, 'Whatever', '04', '11', 50.00)";
		jdbc.update(sqlAddNewCampsite);
		List<Campground> results2 = jdbcCampground.selectCampgroundByPark(1);
		Assert.assertEquals(4, results2.size());
		
	}

	/* This method provides access to the DataSource for subclasses so that
	 * they can instantiate a DAO for testing */
	protected DataSource getDataSource() {
		return dataSource;
	}
	
}
