package procurementsys;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Jan Tristan Milan
 */

public class AddNewPaymentController extends Controller {
	@FXML private DatePicker orderDatePicker;
	@FXML private ChoiceBox<String> hourChoiceBox;
	@FXML private ChoiceBox<String> minuteChoiceBox;
	@FXML private ChoiceBox<String> amPmChoiceBox;
	@FXML TextField paidAmountTextField;
	
	@FXML 
	private void initialize() {
		for (int i = 0; i < 12; i++) {
			String hour = (i + 1) + "";
			hourChoiceBox.getItems()
				.add((hour.length() == 1) ? hour : hour); // just add "0" + to the first condition
														  // if desired
		}
		hourChoiceBox.getSelectionModel().select(0);
		
		for (int i = 0; i < 59; i++) {
			String minute = (i + 1) + "";
			minuteChoiceBox.getItems()
				.add((minute.length() == 1) ? "0" + minute : minute);
		}
		minuteChoiceBox.getSelectionModel().select(0);
		
		amPmChoiceBox.getItems().add("A.M.");
		amPmChoiceBox.getItems().add("P.M.");
		amPmChoiceBox.getSelectionModel().select(0);
	}
	
	@FXML 
	protected void handleConfirmPaymentBtn(ActionEvent event) throws IOException {
		// TODO - Save new payment to the database
		Parent root = FXMLLoader.load(getClass().getResource("view/add_new_payment_success_dialog.fxml"));
		Stage stage = getStage(event);
		stage.setScene(new Scene(root));
		stage.setTitle("Success");
	}
	
	@FXML protected void handleCancelBtnPress(ActionEvent event) {
		getStage(event).close();
	}
}
