package controller;

import model.Car;
import model.Van;
import model.Vehicle;
import util.DateTime;
import view.AlertPopupView;
import view.CustomExceptionBox;
import view.MainProgram;

public class VehicleController {
	// Controller to rent a vehicle
	public void rentVehicle(Vehicle vehicle, String id, DateTime rentDate, int days) throws Exception {
		try {
			Vehicle rentVehicle;
			
			if(vehicle.getVehicleType().equals("Car"))
				rentVehicle = (Car)vehicle;
			else
				rentVehicle = (Van)vehicle;
		
			String customerId = "CUS" + id;
			
			if(rentVehicle.rent(customerId, rentDate, days))
				AlertPopupView.display("Success", "Vehicle has been successfully booked");					
		}catch(Exception e) {
			throw new Exception("Can not rent vehicle: \n"+ e.getMessage());
		}
	}

	// Controller to return a vehicle
	public void returnVehicle(Vehicle vehicle, DateTime returnDate) throws Exception {
		try {
			Vehicle returnVehicle;
			
			if(vehicle.getVehicleType().equals("Car"))
				returnVehicle = (Car)vehicle;
			else
				returnVehicle = (Van)vehicle;
			if(returnVehicle.returnVehicle(returnDate))
				AlertPopupView.display("Success", "Vehicle has been successfully returned");
		}catch(Exception e) {
			throw new Exception("Can not return vehicle: \n"+ e.getMessage());
			
		}
	}

	// Controller to add new car in database
	public void addCar(Car car) throws Exception {
		try {
			if(! vehicleIdAlreadyExists(car.getVehicleId())) {
				MainProgram.getDATA_LINK().insertNewVehicle(car);
				AlertPopupView.display("Success", "Car added");
			}
			else {
				throw new Exception("Vehicle with same Vehicle ID already exists. Please choose another id" );
			}
		} catch(Exception e) {
			throw new Exception("Can not add a new Car: \n"+ e.getMessage());
		}
	}
	
	// Controller to add Van in database
	public void addVan(Van van) throws Exception{
		try {
			if(! vehicleIdAlreadyExists(van.getVehicleId())) {
				MainProgram.getDATA_LINK().insertNewVehicle(van);
				AlertPopupView.display("Success", "Van added");
			}
			else {
				throw new Exception("Vehicle with same Vehicle ID already exists. Please choose another id" );
			}
		} catch(Exception e) {
			throw new Exception("Can not add a new Van: \n"+ e.getMessage());
		}
	}
	
	// To check if vehicle with same vehicleId already exists in database
	public boolean vehicleIdAlreadyExists(String vehicleId) throws Exception {
		for(Vehicle v : MainProgram.getDATA_LINK().getVehicles()) {
			if(v.getVehicleId().toUpperCase().equals(vehicleId.toUpperCase()))
				return true;
		}
		return false;
	}
}
