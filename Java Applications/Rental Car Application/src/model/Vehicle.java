package model;

import java.util.ArrayList;
import util.DateTime;
import view.MainProgram;

public abstract class Vehicle {
	
	 	private String vehicleId;
		private int year;
		private String make;
		private String model;
		private int noOfSeats;
		private String vehicleType;
		private VehicleStatus vehicleStatus;
		private String imagePath;
		private ArrayList<RentalRecord> record;
		
		public Vehicle(String vehicleId, int year, String make, String model, int noOfSeats, String vehicleType, String imagePath)
		{
			this.setVehicleId(vehicleId);
			this.setYear(year);
			this.setMake(make);
			this.setModel(model);
			this.setNoOfSeats(noOfSeats);
			this.setVehicleType(vehicleType);
			this.setVehicleStatus(VehicleStatus.available);
			this.setImagePath(imagePath);
			this.record = new ArrayList<RentalRecord>(10);			
		}
		
		public void addRecord(RentalRecord newRecord) throws Exception {
			if(this.getRecord().size()>10) {
				this.record.remove(0);
				this.record.add(newRecord);
			}
			else {
				this.record.add(newRecord);
			}
		}
		
		// Override toString() 	method to return string with details of vehicle
		public String toString() {
			return this.getVehicleId() + ":" + this.getYear() + ":" + this.getMake() + ":" +
					this.getModel() + ":" + this.getNoOfSeats() + ":" + this.getVehicleType();
		}
		
		public abstract boolean rent(String customerId, DateTime rentDate, int numOfRentDay) throws Exception;
		public abstract boolean returnVehicle(DateTime returnDate) throws Exception;
		public abstract boolean performMaintenance() throws Exception;
		public abstract boolean completeMaintenance(DateTime completionDate) throws Exception;
		public abstract String getDetails() throws Exception;

		
		// Getter Methods
		public String getVehicleId() {
			return vehicleId;
		}

		public int getYear() {
			return year;
		}

		public String getMake() {
			return make;
		}

		public String getModel() {
			return model;
		}

		public int getNoOfSeats() {
			return noOfSeats;
		}

		public String getVehicleType() {
			return vehicleType;
		}

		public VehicleStatus getVehicleStatus() {
			return vehicleStatus;
		}

		public String getImagePath() {
			return imagePath;
		}

		public ArrayList<RentalRecord> getRecord() throws Exception {
			record = MainProgram.getDATA_LINK().getVehicleRentalRecords(this.getVehicleId());
			return record;
		}

		// Setter Methods
		public void setVehicleId(String vehicleId) {
			this.vehicleId = vehicleId;
		}

		public void setYear(int year) {
			this.year = year;
		}

		public void setMake(String make) {
			this.make = make;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public void setNoOfSeats(int noOfSeats) {
			this.noOfSeats = noOfSeats;
		}

		public void setVehicleType(String vehicleType) {
			this.vehicleType = vehicleType;
		}

		public void setVehicleStatus(VehicleStatus vehicleStatus) {
			this.vehicleStatus = vehicleStatus;
		}

		public void setImagePath(String imagePath) {
			this.imagePath = imagePath;
		}

		public void setRecord(ArrayList<RentalRecord> record) {
			this.record = record;
		}
}
