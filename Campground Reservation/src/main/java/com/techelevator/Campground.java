package com.techelevator;

import java.math.BigDecimal;

public class Campground {

	private Integer campgroundId;
	private Integer parkId;
	private String name;
	private String openFromMm;
	private String openToMM;
	private BigDecimal dailyFee;
	
	public Integer getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(Integer campgroundId) {
		this.campgroundId = campgroundId;
	}
	public Integer getParkId() {
		return parkId;
	}
	public void setParkId(Integer parkId) {
		this.parkId = parkId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpenFromMm() {
		return openFromMm;
	}
	public void setOpenFromMm(String openFromMm) {
		this.openFromMm = openFromMm;
	}
	public String getOpenToMM() {
		return openToMM;
	}
	public void setOpenToMM(String openToMM) {
		this.openToMM = openToMM;
	}
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}
	
	
}
