package view;

import java.time.LocalDate;

import controller.VehicleController;
//import controller.BookVehicleController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Vehicle;
import util.DateTime;

// View for booking a vehicle
public class BookDialogBoxView {
	public void bookingDisplay(Vehicle vehicle) {
		Stage bookStage = new Stage();
		bookStage.initModality(Modality.APPLICATION_MODAL);
		bookStage.setTitle("Book Vehicle");
		bookStage.setMinWidth(500);
		
		GridPane bookGrid = new GridPane();
		bookGrid.setPadding(new Insets(10, 10, 10, 10)); 
		bookGrid.setVgap(15);
		bookGrid.setHgap(30);
		bookGrid.setAlignment(Pos.CENTER);
		
		Label headingLabel = new Label("Please enter details for booking (All fields are mandatory)");
		Label customerIdLabel = new Label("Customer Id (4 digit number)");
		Label rentDateLabel = new Label("Booking Start Date");
		Label noOfDaysLabel = new Label("No of Days");
		
		TextField customerId = new TextField();
		try {
			
			DatePicker rentDate = new DatePicker();
			rentDate.setValue(LocalDate.now());
			rentDate.setDayCellFactory(picker -> new DateCell() {
			       public void updateItem(LocalDate date, boolean empty) {
			           super.updateItem(date, empty);
			           LocalDate today = LocalDate.now();
			           setDisable(empty || date.compareTo(today) < 0);
			       }
			   });
			
			
			TextField noOfDays = new TextField();
			customerId.setPromptText("Enter a 4 digit number");
			rentDate.setPromptText("DD/MM/YYYY");
			rentDate.setTooltip(new Tooltip("Should be a future Date"));
			
			// Validate user inputs and book a vehicle on submit
			Button submit = new Button("Submit");
			submit.setOnAction(e -> { 
				String validationErrors = "";
				String dayErrors = "";			
				validationErrors = validationErrors + this.validateDetail("Customer ID", customerId);
				if(validationErrors.isEmpty()) 
					validationErrors = validationErrors + this.isIntegerVal("Customer ID", customerId);
				if(validationErrors.isEmpty() && customerId.getText().length() != 4 ) {
					validationErrors = validationErrors + "Customer ID input must a 4 digit number \n";
					customerId.clear();}
				dayErrors = dayErrors + this.validateDetail("Number of days", noOfDays);
				if(dayErrors.trim().isEmpty())
					dayErrors = dayErrors + this.isIntegerVal("No of Days", noOfDays);			
				validationErrors += dayErrors;			
				if(!validationErrors.trim().isEmpty()) {
					AlertPopupView.display("Error in input", validationErrors);
				} else {
					String customerIdVal = customerId.getText();
					LocalDate rentDateVal = rentDate.getValue();
					DateTime dt = new DateTime(rentDateVal.getDayOfMonth(), rentDateVal.getMonthValue() ,rentDateVal.getYear());
					int noOfDaysVal = Integer.parseInt(noOfDays.getText());
					try {
						new VehicleController().rentVehicle(vehicle, customerIdVal , dt, noOfDaysVal);
					} catch (Exception e1) {
						new AlertPopupView().display("Booking Failed", e1.getMessage());
					}
					bookStage.close();
				}	
			});
			
			// To clear all inputs in TextFields
			Button clear = new Button("Clear all fields");
			clear.setOnAction(e -> {
				customerId.clear();
				noOfDays.clear();
				
			}); 
			
			// Close the current view dialog for Booking Vehicle  
			Button close = new Button("Close");
			close.setOnAction(e -> bookStage.close());
	
			// Add to Layout
			bookGrid.add(headingLabel, 0, 0);
			bookGrid.add(customerIdLabel, 0, 1);
			bookGrid.add(rentDateLabel, 0, 2);
			bookGrid.add(noOfDaysLabel, 0, 3);
			bookGrid.add(customerId, 1, 1);
			bookGrid.add(rentDate, 1, 2);
			bookGrid.add(noOfDays, 1, 3);
			
			VBox containerVBox = new VBox(20);
			HBox hbox = new HBox(10);
			hbox.getChildren().addAll(clear, submit, close);
			containerVBox.getChildren().addAll(
					bookGrid,
					hbox
					);
			containerVBox.setAlignment(Pos.CENTER);
			containerVBox.setMinHeight(300);
			containerVBox.setMinWidth(450);
			containerVBox.setPadding(new Insets(20,20,20,20));
			Scene sc = new Scene(containerVBox);
			
			bookStage.setScene(sc);
			bookStage.showAndWait();
		} catch(Exception e) {
			throw e;
		}
		}
	
	// Checks for Empty TextField values
	public String validateDetail(String labelName ,TextField item) {
		String validationFailures = "";
		if(item.getText().trim().isEmpty() || !item.getText().matches("^[a-zA-Z0-9]*$")) {
			validationFailures = "Invalid Input in field " + labelName + "\n";
			item.clear();
		}		
		return validationFailures;
	}
	
	// Checks for Integer in TextField values
	public String isIntegerVal(String labelName, TextField item) {
		String validationFailures = "";
		try {		
			Integer.parseInt(item.getText());
		}
		catch (Exception e) {
			validationFailures = "Non Integer Input in field " + labelName + "\n";
			item.clear();
		}
		return validationFailures;
	}
	
}
