package view;


import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.geometry.Pos;

// Alert window to display message in a dialog
public class AlertPopupView {
	public static void display(String title, String msg) {
		Stage window = new Stage();
		VBox layout = new VBox(20);
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(350);
		window.setMinHeight(250);
		layout.setAlignment(Pos.CENTER);
		
		Label alertLabel = new Label();
		alertLabel.setText(msg);
		
		Button alertButton = new Button("Close");
		alertButton.setOnAction(e -> window.close());
		
		layout.getChildren().addAll(alertLabel,alertButton);		
		Scene sc = new Scene(layout);
		window.setScene(sc);
		window.showAndWait();
	}	
}
