package procurementsys.controller;

import java.util.Optional;

import org.controlsfx.control.Notifications;
import procurementsys.model.Product;
import procurementsys.model.database.MySQLProductDAO;
import procurementsys.model.database.ProductDAO;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

/**
 * @author Jan Tristan Milan
 */

public class AddNewProductController extends Controller {
	
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
    			
    			Notifications.create().title("Error").text(errorMsg)
    				.position(Pos.TOP_RIGHT).showError();
                event.consume();
            }
        });
         
        Optional<String> name = dialog.showAndWait();
        if (name.isPresent()){
        	ProductDAO productDAO = new MySQLProductDAO();
        	productDAO.add(new Product(name.get()));
       
    		Notifications.create().title("Success")
			.text("The product \'" + name.get() 
				  + "\' has been successfully added to the system.")
			.position(Pos.TOP_RIGHT).showInformation();
        }
        
	}
	
	private static boolean isValidName(String name) {
		if (name == null || name.length() == 0) {
			return false;
		}
		return true;
	}
	
	
}
