package com.techelevator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCCampgroundDAO implements CampgroundDAO{
	
	private JdbcTemplate jdbc;
	
	public JDBCCampgroundDAO (DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public void displayCampgrounds(Integer parkSelection) {
		List<Campground> campgroundsToPrint = this.selectCampgroundByPark(parkSelection);
		
		System.out.println("\tName\t\tOpen\t\tClose\t\t Daily Fee");
		
		for (int i = 0; i < campgroundsToPrint.size(); i++) {
			System.out.println((i + 1) + ") \t" + campgroundsToPrint.get(i).getName() + " \t" + monthGetter(campgroundsToPrint.get(i).getOpenFromMm()) + " \t" + monthGetter(campgroundsToPrint.get(i).getOpenToMM()) + " \t$" + campgroundsToPrint.get(i).getDailyFee());
		}
	}
	
	public List<Campground> selectCampgroundByPark(int parkSelection) {
		
		List<Campground> campgroundByPark = new ArrayList<>();
		
		String sqlToFindCampgrounds = "SELECT * FROM campground WHERE park_id = ?";
		SqlRowSet resultOfSqlQuery = jdbc.queryForRowSet(sqlToFindCampgrounds, parkSelection);
		
		while (resultOfSqlQuery.next()) {
			Campground campground = new Campground();
			campground.setCampgroundId(resultOfSqlQuery.getInt("campground_id"));
			campground.setParkId(resultOfSqlQuery.getInt("park_id"));
			campground.setName(resultOfSqlQuery.getString("name"));
			campground.setOpenFromMm(resultOfSqlQuery.getString("open_from_mm"));
			campground.setOpenToMM(resultOfSqlQuery.getString("open_to_mm"));
			campground.setDailyFee(new BigDecimal(resultOfSqlQuery.getDouble("daily_fee")));
			
			campgroundByPark.add(campground);
		}
		
		return campgroundByPark;
	}
	
	public Campground selectCampgroundById(int campgroundId) {
		Campground myCampground = new Campground();
		String sqlString = "SELECT * FROM campground WHERE campground_id = ?;";
		SqlRowSet result = jdbc.queryForRowSet(sqlString, campgroundId);
	
		while (result.next()) {
			myCampground.setCampgroundId(result.getInt("campground_id"));
			myCampground.setParkId(result.getInt("park_id"));
			myCampground.setName(result.getString("name"));
			myCampground.setOpenFromMm(result.getString("open_from_mm"));
			myCampground.setOpenToMM(result.getString("open_to_mm"));
			myCampground.setDailyFee(new BigDecimal(result.getDouble("daily_fee")));
			
		}
		return myCampground;
	}
	
	
	private String monthGetter(String monthNumber) {
		String month;
		
		switch (monthNumber) {
			case "01":
				month = "January";
				break;
			case "02":
				month = "February";
				break;
			case "03":
				month = "March";
				break;
			case "04":
				month = "April";
				break;
			case "05":
				month = "May";
				break;
			case "06":
				month = "June";
				break;
			case "07":
				month = "July";
				break;
			case "O8":
				month = "August";
				break;
			case "09":
				month = "September";
				break;
			case "10":
				month = "October";
				break;
			case "11":
				month = "November";
				break;
			default:
				month = "December";
				break;
		}
		
		return month;
	}

}
