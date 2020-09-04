package controller;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import model.*;
import util.Helper;

public class DataController {

	Connection con = null;
	Statement stmt = null;
	int result = 0;
	
	public DataController() throws Exception{
		try {
			//Registering the HSQLDB JDBC driver
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			//Creating the connection with HSQLDB
			con = DriverManager.getConnection("jdbc:hsqldb:file:database/thriftyrentdb", "SA", "");
			if (con!= null){
				System.out.println("Connection created successfully");
				
			}else{
				System.out.println("Problem with creating connection");
			}
			
		}  catch (Exception e) {
			throw e;
		}
		
	}
	
	// Insert new vehicle record
	public void insertNewVehicle(Vehicle vehicle) throws Exception {
        try {
        	String insertQuery;
        	stmt = con.createStatement();
        	
        	if(vehicle instanceof Car) {
        		insertQuery = "INSERT INTO vehicles ( \"VEHICLEID\", \"YEAR\", \"MAKE\", \"MODEL\", \"NOOFSEATS\", \"VEHICLETYPE\",  \"VEHICLESTATUS\",  \"IMAGEPATH\") "
        	        	+ "VALUES('" 
        	        	+ vehicle.getVehicleId() + "'," 
        	        	+ vehicle.getYear() + ",'"
        	        	+ vehicle.getMake() + "','"
        	        	+ vehicle.getModel() + "',"
        	        	+ vehicle.getNoOfSeats() + ",'"
        	        	+ vehicle.getVehicleType() + "','"
        	        	+ vehicle.getVehicleStatus().toString() + "','" 
        	        	+ vehicle.getImagePath() + "'"
        	        	+ ")";
        	} else {
	        	insertQuery = "INSERT INTO vehicles ( \"VEHICLEID\", \"YEAR\", \"MAKE\", \"MODEL\", \"NOOFSEATS\", \"VEHICLETYPE\", \"VEHICLESTATUS\", \"LASTMAINTENANCEDATE\",  \"IMAGEPATH\" ) "
	        	+ "VALUES('" 
	        	+ vehicle.getVehicleId() + "'," 
	        	+ vehicle.getYear() + ",'"
	        	+ vehicle.getMake() + "','"
	        	+ vehicle.getModel() + "',"
	        	+ vehicle.getNoOfSeats() + ",'"
	        	+ vehicle.getVehicleType() + "','"
	        	+ vehicle.getVehicleStatus().toString() + "'," 
	        	+ "TO_DATE('"+((Van)vehicle).getLastMaintenanceDate().getFormattedDate() + "','DD/MM/YYYY'), '"
	        	+ vehicle.getImagePath() + "')";
        	}	
        	result = stmt.executeUpdate(insertQuery);
			
		} catch (SQLException e) {
			throw e;
		}
	}
	
	// Get list of all vehicles
	public ArrayList<Vehicle> getVehicles() throws Exception{
		 ResultSet vehicleSet = null;
		 ArrayList<Vehicle> vehicles  = new ArrayList<Vehicle>(50);
	      try {
	         stmt = con.createStatement();
	         String selectQuery = "SELECT * FROM vehicles"; 
	         vehicleSet = stmt.executeQuery(selectQuery);
	         
	         while(vehicleSet.next()){
	        	 Vehicle vehicleObject;
	        	 if (vehicleSet.getString("vehicleType").equals("Car")) {
	        		 vehicleObject = new Car(
	        				 vehicleSet.getString("vehicleId"), 
	        				 vehicleSet.getInt("year"),
	        				 vehicleSet.getString("make"),
		        			 vehicleSet.getString("model"),
		        			 vehicleSet.getInt("noOfSeats"),
		        			 vehicleSet.getString("imagepath")
		        			 );
	        	 }
	        	 else {
	        		 Date dt = vehicleSet.getDate("lastMaintenanceDate");
	        		         		 
	        		 vehicleObject = new Van(
	        				 vehicleSet.getString("vehicleId"), 
	        				 vehicleSet.getInt("year"),
	        				 vehicleSet.getString("make"), 
	        				 vehicleSet.getString("model"),
	        				 Helper.convertToDateDB(dt.toString()),
	        				 vehicleSet.getString("imagepath"));
	        	 }
	        	 vehicleObject.setImagePath(vehicleSet.getString("imagePath"));
	        	 vehicleObject.setRecord(this.getVehicleRentalRecords(vehicleObject.getVehicleId()));
	        	 vehicleObject.setVehicleStatus(vehicleSet.getString("vehiclestatus").equalsIgnoreCase("available") ? VehicleStatus.available : 
	        		 vehicleSet.getString("vehiclestatus").equalsIgnoreCase("rented") ? VehicleStatus.rented : VehicleStatus.underMaintanance);
	        	 vehicles.add(vehicleObject);
	        	 
	         }
	      } catch (Exception e) {
	    	  throw e;
	      }
	      
	      return vehicles;
	}
	
	public ArrayList<Vehicle> getVehicle(String vehicleId) throws Exception {
		ResultSet vehicleSet = null;
		ArrayList<Vehicle> vehicle  = new ArrayList<Vehicle>(1);
		try {
			stmt = con.createStatement();
			vehicleSet = stmt.executeQuery("SELECT * FROM vehicles where vehicleID = '" + vehicleId +"'" );
			Car car = new Car(
					vehicleSet.getString("vehicleId"),
					vehicleSet.getInt("year"), 
					vehicleSet.getString("make"),
       			 	vehicleSet.getString("model"), 
       			 	vehicleSet.getInt("noOfSeats"),
       			 	vehicleSet.getString("imagepath")
       			 	);
			
			vehicle.add(car);
		} catch ( Exception e) {
			throw e;
		}
		return vehicle;
	}
	
