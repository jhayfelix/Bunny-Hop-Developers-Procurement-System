package procurementsys.controller;

import java.util.Optional;

import procurementsys.model.Supplier;
import procurementsys.model.database.MySQLSupplierDAO;
import procurementsys.model.database.SupplierDAO;
import procurementsys.view.SoftwareNotification;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 * @author Jan Tristan Milan
 */

public class AddNewSupplierController extends Controller {
	
	public static void run() {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Add Supplier");
		dialog.setHeaderText("Enter supplier details");	
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		TextField nameTextField = new TextField();
		TextField contactNumberTextField = new TextField();
	
		grid.add(new Label("Name:"), 0, 0);
		grid.add(nameTextField, 1, 0);
		grid.add(new Label("Contact Number:"), 0, 1);
		grid.add(contactNumberTextField, 1, 1);
		
		dialog.getDialogPane().setContent(grid);
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == ButtonType.OK) {
		    	return new Pair<>(nameTextField.getText(), contactNumberTextField.getText());
		    }
		    return null;
		});
		
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
        	String name = nameTextField.getText();
        	String contactNumber = contactNumberTextField.getText();
            if (!isValidName(name)) {
    			String errorMsg = "Supplier name cannot be empty."
    					+ " Please enter a supplier name.";
    			SoftwareNotification.notifyError(errorMsg);
                event.consume();
            } else if (!isValidContactNum(contactNumber)) {
    			String errorMsg = (contactNumber == null || contactNumber.length() == 0)
    					? "Contact number cannot be empty. Please enter a contact number."
    					: "Contact number can only be composed of digits."
    							+ " Please enter another contact number.";
    			SoftwareNotification.notifyError(errorMsg);
                event.consume();
            }
        });
		
		Optional<Pair<String, String>> result = dialog.showAndWait();
		if (result.isPresent()) {
			Pair<String, String> pair = result.get();
			String name = pair.getKey();
			String contactNumber = pair.getValue();
			
			SupplierDAO supplierDAO = new MySQLSupplierDAO();
			supplierDAO.add(new Supplier(name, contactNumber));
			
			String successMsg = "The supplier \'" + name 
					  + "\' has been successfully added to the system.";
			SoftwareNotification.notifySuccess(successMsg);

		}
	}

	
	private static boolean isValidName(String name) {
		if (name == null || name.length() == 0) {
			return false;
		}
		return true;
	}
	
	private static boolean isValidContactNum(String contactNumber) {
		if (contactNumber == null || contactNumber.length() == 0) {
			return false;
		}
		
		for (int i = 0; i < contactNumber.length(); i++) {
			if (!Character.isDigit(contactNumber.charAt(i))) {
			 	return false;
			}
		}
		
		return true;
	}
	
}
