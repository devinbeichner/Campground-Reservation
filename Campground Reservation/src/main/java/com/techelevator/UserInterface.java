package com.techelevator;

import java.time.LocalDate;
import java.util.Scanner;

public class UserInterface {
	
	Scanner in = new Scanner(System.in);
	
	public Integer selectParkForInquiry(Integer endOfRange) {
		Integer userSelection = 0;
		boolean killswitch = true;
		
		
		while(killswitch) {
			String parkSelectionOrQuit = in.nextLine().toLowerCase();
			
			if (parkSelectionOrQuit.equals("q")) {
				System.out.println();
				System.out.println("Thank you for using the National Park Capsite Reservation System!");
				System.exit(0);
			} else {
				try {
					Integer checkIfNumberInRange = Integer.parseInt(parkSelectionOrQuit);
					
					if(checkIfNumberInRange > 0 && checkIfNumberInRange <= endOfRange) {
						userSelection = checkIfNumberInRange;
						killswitch = false;
					} else {
						throw new NumberFormatException();
					}
					
				} catch (NumberFormatException e) {
					System.out.println("Please select park number or \"Q\" to quit.");
				}
			}
		}
		return userSelection;
	}
	
	public Integer viewCampgroundsOrMakeReservation() {
		
		Integer finalInput = 0;
		boolean killswitch = true;
		
		System.out.println();
		System.out.println("What would you like to do?");
		System.out.println();
		System.out.println("1) View Campgrounds");
		System.out.println("2) Search for Reservation");
		System.out.println("3) View reservations for the next 30 days");
		System.out.println("4) Return to previous menu");
		System.out.println();
		
		while(killswitch) {
			String userSelection = in.nextLine();
			
			try {
				Integer userSelectionInt = Integer.parseInt(userSelection);
				
				if (userSelectionInt > 0 && userSelectionInt < 5) {
					finalInput = userSelectionInt;
					killswitch = false;
				} else {
					throw new NumberFormatException();
				}
				
			} catch (NumberFormatException e) {
				System.out.println("Please choose from the provided options");
			}
		}
		return finalInput;
	}
	
