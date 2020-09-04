//package controller;
//
//
//import java.util.ArrayList;
//import util.DateTime;
//import model.*;
//import util.Helper;
//import view.MainProgram;
//
//public class ThriftyRentSystem {
//		
//		// update beta
//		//ArrayList<Vehicle> vehicles;
//		private static DataController dc = new DataController();
//	
//		public ThriftyRentSystem() {
//			// update beta
//			//vehicles = new ArrayList<Vehicle>(50);
//		}
//		
//		public ArrayList<Vehicle> getVehicles() {
//			return MainProgram.getDATA_LINK().getVehicles();
//		}
//		
//		public void runApplication() {
//			
//			do {				
//				Helper.printMainMenu();
//				int input = Helper.getIntegerInputFromUser(1,7);
//				
//				switch(input) {
//				case 1:
//					this.createVehicle();
//					break;
//				case 2:
//					this.rent();
//					break;
//				case 3:
//					this.returnVehicle();
//					break;
//				case 4:
//					this.requestMaintenance();
//					break;
//				case 5:
//					this.completeMaintenance();
//					break;
//				case 6:
//					this.displayAllVehicles();
//					break;
//				case 7:
//					return;
//				}				
//			} while (true);					
//		}
//		
//
//		public void createVehicle() {
//			try {
//				System.out.println("**** Add Vehicle ****");
//				System.out.println("Add Car: 1");
//				System.out.println("Add Van: 2");
//				
//				int input = Helper.getIntegerInputFromUser(1,2);
//				
//				switch(input) {
//					case 1:
//						this.createCar();
//						break;
//					case 2:
//						this.createVan();
//						break;
//				}
//			}catch(Exception e)
//				{
//					System.out.println(e.getMessage());
//				}
//		}
//		
//		public void createCar() throws Exception {
//			String vehicleId;
//			
//			if(this.getVehicles().size() >= 50) {
//				throw new Exception("Maximum limit exceeded, can not create new vehicle");
//			}	
//			System.out.println("Please provide details of your Car: \n");
//			// to generate vehicleID ask for 4 digit number
//			System.out.println("VehicleId(Enter a number between 1000 & 2000):");
//			do {
//				int input = Helper.getIntegerInputFromUser(1000,2000);
//				vehicleId = "C_" + Integer.toString(input);
//				
//				if(!this.vehicleIdAlreadyExists(vehicleId))
//					break;
//				else
//					System.out.println("Vehicle Id already exists");
//			}while(true);
//			
//			System.out.println("Year:");
//			int year = Helper.getIntegerInputFromUser(1800, Integer.parseInt(DateTime.getCurrentTime().split("-")[0]));
//			
//			System.out.println("Make of car:");
//			String make = Helper.getStringInputFromUser();
//			
//			System.out.println("Model of car:");
//			String model = Helper.getStringInputFromUser();
//			
//			System.out.println("Choose Number of seats:");
//			System.out.println("4 seats: 1");
//			System.out.println("7 seats: 2");
//			int seatInput = Helper.getIntegerInputFromUser(1,2);
//			int numOfSeats;
//			if(seatInput == 1) {
//				numOfSeats = 4;
//			}
//			else {
//				numOfSeats = 7;
//			}
//		
//			System.out.println("Vehicle of type Car created successfully with vehicle id: " + vehicleId);
//		}
//		
//		
//		public void createVan() throws Exception {
//			String vehicleId;
//			
//			if(this.getVehicles().size() >= 50) {
//				throw new Exception("Maximum limit exceeded, can not create new vehicle");
//			}	
//			System.out.println("Please provide details of your Van: \n");
//			// to generate vehicleID ask for 4 digit number
//			System.out.println("VehicleId(Enter a number between 1000 & 2000):");
//			do {
//				int input = Helper.getIntegerInputFromUser(1000,2000);
//				vehicleId = "V_" + Integer.toString(input);
//				
//				if(!this.vehicleIdAlreadyExists(vehicleId))
//					break;
//				else
//					System.out.println("Vehicle Id already exists");
//			}while(true);
//			
//			System.out.println("Year:");
//			int year = Helper.getIntegerInputFromUser(1800, Integer.parseInt(DateTime.getCurrentTime().split("-")[0]));
//			
//			System.out.println("Make of car:");
//			String make = Helper.getStringInputFromUser();
//			
//			System.out.println("Model of car:");
//			String model = Helper.getStringInputFromUser();
//			
//			System.out.println("Last Maintenance Date:");
//			DateTime lastMaintenanceDate = Helper.checkDate();
//			
////			Van van = new Van(vehicleId, year, make, model, lastMaintenanceDate);
////			MainProgram.getDATA_LINK().insertNewVehicle(van);
//			System.out.println("Vehicle of type Van created successfully with vehicle id: " + vehicleId);
//		}
//		
//		
//		public void rent() {
//			try {
//				Vehicle rentVehicle;
//				
//				System.out.println("**** Rent ****");
//				System.out.println("vehicleId (in format C_1234/V_1234): ");
//				String vehicleId = Helper.getStringInputFromUser();
//				
//				if(!this.vehicleIdAlreadyExists(vehicleId)) {
//					throw new Exception("A vehicle " + vehicleId + " does not exist");
//				}
//				
//				rentVehicle = this.findVehicle(vehicleId);
//				
//				if(rentVehicle.getVehicleType().equals("Car")) {
//					rentVehicle = (Car) rentVehicle;
//				}
//				else {
//					rentVehicle = (Van) rentVehicle;
//				}
//				
//				System.out.println("CustomerId (Enter digit between 1000 and 2000):");
//				int input = Helper.getIntegerInputFromUser(1000,2000);
//				String customerId = "CUS" + Integer.toString(input);
//				
//				System.out.println("Rent date(dd/mm/yyyy) : ");
//				DateTime rentDate = Helper.checkDate();
//				
//				System.out.println("Number of days:");
//				int numOfDays = Helper.getIntegerInputFromUser(1,14);
//				
//				if(rentVehicle.rent(customerId, rentDate, numOfDays)) {
//					System.out.println("Vehicle " + vehicleId + " is now successfully rented by " + customerId);
//				}
//						
//			}catch(Exception e)
//			{
//				System.out.println(e.getMessage());
//			}
//		}
//		
//		
//		public void returnVehicle() { 
//			try {
//				Vehicle returnVehicle;
//				
//				System.out.println("**** Return ****");
//				System.out.println("vehicleId: ");
//				String vehicleId = Helper.getStringInputFromUser();
//				
//				if(!this.vehicleIdAlreadyExists(vehicleId)) {
//					throw new Exception("A vehicle " + vehicleId + " does not exist");
//				}
//				
//				returnVehicle = this.findVehicle(vehicleId);
//				
//				if(returnVehicle.getVehicleType().equals("Car")) {
//					returnVehicle = (Car) returnVehicle;
//				}
//				else {
//					returnVehicle = (Van) returnVehicle;
//				}
//				
//				System.out.println("Return Date(dd/mm/yyyy) : ");
//				DateTime returnDate = Helper.checkDate();
//				
//				if( returnVehicle.returnVehicle(returnDate)) {
//					System.out.println("Vehicle " + vehicleId + " is now returned successfully");
//				}
//				
//			}catch(Exception e)
//			{
//				System.out.println(e.getMessage());
//			}
//		}
//		
//		
//		public void requestMaintenance() {
//			try {
//				Vehicle maintVehicle;
//				
//				System.out.println("**** Perform Maintenance ****");
//				System.out.println("vehicleId: ");
//				String vehicleId = Helper.getStringInputFromUser();
//				
//				if(!this.vehicleIdAlreadyExists(vehicleId)) {
//					throw new Exception("A vehicle " + vehicleId + " does not exist");
//				}
//				
//				maintVehicle = this.findVehicle(vehicleId);
//				
//				if(maintVehicle.getVehicleType().equals("Car")) {
//					maintVehicle = (Car) maintVehicle;
//				}
//				else {
//					maintVehicle = (Van) maintVehicle;
//				}
//				
//				if(maintVehicle.performMaintenance()) {
//					System.out.println("Maintenance request successfull: Vehicle " + vehicleId + " is now under maintenance");
//				}
//			}catch(Exception e)
//			{
//				System.out.println(e.getMessage());
//			}
//		}
//		
//		
//		public void completeMaintenance() {
//			try {
//				Vehicle maintVehicle;
//				
//				System.out.println("**** Complete Maintenance ****");
//				System.out.println("vehicleId: ");
//				String vehicleId = Helper.getStringInputFromUser();
//				
//				if(!this.vehicleIdAlreadyExists(vehicleId)) {
//					throw new Exception("A vehicle " + vehicleId + " does not exist");
//				}
//				
//				maintVehicle = this.findVehicle(vehicleId);
//				
//				if(maintVehicle.getVehicleType().equals("Car")) {
//					maintVehicle = (Car) maintVehicle;
//				}
//				else {
//					maintVehicle = (Van) maintVehicle;
//				}
//				
//				System.out.println("CompletionDate:");
//				DateTime completionDate = Helper.checkDate();
//				
//				if(maintVehicle.completeMaintenance(completionDate)) {
//					System.out.println("Vehicle " + vehicleId + " has all maintenannce completed and ready for rent");
//				}
//			}catch(Exception e)
//			{
//				System.out.println(e.getMessage());
//			}
//		}
//		
//		
//		public void displayAllVehicles() {
//			for(Vehicle v : this.getVehicles()) {
//				System.out.println(v.getDetails());
//			}
//			if(this.getVehicles().size() == 0) {
//				System.out.println("No vehicles available in the system");
//			}
//		}
//		
//		public boolean vehicleIdAlreadyExists(String vehicleId) {
//			for(Vehicle v : this.getVehicles()) {
//				if(v.getVehicleId().toUpperCase().equals(vehicleId.toUpperCase()))
//					return true;
//			}
//			return false;
//		}
//		
//		public Vehicle findVehicle(String vehicleId) {
//			for(Vehicle v : this.getVehicles()) {
//				if(v.getVehicleId().toLowerCase().equals(vehicleId.toLowerCase())) {
//					return v;
//				}
//			}
//			return null;
//		}
//}
