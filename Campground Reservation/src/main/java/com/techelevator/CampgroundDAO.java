package com.techelevator;

import java.util.List;

public interface CampgroundDAO {

	public List<Campground> selectCampgroundByPark(int parkSelection);
	public void displayCampgrounds(Integer parkSelection);
	
}