	public Integer reserveOrReturn() {
		
		Integer finalInput = 0;
		boolean killswitch = true;
		
		System.out.println();
		System.out.println("Would you like to make a reservation?");
		System.out.println();
		System.out.println("1) Yes");
		System.out.println("2) No (return to Park Information)");
		System.out.println();
		
		while(killswitch) {
			String userSelection = in.nextLine();
			
			try {
				Integer userSelectionInteger = Integer.parseInt(userSelection);
				
				if (userSelectionInteger > 0 && userSelectionInteger < 3) {
					finalInput = userSelectionInteger;
					killswitch = false;
				} else {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.out.println();
				System.out.println("Please choose 1 or 2");
				System.out.println();
			}
		}
		return finalInput;
	}
	
	public Integer selectCampgroundForReservation(Integer endOfCampgroundRange) {
		
		Integer campground = null;
		boolean killswitch = true;
		
		System.out.println();
		System.out.println("Which campground would you like? (0 to cancel)");
		System.out.println();
		
		while(killswitch) {
			String campgroundString = in.nextLine();
			System.out.println();
			
			try {
				Integer campInt = Integer.parseInt(campgroundString);
				
				if(campInt == 0) {
					return campground;
				}
				
				if (campInt > 0 && campInt <= endOfCampgroundRange) {
					campground = campInt;
					killswitch = false;
				} else {
					throw new NumberFormatException();
				}
				
			} catch (NumberFormatException e) {
				System.out.println();
				System.out.println("Please select from the available options.");
				System.out.println();
			}
		}
		return campground;
	}
	
	public Reservation selectDates(Integer campgroundNumber) {
		
		Reservation reservation = new Reservation();
		LocalDate startDate = null;
		LocalDate endDate = null;
		
		while(true) {
			System.out.println();
			System.out.println("What is the arrival date? (mm/dd/yyyy format)");
			System.out.println();
			String arrivalDateString = in.nextLine();
			
			LocalDate tempArrival = checkDate(arrivalDateString);
			
			if(tempArrival == null || tempArrival.compareTo(LocalDate.now()) < 0) {
				System.out.println();
				System.out.println("Please be sure to enter a valid date from today or beyond.");
				System.out.println();
			} else {
				startDate = tempArrival;
				break;
			}
		}
			
		while(true) {
			System.out.println();
			System.out.println("What is the end date? (mm/dd/yyyy format)");
			System.out.println();
			String endDateString = in.nextLine();
			
			LocalDate tempEnd = checkDate(endDateString);
		
			if(tempEnd == null || tempEnd.compareTo(startDate) < 0) {
				System.out.println();
				System.out.println("Please be sure to enter a valid date after or on your arrival date.");
				System.out.println();
			} else {
				endDate = tempEnd;
				break;
			}
		}
		
		reservation.setFromDate(startDate);
		reservation.setToDate(endDate);
		
		return reservation;
	}
	
	public Integer selectSiteForReservation(Integer listSize) {
		Integer siteSelection = 0;
		boolean killswitch = true;
		System.out.println();
		System.out.println("Which site number would you like to reserve?");
		System.out.println();
		
		while(killswitch) {
			String siteString = in.nextLine();
			
			try {
				Integer tempSite = Integer.parseInt(siteString);
				
				if (tempSite > 0 && tempSite <= listSize) {
					siteSelection = tempSite;
					killswitch = false;
				} else {
					throw new NumberFormatException();
				}
				
			} catch (NumberFormatException e) {
				System.out.println();
				System.out.println("Please make your selection from the provided site numbers");
				System.out.println();
			}
		}
		return siteSelection;
	}
	
	public String nameForReservation() {
		String reservationName = null;
		boolean killswitch = true;
		
		while(killswitch) {
			System.out.println();
			System.out.println("What name should the reservation be placed under?");
			System.out.println();
		
			String nameInput = in.nextLine();
			String[] attackChecker = nameInput.split("");
			boolean foundAttack = false;
			
			for(int i = 0; i < attackChecker.length; i++) {
				if (attackChecker[i].matches("[,.\\?!@#\\$%\\^&\\*\\(\\)<>\\{\\}\\\\0-9]")) {
					System.out.println();
					System.out.println("Please enter your first and last name only");
					System.out.println();
					foundAttack = true;
					break;
				}
			}
			if(!foundAttack) {
				reservationName = nameInput;
				killswitch = false;
			}
		}
		
		return reservationName;
	}
	
	private LocalDate checkDate(String date) {
		
		LocalDate finalDate = null;
		
		String[] splitDate = date.split("/");
		Integer[] intsToDate = new Integer[splitDate.length];
		
		if (splitDate.length != 3) {
			return finalDate;
		}
		
		try {
			for (int i = 0; i < splitDate.length; i++) {
				Integer stringToInt = Integer.parseInt(splitDate[i]);
				intsToDate[i] = stringToInt;
			}
		} catch (NumberFormatException e) {
			return finalDate;
		}
		
		if(intsToDate[0] < 1 || intsToDate[0] > 12) {
			return finalDate;
		}
		
		if(intsToDate[0] == 1 || intsToDate[0] == 3 || intsToDate[0] == 5 || intsToDate[0] == 7 || intsToDate[0] == 8 || intsToDate[0] == 10 || intsToDate[0] == 12) {
			if (intsToDate[1] < 1 || intsToDate[1] > 31) {
				return finalDate;
			}
		}
		
		if(intsToDate[0] == 4 ||intsToDate[0] == 6 ||intsToDate[0] == 9 ||intsToDate[0] == 11) {
			if(intsToDate[1] < 1 || intsToDate[1] > 30) {
				return finalDate;
			}
		}
		
		if(intsToDate[0] == 2) {
			if(intsToDate[2] % 4 == 0) {
				if (intsToDate[1] < 1 || intsToDate[1] > 29) {
					return finalDate;
				}
			} else {
				if(intsToDate[1] < 1 || intsToDate[1] > 28) {
					return finalDate;
				}
			}
		}
		
		finalDate = LocalDate.of(intsToDate[2], intsToDate[0], intsToDate[1]);
		
		return finalDate;
		
	}

}
