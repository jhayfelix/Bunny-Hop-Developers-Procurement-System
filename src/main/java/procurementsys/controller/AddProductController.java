package procurementsys.controller;

import java.util.Optional;


import procurementsys.model.Product;
import procurementsys.model.database.MySQLProductDAO;
import procurementsys.model.database.ProductDAO;
import procurementsys.view.SoftwareNotification;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

/**
 * @author Jan Tristan Milan
 */

public class AddProductController extends Controller {
	
	public static void run() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add Product");
		dialog.setHeaderText("Enter Product Details");
		dialog.setContentText("Product name:");
        dialog.setGraphic(null);
	
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            if (!isValidName(dialog.getEditor().getText())) {
    			String errorMsg = "Product name cannot be empty."
    					+ " Please enter a product name.";
    			SoftwareNotification.notifyError(errorMsg);
    			event.consume();
            }
        });
         
        Optional<String> name = dialog.showAndWait();
        if (name.isPresent()){
        	ProductDAO productDAO = new MySQLProductDAO();
        	productDAO.add(new Product(name.get()));
       
    		String successMsg = "The product \'" + name.get() 
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
	
	
}
