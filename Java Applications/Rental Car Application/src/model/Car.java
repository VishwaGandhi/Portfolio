package model;

import java.util.ArrayList;
import util.DateTime;
import view.MainProgram;

public class Car extends Vehicle {


	private static final int minDaysSunToThur = 2;
	private static final int minDaysFriSat = 3;
	private static final int hireDaysMaxlimit = 14;
	private static final double rentFor4seats = 78;
	private static final double rentFor7seats = 113;
	private static final double lateFeeRatePerDay = 1.25;
	
	
	public Car(String vehicle_id, int year, String make, String model, int no_of_passengers, String imagePath)
	{
		super(vehicle_id, year, make, model, no_of_passengers, "Car", imagePath);		
	}
	
	
	public boolean rent(String customerId, DateTime rentDate, int numOfRentDay) throws Exception {
		DateTime returnDateCheck = new DateTime();
	
		// check availability of vehicle
		if(getVehicleStatus() != VehicleStatus.available) {
			throw new Exception("Vehicle is not currently available for rent");
		}
		
		// check rentDate with currentDate
		if(DateTime.diffDays(rentDate,new DateTime()) < -1) {
			throw new Exception("Vehicle can not be rented for past days");
		}
		
		// check maximum hire days limit
		if(numOfRentDay > Car.hireDaysMaxlimit) {
			throw new Exception("Vehicle can not be hired for more than " + Car.hireDaysMaxlimit + " Days");
		}
		
		// check minimum rent days limits from Sunday to Thursday
		if((rentDate.getNameOfDay().equals(DayOfWeek.Sunday.toString()) || rentDate.getNameOfDay().equals(DayOfWeek.Monday.toString()) || rentDate.getNameOfDay().equals(DayOfWeek.Tuesday.toString()) || rentDate.getNameOfDay().equals(DayOfWeek.Wednesday.toString()) || rentDate.getNameOfDay().equals(DayOfWeek.Thursday.toString())) & numOfRentDay < Car.minDaysSunToThur) {
			throw new Exception("Car can not be rented for less than " + Car.minDaysSunToThur +" days from Sunday to Thursday");
		}
		
		// check minimum rent days limits on Friday and Saturday
		if((rentDate.getNameOfDay().equals(DayOfWeek.Friday.toString()) || rentDate.getNameOfDay().equals(DayOfWeek.Saturday.toString())) & numOfRentDay < Car.minDaysFriSat ) {
			throw new Exception("Car can not be rented for less than " + Car.minDaysFriSat + " days on Friday and Saturday");
		}
			
		for(RentalRecord tempRecord : this.getRecord()) {
			if(DateTime.diffDays(returnDateCheck, tempRecord.getActualReturnDate()) <= 0) {
				returnDateCheck = tempRecord.getActualReturnDate();
			}
		}
		// Check if rentDate is after last returnDate
		if(DateTime.diffDays(rentDate, returnDateCheck) < 0) {
			throw new Exception("Car can not be rented before date : " + returnDateCheck);
		}

		// update vehicleStatus to rented and add rental record according to new rental request
		this.setVehicleStatus(VehicleStatus.rented);
		String rentRecordId = this.getVehicleId() + customerId + rentDate.getEightDigitDate();
		DateTime estimatedReturnDate = new DateTime(rentDate, numOfRentDay); 
		RentalRecord currentRecord = new RentalRecord(rentRecordId, rentDate, estimatedReturnDate);
		
		
		// Updating Database RentalRecord for current vehicle instance
		MainProgram.getDATA_LINK().updateVehicleDetails(this);
		MainProgram.getDATA_LINK().insertRentalRecord(currentRecord, this.getVehicleId());
		
		return true;
	}

