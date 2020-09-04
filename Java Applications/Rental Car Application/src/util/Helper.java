package util;

import java.util.Scanner;
import util.DateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Helper {

	// Prints Main menu on colsole
	public static void printMainMenu() {	
		System.out.println("**** ThriftyRent SYSTEM MENU ****\n");
		System.out.println("Add vehicle: 1");
		System.out.println("Rent vehicle: 2");
		System.out.println("Return vehicle: 3");
		System.out.println("Vehicle Maintenance: 4");
		System.out.println("Complete Maintenance: 5");
		System.out.println("Display All Vehicles: 6");
		System.out.println("Exit Program: 7");
		System.out.println("Enter your choice:");
	}
	
	// Get Integer input from user
	public static int getIntegerInputFromUser() {
		do {
		try {
			Scanner scan = new Scanner(System.in);
			int i = scan.nextInt();
			scan.close();
			return i;
		}
		catch(Exception ex) {
			System.out.println("Invalid Input, please try again.\n\n");
		}
		}while(true);
	}
	
	// To verify validity of integer input and ch 
	@SuppressWarnings("resource")
	public static int getIntegerInputFromUser(int startValue, int endValue) {
		do {
		try {
			Scanner scan = new Scanner(System.in);
			int i = scan.nextInt();
			if(i < startValue || i > endValue) {
				throw new Exception("Please enter a value between" + startValue + " and " + endValue);
			}
			return i;
		}
		catch(Exception ex) {
			System.out.println("Invalid Input, please try again.");
		}
		}while(true);
	}
	
	// To get String input from colsole user
	public static String getStringInputFromUser() {
		do {
		try {
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			String s = scan.nextLine();
			if(s.isEmpty() || s.equals(" ")) {
				throw new Exception("String is empty");
			}
			return s;
		}
		catch(Exception ex) {
			System.out.println("Invalid Input, please try again.\n\n");
		}
		}while(true);
	}
	
	// Converts String date in dd/MM/yyyy format to DateTime object 
	public static DateTime convertToDate(String input) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate date = formatter.parse(input, LocalDate::from);
		DateTime dt = new DateTime(date.getDayOfMonth(),date.getMonthValue(),date.getYear());
		return dt;
	}
	
	// Converts String date in yyyy-MM-dd format to DateTime object : suitable for database queries
	public static DateTime convertToDateDB(String input) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = formatter.parse(input, LocalDate::from);
		DateTime dt = new DateTime(date.getDayOfMonth(),date.getMonthValue(),date.getYear());
		return dt;
	}
	
	// To fetch input from user in valid DateTime format
	public static DateTime checkDate() {
		do {
			try {
				System.out.println("Please enter a valid date:");
				Scanner sc = new Scanner(System.in);
				String input = sc.nextLine();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate date = formatter.parse(input, LocalDate::from);
				DateTime dt = new DateTime(date.getDayOfMonth(),date.getMonthValue(),date.getYear());
				sc.close();
				return dt;				
				}
			catch(Exception ex) {
				System.out.println(ex.getMessage());
			}
			}while(true);
	}
}