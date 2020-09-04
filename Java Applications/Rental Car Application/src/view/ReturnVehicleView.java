package view;
import java.time.LocalDate;

import controller.VehicleController;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.*;
import model.Vehicle;
import util.DateTime;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

// View for Returning a vehicle
public class ReturnVehicleView {
	public void display(Vehicle vehicle) {
	Stage window = new Stage();
	window.initModality(Modality.APPLICATION_MODAL);
	window.setTitle("");
	window.setMinWidth(250);

	Label reutrnLabel = new Label("Select Return Date");
	
	DatePicker returnDate = new DatePicker();
	returnDate.setValue(LocalDate.now());
	returnDate.setDayCellFactory(picker -> new DateCell() {
	       public void updateItem(LocalDate date, boolean empty) {
	           super.updateItem(date, empty);
	           LocalDate today = LocalDate.now();
	           setDisable(empty || date.compareTo(today) < 0);
	       }
	   });

	// Checks validity of return date and performs necessary operations to return a vehicle
	Button submit = new Button("Submit");
	submit.setOnAction(e -> {
		LocalDate returnDateVal = returnDate.getValue();
		DateTime dt = new DateTime(returnDateVal.getDayOfMonth(), returnDateVal.getMonthValue() ,returnDateVal.getYear());
		try {
			new VehicleController().returnVehicle(vehicle, dt);
		} catch (Exception e1) {
			new AlertPopupView().display("Booking Failed", e1.getMessage());
		}
		window.close();
	});

	
	Button close = new Button("Close");
	close.setOnAction(e -> window.close());

	GridPane grid = new GridPane();
	grid.add(reutrnLabel, 0,0);
	grid.add(returnDate, 1, 0);
	grid.add(submit, 0, 1);
	grid.add(close, 1, 1);
	grid.setVgap(30);
	grid.setHgap(30);
	grid.setAlignment(Pos.CENTER);
	grid.setMinHeight(100);
	grid.setMinWidth(250);
	grid.setPadding(new Insets(20,20,20,20));
	Scene sc = new Scene(grid);
	window.setScene(sc);
	window.showAndWait();
	}
}
