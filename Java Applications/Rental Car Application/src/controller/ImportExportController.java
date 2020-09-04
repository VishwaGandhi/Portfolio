package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.stage.FileChooser;
import model.Car;
import model.RentalRecord;
import model.Van;
import model.Vehicle;
import util.DateTime;
import util.Helper;
import view.CustomExceptionBox;
import view.MainProgram;

public class ImportExportController {
	// To Import data from a text file stored at user selected Directory
	public static Boolean importData(String filepath) throws Exception {
		Boolean result;;
		try {
			Scanner input = new Scanner(new FileInputStream(filepath));
			String vehicleId = null;
			// clean database before importing
			MainProgram.getDATA_LINK().cleanAllData();
			
			while (input.hasNextLine()) {
				String line = input.nextLine();
				Vehicle vehicle;
				RentalRecord record;
				String item[] = line.split(":");
				
				for(int i=0; i< item.length;i++)
					item[i] = item[i].trim();
				
				// Create and Insert Vehicle Objects in Vehicle Database
				if(item[0].length() == 6 || item[5].trim().equalsIgnoreCase("Car") || item[5].trim().equalsIgnoreCase("Van")) {
					vehicleId = item[0];
					int year = Integer.parseInt(item[1]);
					String make = item[2];
					String model = item[3];
					int noOfSeats = Integer.parseInt(item[4]);
					String vehicleType = item[5];
					DateTime lastMaintenanceDate;
					String imagepath;
					
					if(vehicleType.trim().equalsIgnoreCase("Van")) {
						lastMaintenanceDate = Helper.convertToDate(item[6].trim());
						imagepath = item[7];
						vehicle = new Van(vehicleId, year, make, model, lastMaintenanceDate, imagepath);
					} else {
						imagepath = item[6];
						vehicle = new Car(vehicleId, year, make, model, noOfSeats, imagepath);		
					}
					MainProgram.getDATA_LINK().insertNewVehicle(vehicle);
				}else {
					// Create and Insert RentalRecord data into Database
					String recordId = item[0];
					DateTime rentDate = Helper.convertToDate(item[1].trim());
					DateTime estimatedReturnDate = Helper.convertToDate(item[2]);
					
					record = new RentalRecord(recordId,rentDate, estimatedReturnDate);
					
					if(!(item[3].trim().equalsIgnoreCase("null"))) {
						DateTime actualReturnDate =Helper.convertToDate(item[3]);
						double rentalFee = Double.parseDouble(item[4]);
						double lateFee = Double.parseDouble(item[5]);
						
						record.setActualReturnDate(actualReturnDate);
						record.setRentalFee(rentalFee);
						record.setLateFee(lateFee);
					}
					MainProgram.getDATA_LINK().insertRentalRecord(record, vehicleId);					
				}
			}
			input.close();
			result = true;
		}	catch(FileNotFoundException fe) {
			result = false;
			throw new Exception("Import from a file failed: \n"+ fe.getMessage());
		}
		return result;
	}
	
	// Export data in a text file at user selected Directory
	public static Boolean exportData(String filepath) throws Exception {
		Boolean result;
		FileWriter writer = null;
		
		try {
			File file= new File(filepath, "export_data.txt");
			writer = new FileWriter(file);
			ArrayList<Vehicle> vehicle = MainProgram.getDATA_LINK().getVehicles();
			
			// Export vehicles and records to a text file
			for(Vehicle v: vehicle) {
				writer.write(v.toString());
				writer.write(System.getProperty( "line.separator" ));
				ArrayList<RentalRecord> records = MainProgram.getDATA_LINK().getVehicleRentalRecords(v.getVehicleId());
				for(RentalRecord r : records) {
					writer.write(r.toString());
					writer.write(System.getProperty( "line.separator" ));
				}
			}		
			writer.close();
			result = true;
		} catch (IOException ioe) {
			result = false;
			throw new Exception("Export file failed: \n"+ ioe.getMessage());
		}
		return result;
	}
}
