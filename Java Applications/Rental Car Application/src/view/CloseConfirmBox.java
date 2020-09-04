package view;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

// Generates alert before closing program completely
public class CloseConfirmBox {
	static boolean answer;
	public static boolean displayConfirmBox() {
		Stage alertStage = new Stage();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Sure, you want to exit?");

		ButtonType yesButton = new ButtonType("Yes");
		ButtonType noButton = new ButtonType("No");
		
		alert.getButtonTypes().setAll(yesButton, noButton);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == yesButton){
			CloseConfirmBox.answer = true;
			alertStage.close();
		} else if (result.get() == noButton) {
			CloseConfirmBox.answer = false;
			alertStage.close();
		}
	
		return CloseConfirmBox.answer;
	}
	
}
