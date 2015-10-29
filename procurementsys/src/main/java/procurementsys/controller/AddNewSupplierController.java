package procurementsys.controller;

import java.io.IOException;

import procurementsys.model.Supplier;
import procurementsys.model.database.MySQLSupplierDAO;
import procurementsys.model.database.SupplierDAO;
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
		String contactNumber = contactNumberTextField.getText();
		if (!isValidName(name)) {
			
		} else if (!isValidContactNum(contactNumber)) {
			
		} else {
			SupplierDAO supplierDAO = new MySQLSupplierDAO();
			supplierDAO.add(new Supplier(name, contactNumber));
			
			Parent root = FXMLLoader.load(getClass()
					.getResource("/procurementsys/view/add_new_supplier_success_dialog.fxml"));
			Stage stage = getStage(event);
			stage.setScene(new Scene(root));
			stage.setTitle("Success");
		}
	}
	
	private boolean isValidName(String name) {
		if (name == null)
			return false;
		
		return true;
	}
	
	private boolean isValidContactNum(String contactNumber) {
		if (contactNumber == null)
			return false;
		
		for (int i = 0; i < contactNumber.length(); i++) {
			if (!Character.isDigit(contactNumber.charAt(i))) {
			 	return false;
			}
		}
		
		return true;
	}
	
	@FXML protected void handleCancelBtnPress(ActionEvent event) {
		getStage(event).close();
	}
	
}