	// To return a vehicle
	public boolean returnVehicle(DateTime returnDate) throws Exception{
		int sizeOfRecord = this.getRecord().size();

		ArrayList<RentalRecord> allRecord = MainProgram.getDATA_LINK().getVehicleRentalRecords(this.getVehicleId());
		RentalRecord currentRecord = allRecord.get(sizeOfRecord -1);

		for(RentalRecord rr : allRecord) {
			if(rr.getActualReturnDate() == null)
			{
				currentRecord = rr;
			}
		}
		
		int actualRentDays= DateTime.diffDays(returnDate, currentRecord.getRentDate());
		int estimatedDays = DateTime.diffDays(currentRecord.getEstimatedReturnDate(), currentRecord.getRentDate());
		int minRentDays;
		double rent;
			
		if(this.getVehicleStatus() != VehicleStatus.rented) {
			throw new Exception("Vehicle is not currently rented. False attempt to return a vehicle");
		}
		
		// check if return date is prior to rentDate of that record
		if(DateTime.diffDays(returnDate, currentRecord.getRentDate()) < 0) {
			throw new Exception("Return ddate can not be prior to rent date");
		}
		
		// calculate and set rental and late fee
		if(currentRecord.getRentDate().getNameOfDay().equals(DayOfWeek.Friday.toString()) || currentRecord.getRentDate().getNameOfDay().equals(DayOfWeek.Saturday.toString())) {
			minRentDays = Car.minDaysFriSat;
		} else {
			minRentDays = Car.minDaysSunToThur;
		}
		
		
		if(this.getNoOfSeats() == 4)
		{
			rent = Car.rentFor4seats;
		} else {
			rent = Car.rentFor7seats;
		}
		
		
		if (actualRentDays < estimatedDays) {
			if(actualRentDays > minRentDays) {	
				currentRecord.setRentalFee(rent * actualRentDays);		
			} else {
				currentRecord.setRentalFee(rent * minRentDays);
			}
			currentRecord.setLateFee(0);
		} else {
			currentRecord.setRentalFee(rent * estimatedDays);	
			currentRecord.setLateFee((Car.lateFeeRatePerDay) * rent * (actualRentDays - estimatedDays));
		}
		
		currentRecord.setActualReturnDate(returnDate);
		this.setVehicleStatus(VehicleStatus.available);
		
		// Update Rental Record in a database for a current vehicle instance
		MainProgram.getDATA_LINK().updateVehicleDetails(this);
		MainProgram.getDATA_LINK().updateRentalRecord(currentRecord, this.getVehicleId());
		return true;
	}

	
	public boolean performMaintenance() throws Exception {
		if(this.getVehicleStatus() == VehicleStatus.rented) {
			throw new Exception("Vehicle is already being rented. Can not perform maintenance at the moment");
		}
		this.setVehicleStatus(VehicleStatus.underMaintanance);
		MainProgram.getDATA_LINK().updateVehicleDetails(this);
		return true;
	}
	
	
	public boolean completeMaintenance(DateTime completionDate) throws Exception {
		if(this.getVehicleStatus() != VehicleStatus.underMaintanance) {
			throw new Exception("Vehicle is not under maintenance");
		}
		this.setVehicleStatus(VehicleStatus.available);
		MainProgram.getDATA_LINK().updateVehicleDetails(this);
		return true;
	}
	
	// To create Vehicle detials and rental records in single line
	public String getDetails() throws Exception {
		String result = "";
		String vehicleType = "Vehicle Type:\t\t"+this.getVehicleType() + "\n";
		String vehicleId = "Vehicle ID:\t\t"+this.getVehicleId() + "\n";
		String year = "Year:\t\t\t"+this.getYear() + "\n";
		String make = "Make:\t\t\t"+this.getMake()+ "\n";
		String model = "Model:\t\t\t"+this.getModel()+ "\n";
		String noOfSeats = "Number of Seats:\t"+this.getNoOfSeats()+ "\n";
		String vehicleStatus = "Vehicle Status:\t\t"+this.getVehicleStatus()+ "\n";
				
		String record = "";
		if(this.getRecord().size() != 0) {
			
			record = record + "\n Rental Records \n";
			for(RentalRecord r : this.getRecord()) {
			record = record + r.getDetails();
			}
		} 
		result = vehicleType + vehicleId + year + make + model + noOfSeats + vehicleStatus;
		result = result + record;
		return result;
	}
	
	// To create one line record for a vehicle
	public String toString() {
		return super.toString() + ":" + this.getImagePath();
	}
}
