package procurementsys;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Jan Tristan Milan
 */

public class AddNewSupplierController extends Controller {
	@FXML private TextField nameTextField;
	@FXML private TextField contactNumberTextField;
	
	@FXML protected void handleConfirmSupplierDetailsBtnPress(ActionEvent event) throws IOException {
		String name = nameTextField.getText();
		// TODO - add code for adding to the system here
		
		Parent root = FXMLLoader.load(getClass()
				.getResource("view/add_new_supplier_success_dialog.fxml"));
		Stage stage = getStage(event);
		stage.setScene(new Scene(root));
		stage.setTitle("Success");
	}
	
	@FXML protected void handleCancelBtnPress(ActionEvent event) {
		getStage(event).close();
	}
	
}