	public ArrayList<RentalRecord> getVehicleRentalRecords(String vehicleId) throws Exception{
		ResultSet recordSet = null;
		ArrayList<RentalRecord> recordList = new ArrayList<RentalRecord>();
		try {
			stmt = con.createStatement();
			recordSet = stmt.executeQuery("SELECT * FROM records where vehicleID = '" + vehicleId.toUpperCase() +"'" );
			while(recordSet.next()) {

				RentalRecord record = new RentalRecord(
						recordSet.getString("recordId"),
						Helper.convertToDateDB(recordSet.getDate("rentDate").toString()),
						Helper.convertToDateDB(recordSet.getDate("estimatedReturnDate").toString())
	       			 	);
				if(recordSet.getDate("actualReturnDate") != null) {
					record.setActualReturnDate(Helper.convertToDateDB(recordSet.getDate("actualReturnDate").toString()));
				}
				record.setRentalFee(recordSet.getDouble("rentalFee"));
				record.setLateFee(recordSet.getDouble("lateFee"));
				recordList.add(record);
				Collections.sort(recordList);
			}
		} catch ( Exception e) {
			throw e;
		}
		return recordList;
	}
	
	
	
	public void updateVehicleDetails(Vehicle updateVehicle) throws Exception{
		try {
			Statement stmt = con.createStatement();
			int result = 0;
			String updateQuery ="UPDATE vehicles SET "
			+ "YEAR = '"+ updateVehicle.getYear() + "',"
			+ "MAKE = '"+ updateVehicle.getMake().toString() + "',"
			+ "MODEL = '"+ updateVehicle.getModel().toString() + "',"
			+ "NOOFSEATS = " + updateVehicle.getNoOfSeats() + " ,"
			+ "VEHICLETYPE = '" + updateVehicle.getVehicleType().toString() + "',"
			+ "VEHICLESTATUS = '" + updateVehicle.getVehicleStatus().toString() + "'"
			+ " WHERE VEHICLEID = '" + updateVehicle.getVehicleId() + "'";

			
			result = stmt.executeUpdate(updateQuery);
			if(result != 1) {
				throw new Exception("Vehicle Status updation failure");
			}
			}
			catch(Exception e) {
				throw e;
			}
		}
	
	public void insertRentalRecord(RentalRecord rentalRecord, String vehicleId) throws Exception {
		try {
			stmt = con.createStatement();
			String beta = "";
			String insertQuery;
			if(rentalRecord.getActualReturnDate() != null) {
					insertQuery = "Insert into RECORDS ( \"RECORDID\", \"VEHICLEID\", \"RENTDATE\", \"ESTIMATEDRETURNDATE\", \"ACTUALRETURNDATE\", \"RENTALFEE\", \"LATEFEE\" ) VALUES ('" +
						rentalRecord.getRecordId() + "','" +
						vehicleId + "'," + 
						"TO_DATE('" + rentalRecord.getRentDate().getFormattedDate() + "','dd/MM/yyyy') ," +
						"TO_DATE('" + rentalRecord.getEstimatedReturnDate().getFormattedDate() + "','dd/MM/yyyy')," +
						"TO_DATE('" + rentalRecord.getActualReturnDate().getFormattedDate() + "','dd/MM/yyyy')," +
						rentalRecord.getRentalFee() +"," +
						rentalRecord.getLateFee()+
						")";
			}
			else {
				insertQuery = "Insert into RECORDS ( \"RECORDID\", \"VEHICLEID\", \"RENTDATE\", \"ESTIMATEDRETURNDATE\" ) VALUES ('" +
					rentalRecord.getRecordId() + "','" +
					vehicleId + "'," + 
					"TO_DATE('" + rentalRecord.getRentDate().getFormattedDate() + "','dd/MM/yyyy') ," +
					"TO_DATE('" + rentalRecord.getEstimatedReturnDate().getFormattedDate() + "','dd/MM/yyyy')" +
					")";
			}
			result = stmt.executeUpdate(insertQuery);
			
		} catch ( Exception e) {
			 throw e;
		}	
	}
	
	public void updateRentalRecord(RentalRecord rentalRecord, String vehicleId) throws Exception{
		try {
			stmt = con.createStatement();
			String updateQuery = "UPDATE records SET " +
					"\"ACTUALRETURNDATE\" = " + "TO_DATE('" + rentalRecord.getActualReturnDate().getFormattedDate() + "','DD/MM/YYYY') ," +
					"\"RENTALFEE\" = " + rentalRecord.getRentalFee() +
					",\"LATEFEE\" = " + rentalRecord.getLateFee() +
					" WHERE RECORDID = '"+rentalRecord.getRecordId()+"'";
			
			result = stmt.executeUpdate(updateQuery);			

		} catch ( Exception e) {
			throw e;
		}	
	}
	
	public ArrayList<String> getUniqueMake() throws Exception{
		ResultSet makeSet = null;
		ArrayList<String> make = new ArrayList<String>();
		try {
			stmt = con.createStatement();
			makeSet = stmt.executeQuery("SELECT DISTINCT \"MAKE\" from vehicles" );
			
			while(makeSet.next()) {
				make.add(makeSet.getString("Make"));
			}
		} catch ( Exception e) {
			throw e;
		}	
		return make;
	}
	
	public Boolean cleanAllData() throws Exception {
		Boolean result = false;
		
		try {
			stmt = con.createStatement();
			int vehicleOutcome = stmt.executeUpdate("DELETE from VEHICLES" );
			int recordOutcome = stmt.executeUpdate("DELETE from RECORDS" );
			
			if(recordOutcome > 0 && vehicleOutcome >0)
				result = true;
		} catch ( Exception e) {
			throw e;
		}	
		return result;
	}	

}