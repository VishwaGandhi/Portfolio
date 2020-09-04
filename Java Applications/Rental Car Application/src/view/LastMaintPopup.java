package view;

import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.DateTime;

// View for fetching last maintenance date
public class LastMaintPopup {
	public DateTime display() {
		Stage alertBox = new Stage();
		alertBox.initModality(Modality.APPLICATION_MODAL);
		alertBox.setTitle("Complete Maintenance");
		alertBox.setMinWidth(250);
		
		Label label = new Label("Last Maintenance Date");
		
		DatePicker lastDate = new DatePicker();
		lastDate.setValue(LocalDate.now());		
		LocalDate returnDateVal = lastDate.getValue();
		DateTime dt = new DateTime(returnDateVal.getDayOfMonth(), returnDateVal.getMonthValue() ,returnDateVal.getYear());
		
		Button button = new Button("Submit");
		button.setOnAction(e -> alertBox.close());
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label,lastDate,button);
		layout.setAlignment(Pos.CENTER);
		layout.setMinHeight(100);
		layout.setMinWidth(350);
		layout.setPadding(new Insets(20,20,20,20));
		Scene sc = new Scene(layout);
		alertBox.setScene(sc);
		alertBox.showAndWait();
		return dt;
	}	
}
