package model;

import java.util.*;
import util.DateTime;
import view.MainProgram;

public class Van extends Vehicle{

	private static final int noOfSeats = 15;
	private static final int minRentDays = 1;
	private static final double rentPerDay = 235;
	private static final double lateFeePerDay = 299;
	private static final int maintenancePeriod = 12;
	private DateTime lastMaintenanceDate;
	
	
	public Van(String vehicleId, int year, String make, String model, DateTime lastMaintenanceDate, String imagePath) {
		super(vehicleId, year, make, model, Van.noOfSeats, "Van", imagePath);
		this.lastMaintenanceDate = lastMaintenanceDate; 
	}
	
	
	public DateTime getLastMaintenanceDate() {
		return lastMaintenanceDate;
	}


	public void setLastMaintenanceDate(DateTime lastMaintenanceDate) {
		this.lastMaintenanceDate = lastMaintenanceDate;
	}

	
	public boolean rent(String customerId, DateTime rentDate, int numOfrentDay) throws Exception {
		DateTime estimatedReturnDate = new DateTime(rentDate, numOfrentDay);
		DateTime returnDateCheck = new DateTime();
		
		if(getVehicleStatus() != VehicleStatus.available) {
			throw new Exception("Vehicle is not currently available for rent");
		}

		// check rentdate with currentDate
		if(DateTime.diffDays(rentDate,new DateTime()) < -1) {
			throw new Exception("Vehicle can not be rented for past days");
		}
		
		// check minimum number of allowed rent days
		if(numOfrentDay < Van.minRentDays) {
			throw new Exception("Vehicle can not be hired for less than " + Van.minRentDays + " Days");
		}

		// check whether maintenance period criteria
		if(DateTime.diffDays(rentDate, lastMaintenanceDate) >= Van.maintenancePeriod || numOfrentDay > Van.maintenancePeriod & DateTime.diffDays(estimatedReturnDate , this.lastMaintenanceDate) > Van.maintenancePeriod) {
			
			throw new Exception("Maintenance period criterea mismatch");
		}
 
		for(RentalRecord tempRecord : this.getRecord()) {
			if(DateTime.diffDays(returnDateCheck, tempRecord.getActualReturnDate()) < 0) {
				returnDateCheck = tempRecord.getActualReturnDate();
			}
		}
		
		// Check if rentDate is after last returnDate
		if(DateTime.diffDays(rentDate, returnDateCheck) < 0) {
			throw new Exception("Car can not be rented before date : " + returnDateCheck);
		}
		this.setVehicleStatus(VehicleStatus.rented);
		String rentRecordId = this.getVehicleId() + customerId + rentDate.getEightDigitDate(); 
		RentalRecord currentPerDayRecord = new RentalRecord(rentRecordId, rentDate, estimatedReturnDate);
		
		// Update database for current vehicle instance
		MainProgram.getDATA_LINK().updateVehicleDetails(this);
		MainProgram.getDATA_LINK().insertRentalRecord(currentPerDayRecord, this.getVehicleId());
		return true;
	}


		
	public boolean returnVehicle(DateTime returnDate) throws Exception {
		ArrayList<RentalRecord> allRecord = MainProgram.getDATA_LINK().getVehicleRentalRecords(this.getVehicleId());
		int sizeOfRecord = allRecord.size();
		RentalRecord currentRecord = allRecord.get(sizeOfRecord -1 );
		
		for(RentalRecord rr : allRecord) {
			if(rr.getActualReturnDate() == null)
			{
				currentRecord = rr;
			}
		}
	
		int actualRentDays= DateTime.diffDays(returnDate, currentRecord.getRentDate());
		int estimatedDays = DateTime.diffDays(currentRecord.getEstimatedReturnDate(), currentRecord.getRentDate());
			
		if(this.getVehicleStatus() != VehicleStatus.rented) {
			throw new Exception("Vehicle is not currently rented. False attempt to return a vehicle");
		}
		
		// check if return date is prior to rentPerDay date
		if(DateTime.diffDays(returnDate, currentRecord.getRentDate()) <= 0) {
			throw new Exception("Return date can not be prior to rent date");
		}
		
		// calculate and set rental and late fee
		if(actualRentDays < estimatedDays) {
			currentRecord.setRentalFee(Van.rentPerDay * actualRentDays);
			currentRecord.setLateFee(0);
		}
		else {
			currentRecord.setRentalFee(Van.rentPerDay * estimatedDays);
			currentRecord.setLateFee(Van.lateFeePerDay * (actualRentDays - estimatedDays));
		}
		
		currentRecord.setActualReturnDate(returnDate);
		this.setVehicleStatus(VehicleStatus.available);

		// Update in database for current vehicle instance
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
		this.setLastMaintenanceDate(completionDate);
		MainProgram.getDATA_LINK().updateVehicleDetails(this);
		return true;
	}
	
	public String toString() {
		return super.toString() + ":" + this.getLastMaintenanceDate() + ":" + this.getImagePath();
	}

	public String getDetails() throws Exception {
		String result = "";
		String vehicleId = "Vehicle ID:\t\t"+this.getVehicleId() + "\n";
		String year = "Year:\t\t\t"+this.getYear() + "\n";
		String make = "Make:\t\t\t"+this.getMake()+ "\n";
		String model = "Model:\t\t\t"+this.getModel()+ "\n";
		String noOfSeats = "Number of Seats:\t"+this.getNoOfSeats()+ "\n";
		String vehicleStatus = "Vehicle Status:\t\t"+this.getVehicleStatus()+ "\n";
		String lastMaintenanceDate = "Last Maintenance Date:\t"+this.getLastMaintenanceDate()+ "\n";
		result = vehicleId + year + make + model + noOfSeats + vehicleStatus + lastMaintenanceDate;
		
		String record = "";
		if(this.getRecord().size() != 0) {
			for(RentalRecord r : this.getRecord()) {
			record = record + r.getDetails();
			}
		}
		result = result + record;
		return result;
	}
}