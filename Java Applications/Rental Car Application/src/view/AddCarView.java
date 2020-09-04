package view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import controller.VehicleController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Car;

// Generate dialog for Adding a new Car
public class AddCarView {
	public void addVehicleDisplay(){
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Add Car");
		window.setMinWidth(600);
		window.setMinHeight(500);
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(20, 20, 20, 20)); 
		VBox containerVBox = new VBox(20);
		HBox hbox = new HBox(10);
		grid.setVgap(15);
		grid.setHgap(30);
		grid.setAlignment(Pos.CENTER);
		containerVBox.setAlignment(Pos.CENTER);
		
		Label vehicleIdLabel = new Label("Vehicle Id (4 digit number)");
		Label makeLabel = new Label("Make");
		Label modelLabel = new Label("Model");
		Label yearLabel = new Label("Make year");
		Label noOfSeatsLabel = new Label("Number Of Seats");
		Label imageLabel = new Label("Image");
		
		TextField vehicleId = new TextField();
		TextField make = new TextField();
		TextField model = new TextField();
		TextField year = new TextField();
		TextField imageFile = new TextField();
		imageFile.setText("noimage.jpg");
		imageFile.setDisable(true);
		ChoiceBox<String> noOfSeats = new ChoiceBox<String>(FXCollections.observableArrayList("5", "7"));
		noOfSeats.getSelectionModel().selectFirst();
		
		Button createButton = new Button("Create");
		Button clear = new Button("Clear all fields");
		Button close = new Button("Close");
		
		// Import car image from chosen directory & Save to images directory
		FileChooser fileChooser = new FileChooser();
        final Button openFileChooser = new Button("Select image");
        openFileChooser.setOnAction(e -> {
        Path saveImagePath = Paths.get("images/"+vehicleId.getText()+".jpeg");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);
        File fileSelected = fileChooser.showOpenDialog(window);     
        if (fileSelected != null ) {
            try {
                Path originalPath = fileSelected.toPath();
                Files.copy(originalPath, saveImagePath, StandardCopyOption.REPLACE_EXISTING);
                imageFile.setText(saveImagePath.getFileName().toString());
            } catch (IOException ec) {
                AlertPopupView.display("File import error",  "There was some error importing image file.");
                imageFile.setText("noimage.jpg");
            }
        }
        });
	
        // Set action for create event
		createButton.setOnAction(e -> {
			String validationErrors = "";
			String yearErrors = "";
			
			// Validate user inputs
			validationErrors = validationErrors + this.validateDetail("Vehicle Id", vehicleId);
			if(validationErrors.trim().isEmpty()) 
				validationErrors = validationErrors + this.isIntegerVal("Vehicle Id", vehicleId);
			if(validationErrors.trim().isEmpty() && vehicleId.getText().length() != 4 ) {
				validationErrors = validationErrors + "Vehicle ID input must a 4 digit number \n";
				vehicleId.clear();}
			yearErrors = yearErrors + this.validateDetail("Year", year);
			if(yearErrors.trim().isEmpty()) 
				yearErrors = yearErrors + this.isIntegerVal("Year", year);
			if(yearErrors.trim().isEmpty() && (Integer.parseInt(year.getText()) < 1900 || Integer.parseInt(year.getText()) > 2019) ) {
				yearErrors = yearErrors + "Year value should be between 1900 and 2019 \n";
				year.clear();
			}
			validationErrors = validationErrors + this.validateDetail("Make", make);
			validationErrors = validationErrors + this.validateDetail("Model", model);
			if(noOfSeats.getValue() == null)
				validationErrors += "No of seats can't be empty \n";
					
			validationErrors += yearErrors;
			
			// If error generate alert else add Car object to database
			if(!validationErrors.trim().isEmpty()) {
				AlertPopupView.display("Error in input", validationErrors);
			} else {
				String vehicleIdVal = "C_" + vehicleId.getText();
				String yearVal = year.getText();
				String makeVal = make.getText();
				String modelVal = model.getText();
				String noOfSeatsVal = noOfSeats.getValue();
				String imagePath = imageFile.getText();
				
				Car holder = new Car(vehicleIdVal, Integer.parseInt(yearVal), makeVal, modelVal, Integer.parseInt(noOfSeatsVal), imagePath);
				
				try {
					new VehicleController().addCar(holder);
					window.close();
				} catch (Exception ec) { 
					CustomExceptionBox.displayCustomException(ec);
				}
			}
		});
	
		// Acction to clear all the fields
		clear.setOnAction(e -> {
			vehicleId.clear();
			make.clear();
			model.clear();
			year.clear();
		}); 
		
		// Close the add car view window
		close.setOnAction(e -> window.close());
		
	
//		layout.getChildren().addAll(vehicleIdLabel,vehicleId, yearLabel, year, makeLabel, make, modelLabel,model, noOfSeatsLabel,
		//noOfSeats,imageLabel, selectedFile, openFileChooser, button);
	
		grid.add(vehicleIdLabel, 0, 0);
		grid.add(vehicleId, 1, 0);
		grid.add(yearLabel, 0, 1);
		grid.add(year, 1, 1);
		grid.add(makeLabel, 0, 2);
		grid.add(make, 1, 2);
		grid.add(modelLabel, 0, 3);
		grid.add(model, 1, 3);
		grid.add(noOfSeatsLabel, 0, 4);
		grid.add(noOfSeats, 1, 4);
		grid.add(imageLabel, 0, 5);
		grid.add(imageFile, 1, 5);
		grid.add(openFileChooser, 0, 6);
		grid.add(clear, 0, 7);
		grid.add(createButton, 1, 7);
		grid.add(close, 0, 8);
		
		hbox.getChildren().addAll(clear, createButton, close);
		containerVBox.getChildren().addAll(
				grid,
				hbox
				);
		containerVBox.setAlignment(Pos.CENTER);
		containerVBox.setMinHeight(300);
		containerVBox.setMinWidth(450);
		containerVBox.setPadding(new Insets(20,20,20,20));
		Scene sc = new Scene(containerVBox);
		window.setScene(sc);
		window.showAndWait();
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
